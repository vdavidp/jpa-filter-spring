package io.dvp.ds;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class IntegerOperandTest {
    IntegerOperand number = new IntegerOperand();

    @Test
    void copy() {
        Optional<Symbol> other = number.copy("234");
        assertTrue(other.isPresent());
        assertSame(other.get().getClass(), number.getClass());
        assertNotSame(other.get(), number);
        assertEquals("[234]", other.get().toString());

        assertFalse(number.copy("234.33").isPresent());
        assertFalse(number.copy("a").isPresent());
        assertFalse(number.copy(" 12 ").isPresent());
        assertFalse(number.copy("").isPresent());
    }

    @Test
    void merge() {
        Optional<Symbol> num = number.copy("9");
        Symbol other = num.get().merge(new BinaryOperator("+", 10));
        assertEquals("[[9]+null]", other.toString());
    }
}
