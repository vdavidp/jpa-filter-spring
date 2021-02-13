package io.dvp.ds;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpressionTreeTest {
    @Mock
    Symbol operatorProto, operandProto, left, right, op;

    @Test
    public void parseNumberOperand() {
        ExpressionTree et = ExpressionTree.build("1");
        assertEquals("1", et.toString());
    }

    @Test
    public void parseBinaryOperator() {
        when(operatorProto.matches("+")).thenReturn(true);
        when(operatorProto.matches(not(eq("+")))).thenReturn(false);
        when(operandProto.matches(or(eq("4"), eq("3")))).thenReturn(true);
        when(operandProto.matches(and(not(eq("4")), not(eq("3"))))).thenReturn(false);
        when(operandProto.copy(any())).thenReturn(left).thenReturn(right);
        when(operatorProto.copy(any())).thenReturn(op);
        when(left.merge(op)).thenReturn(op);
        when(op.merge(right)).thenReturn(op);

        ExpressionTree et = ExpressionTree.build("4 + 3", operatorProto, operandProto);

        verify(left).merge(op);
        verify(op).merge(right);
    }
}
