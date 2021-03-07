package io.dvp.jpa.filter.el;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DecimalFactoryTest {
    DecimalFactory op = new DecimalFactory();

    @Test
    void copy() {
        assertNotSame(op, op.copy("23.32").get());
        assertTrue(op.copy("34.0").isPresent());
        assertTrue(op.copy("0.33").isPresent());

        assertFalse(op.copy("34").isPresent());
        assertFalse(op.copy(".32").isPresent());
        assertFalse(op.copy("34.").isPresent());
        assertFalse(op.copy("2.2.2").isPresent());
        assertFalse(op.copy("a").isPresent());
        assertFalse(op.copy("").isPresent());
    }
}
