package io.dvp.ds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryOperatorTest {
    BinaryOperator operator = new BinaryOperator("+", 10);
    Symbol left, right, middle;

    @BeforeEach
    void init() {
        left = new IntegerOperand().copy("12").get();
        right = new IntegerOperand().copy("9").get();
        middle = new IntegerOperand().copy("38").get();
    }

    @Test
    void copy() {
        assertNotSame(operator, operator.copy("+").get());
        assertFalse(operator.copy("+ ").isPresent());
        assertFalse(operator.copy("a").isPresent());
        assertFalse(operator.copy("2").isPresent());
    }

    @Test
    void merge() {
        assertEquals(operator, operator.merge(left));
        assertEquals(operator, operator.merge(right));
        assertEquals("[[12]+[9]]", operator.toString());

        Symbol op2 = operator.merge(new BinaryOperator("+", 0));
        assertNotEquals(op2, operator);
        assertEquals("[[[12]+[9]]+null]", op2.toString());
    }

    @Test
    void mergeHeavierOperator() {
        BinaryOperator heavier = new BinaryOperator("/", 20);
        operator.merge(left).merge(middle);
        assertSame(operator, operator.merge(heavier));
        assertSame(operator, operator.merge(right));
        assertEquals("[[12]+[[38]/[9]]]", operator.toString());
    }
}
