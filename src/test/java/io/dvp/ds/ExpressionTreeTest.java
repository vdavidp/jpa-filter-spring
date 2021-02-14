package io.dvp.ds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpressionTreeTest {

    @Test
    void parseNumber() {
        ExpressionTree et = ExpressionTree.build("912", new IntegerOperand());
        assertEquals("[912]", et.toString());
    }

    @Test
    void parseBinaryOperation() {
        ExpressionTree et = ExpressionTree.build("84 + 31", new IntegerOperand(), new BinaryOperator("+"));
        assertEquals("[[84]+[31]]", et.toString());
    }

    @Test
    void parseChainedBinaryOperation() {
        ExpressionTree et = ExpressionTree.build("4 + 5 + 123", new IntegerOperand(), new BinaryOperator("+"));
        assertEquals("[[[4]+[5]]+[123]]", et.toString());
    }
}
