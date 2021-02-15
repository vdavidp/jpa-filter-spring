package io.dvp.ds;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class NumberOperandTest {
    NumberOperand number = spy(NumberOperand.class);

    @BeforeEach
    void init() {
        when(number.toString()).thenReturn("[2]");
    }

    @Test
    void merge() {
        Symbol other = number.merge(new BinaryOperator("+", 10));
        assertEquals("[[2]+null]", other.toString());
    }
}
