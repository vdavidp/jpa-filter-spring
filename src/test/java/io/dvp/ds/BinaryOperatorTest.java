package io.dvp.ds;

import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryOperatorTest {
    BinaryOperator op = new BinaryOperator("+");

    @Test
    void copy() {
        assertNotEquals(op, op.copy("+"));
        assertFalse(op.copy("+ ").isPresent());
        assertFalse(op.copy("a").isPresent());
        assertFalse(op.copy("2").isPresent());
    }

    @Test
    void merge() {
        Optional<Symbol> operand1 = new IntegerOperand().copy("12");
        Optional<Symbol> operand2 = new IntegerOperand().copy("9");

        assertEquals(op, op.merge(operand1.get()));
        assertEquals(op, op.merge(operand2.get()));
        assertEquals("[[12]+[9]]", op.toString());

        Symbol op2 = op.merge(new BinaryOperator("+"));
        assertNotEquals(op2, op);
        assertEquals("[[[12]+[9]]+null]", op2.toString());
    }
}
