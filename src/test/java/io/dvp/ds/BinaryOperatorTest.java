package io.dvp.ds;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryOperatorTest {
    BinaryOperator op = new BinaryOperator("+");

    @Test
    void match() {
        assertTrue(op.matches("+"));
        assertFalse(op.matches("++"));
        assertFalse(op.matches("23"));
        assertFalse(op.matches("+ "));
    }

    @Test
    void copy() {
        Symbol other = op.copy("+");
        assertNotEquals(other, op);
    }

    @Test
    void merge() {
        Symbol operand1 = new IntegerOperand().copy("12");
        Symbol operand2 = new IntegerOperand().copy("9");

        assertEquals(op, op.merge(operand1));
        assertEquals(op, op.merge(operand2));
        assertEquals("[[12]+[9]]", op.toString());

        Symbol op2 = op.merge(new BinaryOperator("+"));
        assertNotEquals(op2, op);
        assertEquals("[[[12]+[9]]+null]", op2.toString());
    }
}
