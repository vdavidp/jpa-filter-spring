package io.dvp.ds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntegerOperandTest {
    IntegerOperand number = new IntegerOperand();

    @Test
    void match() {
        assertTrue(number.matches("123"));
        assertFalse(number.matches("304.23"));
        assertFalse(number.matches("abc"));
        assertFalse(number.matches("0xffff"));
    }

    @Test
    void copy() {
        Symbol other = number.copy("234");
        assertSame(other.getClass(), number.getClass());
        assertNotSame(other, number);
        assertEquals("[234]", other.toString());
    }

    @Test
    void merge() {
        Symbol num = number.copy("9");
        Symbol other = num.merge(new BinaryOperator("+"));
        assertEquals("[[9]+null]", other.toString());
    }
}
