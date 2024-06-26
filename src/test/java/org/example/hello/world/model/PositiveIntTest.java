package org.example.hello.world.model;

import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.Test;

class PositiveIntTest {

    @Test
    void constructor_createsObject_givenPositivePrimitiveInt() {
        assertThatNoException().isThrownBy(() -> new PositiveInt(1));
    }

}
