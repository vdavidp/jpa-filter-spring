package io.dvp.ds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DecimalOperandTest {
    DecimalOperand op = new DecimalOperand();

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

    @Test
    void merge() {
        Symbol num = op.copy("12.32").get();
        Symbol binaryOp = new BinaryOperator("+", 10);
        assertSame(binaryOp, num.merge(binaryOp));
        assertEquals("[[12.32]+null]", binaryOp.toString());

    }
}
