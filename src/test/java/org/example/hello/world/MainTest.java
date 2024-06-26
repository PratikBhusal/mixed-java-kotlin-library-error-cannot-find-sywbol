package org.example.hello.world;

import static org.assertj.core.api.Assertions.assertThatNoException;

import org.example.hello.world.model.PositiveInt;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void testMain() {
        assertThatNoException().isThrownBy(() -> new PositiveInt(1));
        assertThatNoException().isThrownBy(() -> Main.main(new String[]{
            "200"
        })
        );
    }

}
