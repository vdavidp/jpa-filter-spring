package io.dvp.ds;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BinaryOperatorTest {
    @Mock
    Symbol left, right;

    BinaryOperator op = new BinaryOperator() {
        @Override
        protected String getSymbol() {
            return ":";
        }
    };

    @Test
    public void match() {
        assertTrue(op.matches(":"));
        assertFalse(op.matches(":d"));
        assertFalse(op.matches("3"));
    }

    @Test
    public void copy() {
        assertEquals(op, op.copy("+"));
        assertEquals(op, op.copy(null));
        assertEquals(op, op.copy(""));
    }

    @Test
    public void merge() {
        assertEquals(op, op.merge(left));
        assertEquals(op, op.merge(right));
    }

    @Test
    public void checkToString() {
        when(left.toString()).thenReturn("[3]");
        when(right.toString()).thenReturn("[5]");
        op.merge(left);
        op.merge(right);
        assertEquals("[[3]:[5]]", op.toString());
    }
}

