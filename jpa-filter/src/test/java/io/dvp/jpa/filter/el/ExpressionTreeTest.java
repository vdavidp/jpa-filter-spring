package io.dvp.jpa.filter.el;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

public class ExpressionTreeTest {

  @Test
  void parseNumber() {
    ExpressionTree et = ExpressionTree.build("912", new IntegerOperand());
    assertEquals("[912]", et.toString());
  }

  @Test
  void parseBinaryOperation() {
    ExpressionTree et = ExpressionTree.build("84 + 31",
        new IntegerOperand(),
        new BinaryOperator("+", 10));
    assertEquals("[[84]+[31]]", et.toString());
  }

  @Test
  void parseChainedBinaryOperation() {
    ExpressionTree et = ExpressionTree.build("4 + 5 + 123",
        new IntegerOperand(),
        new BinaryOperator("+", 10));
    assertEquals("[[[4]+[5]]+[123]]", et.toString());
  }

  @Test
  void parseOperationWithTwoDifferentOperatorPrecedence() {
    ExpressionTree et = ExpressionTree.build("14 + 12 / 4",
        new IntegerOperand(),
        new BinaryOperator("+", 10),
        new BinaryOperator("/", 20));
    assertEquals("[[14]+[[12]/[4]]]", et.toString());
  }

  @Test
  void parseOperationWithThreeDifferentOperatorPrecedence() {
    ExpressionTree et = ExpressionTree.build("14 + 12 / 4 % 3",
        new IntegerOperand(),
        new BinaryOperator("+", 10),
        new BinaryOperator("/", 20),
        new BinaryOperator("%", 20));
    assertEquals("[[14]+[[[12]/[4]]%[3]]]", et.toString());
  }

  @Test
  void parseOperationWithTwoDifferentOperatorPrecedenceMultipleOccurrences() {
    ExpressionTree et = ExpressionTree.build("14 + 12 / 4 + 3 / 9",
        new IntegerOperand(),
        new BinaryOperator("+", 10),
        new BinaryOperator("/", 20),
        new BinaryOperator("%", 20));
    assertEquals("[[[14]+[[12]/[4]]]+[[3]/[9]]]", et.toString());
  }

  @Test
  void parseFactoryOperator() {
    List<Symbol> factories = singletonList(new DecimalFactory());

    ExpressionTree et = ExpressionTree.build("4.32 + 22.2",
        new IntegerOperand(), new BinaryOperator("+", 10),
        new FactoryOperator(".", 50, factories));
    assertEquals("[[4.32]+[22.2]]", et.toString());
  }

  @Test
  void parseVariableOperand() {
    List<Symbol> factories = singletonList(new DecimalFactory());

    ExpressionTree et = ExpressionTree.build("{obj.prop.subProp} + 4.32 + {otherProp}",
        new IntegerOperand(),
        new VariableOperand(),
        new BinaryOperator("+", 10),
        new FactoryOperator(".", 50, factories));
    assertEquals("[[[obj.prop.subProp]+[4.32]]+[otherProp]]", et.toString());
  }

  @Test
  void parseVarcharOperand() {
    ExpressionTree et = ExpressionTree.build("'Scape with backslashes\\\\' = 'That\\'s all'",
        new BinaryOperator("=", 10),
        new VarcharOperand());
    assertEquals("[[Scape with backslashes\\]=[That's all]]", et.toString());
  }

  @Test
  void parseRightUnaryOperator() {
    ExpressionTree et = ExpressionTree.build("{data} is Null",
        new RightUnaryOperator("Is Null", 10),
        new VariableOperand());
    assertEquals("[[data]IS NULL]", et.toString());
  }
}
