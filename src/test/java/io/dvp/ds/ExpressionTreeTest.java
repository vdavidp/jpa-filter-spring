package io.dvp.ds;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpressionTreeTest {
    @Mock
    Symbol operatorProto, operandProto, left, right, middle, outlier, op1, op2;

    @Test
    public void parseNumberOperand() {
        ExpressionTree et = ExpressionTree.build("1");
        assertEquals("1", et.toString());
    }

    @Test
    public void parseBinaryOperator() {
        when(operatorProto.matches("+")).thenReturn(true);
        when(operatorProto.matches(not(eq("+")))).thenReturn(false);

        when(operandProto.matches(matches("^[1234]+$"))).thenReturn(true);
        when(operandProto.matches(not(matches("^[1234]+$")))).thenReturn(false);

        when(operandProto.copy(matches("^4+$"))).thenReturn(left);
        when(operandProto.copy(matches("^[123]+$"))).thenReturn(right);
        when(operatorProto.copy(any())).thenReturn(op1);

        when(left.merge(op1)).thenReturn(op1);
        when(op1.merge(right)).thenReturn(op1);

        ExpressionTree.build("44 + 321", operatorProto, operandProto);

        verify(left).merge(op1);
        verify(op1).merge(right);
    }

    @Test
    public void parseChainedBinaryOperators() {
        when(operatorProto.matches("+")).thenReturn(true);
        when(operatorProto.matches(not(eq("+")))).thenReturn(false);

        when(operandProto.matches(matches("^[12345]+$"))).thenReturn(true);
        when(operandProto.matches(not(matches("^[12345]+$")))).thenReturn(false);

        when(operandProto.copy("4")).thenReturn(left);
        when(operandProto.copy("1")).thenReturn(middle);
        when(operandProto.copy("235")).thenReturn(right);
        when(operandProto.copy(matches("^[23]+$"))).thenReturn(outlier);
        when(operatorProto.copy(any())).thenReturn(op1, op2);
        
        when(left.merge(op1)).thenReturn(op1);
        when(op1.merge(middle)).thenReturn(op1);
        when(op1.merge(op2)).thenReturn(op2);
        when(op2.merge(right)).thenReturn(op2);

        ExpressionTree.build("4 + 1 + 235", operatorProto, operandProto);

        verify(left).merge(op1);
        verify(op1).merge(middle);
        verify(op1).merge(op2);
        verify(op2).merge(right);
    }
}
