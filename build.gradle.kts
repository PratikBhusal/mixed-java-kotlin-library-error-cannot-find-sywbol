import org.gradle.api.tasks.testing.logging.TestLogEvent

// Tip: Use `apply false` in the top-level build.gradle file to add a Gradle
// plugin as a build dependency but not apply it to the current (root) project.
// Don't use `apply false` in sub-projects.
//
// For more information, see the following on applying external plugins with
// same version to subprojects:
//
// - https://docs.gradle.org/current/userguide/dependency_management_basics.html
// - https://docs.gradle.org/current/samples/sample_building_java_applications_multi_project.html
// - https://docs.gradle.org/current/dsl/org.gradle.plugin.use.PluginDependenciesSpec.html#org.gradle.plugin.use.PluginDependenciesSpec:id(java.lang.String)
plugins {
    id("java")

    // Code Coverage
    id("jacoco")

    // Code Formatter
    alias(libs.plugins.spotless)
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Maven Scope -> Gradle Configuration Translation/Overview
// =============================================================================
// `compile` -> `implementation` or `api`. Prefer `implementation` unless you
//              have good reason to expose the dependency to consumers.
//
// `test`    -> `testImplementation`
//
// `runtime` -> `runtimeOnly`
//
// See:
// - https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_configurations_graph
// - https://reflectoring.io/maven-scopes-gradle-configurations
// - https://kotlinlang.org/docs/gradle-configure-project.html#dependency-types
dependencies {
    compileOnly(libs.jakarta.annotation.api)
    compileOnly(libs.checker.framework.qualifier)

    runtimeOnly(libs.log4j.core)

    implementation(libs.bundles.jvm.shared.logging.implementation)
    implementation(libs.arrow.core)

    // See:
    //
    // - https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-display-names
    testImplementation(libs.bundles.jvm.shared.testing)
}

tasks {
    test {
        // After running test, always generate report
        finalizedBy(jacocoTestReport)

        useJUnitPlatform()
        testLogging { events(
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED,
            TestLogEvent.FAILED
        )}
    }

    javadoc {
        doLast {
            println("[Documentation]: file://${this@javadoc.destinationDir}/index.html")
        }
    }

    jacocoTestReport {
        // Before generating report, always run tests
        dependsOn(test)

        reports {
            xml.required = false
            csv.required = false
            html.required = true
        }

        doLast {
            println("[Coverage]: file://${this@jacocoTestReport.reports.html.entryPoint}")
        }
    }
}

spotless {
    // optional: limit format enforcement to just the files changed by this
    // feature branch
    ratchetFrom("origin/main")

    format("misc") {
        // define the files to apply `misc` to
        target("*.gradle.kts", ".gitattributes", ".gitignore")

        // define the steps to apply to those files
        trimTrailingWhitespace()
        indentWithSpaces(4)
        endWithNewline()
    }
    java {
        // don't need to set target, it is inferred from java

        // Automatic code refactoring with Cleanthat. In the future, may replace or
        // also include eclipse clean up refactoring tool. See:
        //
        // - https://github.com/solven-eu/cleanthat/blob/master/MUTATORS.generated.MD
        // - https://github.com/solven-eu/cleanthat/blob/master/MUTATORS_BY_TAG.generated.MD
        cleanthat()
            // Java 17 is the latest defined version. See:
            //
            // Tracking:  https://github.com/solven-eu/cleanthat/blob/master/refactorer/src/main/java/eu/solven/cleanthat/engine/java/IJdkVersionConstants.java#L60
            // Permalink: https://github.com/solven-eu/cleanthat/blob/c97ded164de19cc1c90bd3a9162ed794dae22ee9/refactorer/src/main/java/eu/solven/cleanthat/engine/java/IJdkVersionConstants.java#L60
            .sourceCompatibility("17")
            .includeDraft(true)
            .addMutator("SafeAndConsensual")
            .addMutator("SafeButControversial")
            .addMutator("SafeButNotConsensual")
            .addMutator("CheckStyleMutators")
            .addMutator("ErrorProneMutators")
            .addMutator("Guava")
            .addMutator("JSparrowMutators")
            .addMutator("PMDMutators")
            .addMutator("SonarMutators")
            .addMutator("SpotBugsMutators")
            .addMutator("Stream")
            .excludeMutator("AvoidInlineConditionals")
            .excludeMutator("CreateTempFilesUsingNio")
            .excludeMutator("LiteralsFirstInComparisons")
            .excludeMutator("RemoveExplicitCallToSuper")
            .excludeMutator("SimplifyBooleanExpression")
            .excludeMutator("SimplifyStartsWith")

        // Run eclipse code formatter profile
        eclipse("4.30")
            .configFile(".config/eclipse-code-formatter-profile.xml")

        removeUnusedImports()

        // import order file as exported from eclipse. See:
        //
        // https://github.com/diffplug/spotless/blob/main/ECLIPSE_SCREENSHOTS.md#creating-spotlessimportorder
        importOrderFile(".config/eclipse-import-order.importorder")

        trimTrailingWhitespace()
        endWithNewline()
    }
}
