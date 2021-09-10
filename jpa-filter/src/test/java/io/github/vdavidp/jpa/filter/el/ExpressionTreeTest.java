package io.github.vdavidp.jpa.filter.el;

import io.github.vdavidp.jpa.filter.el.UnaryOperator.Order;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

public class ExpressionTreeTest {

  @Test
  void parseNumber() {
    ExpressionTree et = new ExpressionTree.Builder().build("912", new IntegerOperand());
    assertEquals("[912]", et.toString());
  }

  @Test
  void parseBinaryOperation() {
    ExpressionTree et = new ExpressionTree.Builder().build("84 + 31",
        new IntegerOperand(),
        new BinaryOperator("+", 10));
    assertEquals("[[84]+[31]]", et.toString());
  }

  @Test
  void parseChainedBinaryOperation() {
    ExpressionTree et = new ExpressionTree.Builder().build("4 + 5 + 123",
        new IntegerOperand(),
        new BinaryOperator("+", 10));
    assertEquals("[[[4]+[5]]+[123]]", et.toString());
  }

  @Test
  void parseOperationWithTwoDifferentOperatorPrecedence() {
    ExpressionTree et = new ExpressionTree.Builder().build("14 + 12 / 4",
        new IntegerOperand(),
        new BinaryOperator("+", 10),
        new BinaryOperator("/", 20));
    assertEquals("[[14]+[[12]/[4]]]", et.toString());
  }

  @Test
  void parseOperationWithThreeDifferentOperatorPrecedence() {
    ExpressionTree et = new ExpressionTree.Builder().build("14 + 12 / 4 % 3",
        new IntegerOperand(),
        new BinaryOperator("+", 10),
        new BinaryOperator("/", 20),
        new BinaryOperator("%", 20));
    assertEquals("[[14]+[[[12]/[4]]%[3]]]", et.toString());
  }

  @Test
  void parseOperationWithTwoDifferentOperatorPrecedenceMultipleOccurrences() {
    ExpressionTree et = new ExpressionTree.Builder().build("14 + 12 / 4 + 3 / 9",
        new IntegerOperand(),
        new BinaryOperator("+", 10),
        new BinaryOperator("/", 20),
        new BinaryOperator("%", 20));
    assertEquals("[[[14]+[[12]/[4]]]+[[3]/[9]]]", et.toString());
  }

  @Test
  void parseFactoryOperator() {
    List<Symbol> factories = singletonList(new DecimalFactory());

    ExpressionTree et = new ExpressionTree.Builder().build("4.32 + 22.2",
        new IntegerOperand(), new BinaryOperator("+", 10),
        new FactoryOperator(".", 50, factories));
    assertEquals("[[4.32]+[22.2]]", et.toString());
  }

  @Test
  void parseVariableOperand() {
    List<Symbol> factories = singletonList(new DecimalFactory());

    ExpressionTree et = new ExpressionTree.Builder().build("{obj.prop.subProp} + 4.32 + {otherProp}",
        new IntegerOperand(),
        new VariableOperand(),
        new BinaryOperator("+", 10),
        new FactoryOperator(".", 50, factories));
    assertEquals("[[[obj.prop.subProp]+[4.32]]+[otherProp]]", et.toString());
  }

  @Test
  void parseVarcharOperand() {
    ExpressionTree et = new ExpressionTree.Builder().build("'Scape with backslashes\\\\' = 'That\\'s all'",
        new BinaryOperator("=", 10),
        new VarcharOperand());
    assertEquals("[[Scape with backslashes\\]=[That's all]]", et.toString());
  }

  @Test
  void parseRightUnaryOperator() {
    ExpressionTree et = new ExpressionTree.Builder().build("{data} is Null",
        new UnaryOperator("Is Null", 10, Order.RIGHT),
        new VariableOperand());
    assertEquals("[[data]Is Null]", et.toString());
  }
  
  @Test
  void parseLeftUnaryOperator() {
    ExpressionTree et = new ExpressionTree.Builder().build("Date '2010-02-23'", 
        new UnaryOperator("Date", 10, Order.LEFT),
        new VarcharOperand());
    assertEquals("[Date[2010-02-23]]", et.toString());
  }

  @Test
  void parseParenthesis() {
    ExpressionTree et = new ExpressionTree.Builder().build("(8+3)*((2+4)*6)",
        new BinaryOperator("+", 10),
        new BinaryOperator("*", 20),
        new IntegerOperand());
    assertEquals("[[[8]+[3]]*[[[2]+[4]]*[6]]]", et.toString());
  }
}
