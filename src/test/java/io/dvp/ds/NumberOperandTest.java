package io.dvp.ds;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NumberOperandTest {
    Symbol operand = new NumberOperand().copy("8");
    BinaryOperator operator = new BinaryOperator() {
        @Override
        protected String getSymbol() {
            return ":";
        }
    };

    @Test
    public void match() {
        assertTrue(operand.matches("393"));
        assertFalse(operand.matches("+33"));
        assertFalse(operand.matches("-33"));
        assertFalse(operand.matches("2d"));
    }

    @Test
    public void merge() {
        assertEquals(operator, operand.merge(operator));
        assertEquals("[[8]:null]", operator.toString());
        assertThrows(RuntimeException.class, () -> operand.merge(new NumberOperand()));
        assertThrows(RuntimeException.class, () -> operand.merge(null));
    }

    @Test
    public void copy() {
        assertEquals("[4321]", operand.copy("4321").toString());
    }
}
