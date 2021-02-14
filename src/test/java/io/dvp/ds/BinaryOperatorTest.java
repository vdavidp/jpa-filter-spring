package io.dvp.ds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BinaryOperatorTest {
    @Mock
    Symbol left, right;

    @Spy
    BinaryOperator operator, operator2;

    @BeforeEach
    public void init() {
        when(operator.getSymbol()).thenReturn(":");
    }

    @Test
    public void match() {
        assertTrue(operator.matches(":"));
        assertFalse(operator.matches(":d"));
        assertFalse(operator.matches("3"));
    }

    @Test
    public void merge() {
        when(operator2.getSymbol()).thenReturn(":");
        when(left.toString()).thenReturn("[3]");
        when(right.toString()).thenReturn("[5]");

        assertEquals(operator, operator.merge(left));
        assertEquals(operator, operator.merge(right));
        assertEquals(operator2, operator.merge(operator2));
        assertEquals("[[[3]:[5]]:null]", operator2.toString());
    }
}

