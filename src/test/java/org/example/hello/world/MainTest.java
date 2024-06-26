package org.example.hello.world;

import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void testMain() {
        assertThatNoException().isThrownBy(() -> Main.main(new String[]{
            "200"
        })
        );
    }

}
