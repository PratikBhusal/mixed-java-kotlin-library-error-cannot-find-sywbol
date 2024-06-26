import type { RuleConfigTuple, UserConfig } from "@commitlint/types";
import resolveExtends from '@commitlint/resolve-extends';
import { RuleConfigSeverity } from "@commitlint/types";

/* Known `type-enum`s of writing this:
 *
 * build:
 *  Changes that affect the build system or external dependencies (example
 *  scopes: gulp, broccoli, npm)
 *
 * chore:
 *  Change to the build process or auxiliary libraries/tools
 *
 * ci:
 *  Changes to our CI configuration files and scripts (example scopes:
 *  Travis, Circle, BrowserStack, SauceLabs)
 *
 * docs:
 *  Documentation only changes
 *
 * feat:
 *  Introduces a new feature
 *
 * fix:
 *  Patches a bug
 *
 * perf:
 *  Performance improvement
 *
 * refactor:
 *  A code change that rewrites existing code to improve readability,
 *  reusability, or structure. It neither fixes a bug nor adds a feature.
 *
 * revert:
 *  Undo previous changes
 *
 * style:
 *  Changes that do not affect the meaning of the code
 *
 * test:
 *  Add missing tests or correcting existing tests
 *
 * wip:
 *  Note: This is a custom type prefix
 *  Indicate that this is a checkpoint commit used to safe changes.
 *  It should eventually be fixup/squashed.
 */
const Configuration: UserConfig = {
  extends: ["@commitlint/config-conventional"],
  helpUrl: `https://github.com/conventional-changelog/commitlint/#what-is-commitlint

    Once you're ready to fix your commit message, run:

    git commit --edit --file $(git rev-parse --git-path COMMIT_EDITMSG)
  `,
  rules: {
    "subject-case": [
      RuleConfigSeverity.Error,
      "always",
      ["lower-case", "sentence-case"],
    ],
    "type-enum": [
      RuleConfigSeverity.Error,
      "always",
      // Concatenates "wip" to the default list of allowed type prefixes.
      (
        resolveExtends({extends: ["@commitlint/config-conventional"]})
        .rules
        ?.["type-enum"] as RuleConfigTuple<string[]>
      )[2]?.concat("wip") as string[]
    ]
  },
};

module.exports = Configuration;
