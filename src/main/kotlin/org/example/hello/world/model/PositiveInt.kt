package org.example.hello.world.model

import org.checkerframework.checker.index.qual.Positive

@JvmInline
value class PositiveInt(private val value: @Positive Int) {
    init {
        require(value > 0) {
            "Value must be a positive integer. Was actually: $value"
        }
    }

    override fun toString() = value.toString()
}
