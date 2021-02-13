package io.dvp.ds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpressionTreeIT {
    Symbol bOp = new BinaryOperator() {
        @Override
        protected String getSymbol() {
            return "+";
        }
    };

    @Test
    public void performSimpleSum() {
        ExpressionTree tree = ExpressionTree.build("2+3", bOp, new NumberOperand());
        assertEquals("[[2]+[3]]", tree.toString());
    }
}
