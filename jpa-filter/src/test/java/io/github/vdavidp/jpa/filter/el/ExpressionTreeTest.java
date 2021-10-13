/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import io.github.vdavidp.jpa.filter.el.UnaryOperator.Order;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static java.util.Arrays.asList;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 *
 * @author david
 */
public class ExpressionTreeTest {
  List<Operator> operators = asList(new BinaryOperator("*", 20), new BinaryOperator("-", 10), new BinaryOperator("+", 10));
  List<Operand> operands = asList(new NumberOperand(), new StringOperand());

  @Test
  void fromEmptyText() {
    ExpressionTree et = new ExpressionTree("", asList(new NumberOperand()), asList(new BinaryOperator("+", 10)));
    assertEquals("[]", et.toString());
  }

  @Test
  void fromNumber() {
    ExpressionTree et = new ExpressionTree("12", asList(new NumberOperand()), asList());
    assertEquals("[12]", et.toString());
  }
  
  @Test
  void fromDecimal() {
    ExpressionTree et = new ExpressionTree("9543.4332", asList(new DecimalOperand()), asList());
    assertEquals("[9543.4332]", et.toString());
  }
  
  @Test
  void fromString() {
    ExpressionTree et = new ExpressionTree("'text'", asList(new StringOperand()), asList());
    assertEquals("[text]", et.toString());
  }
  
  @Test
  void fromInvalidExpression() {
    assertThrows(ParseException.class, () -> {
      ExpressionTree et = new ExpressionTree("( and )", asList(), asList());
      et.toString();
    });
  }
  
  @Test
  void fromSingleVariable() {
    ExpressionTree et = new ExpressionTree("prop", asList(new VariableOperand()), asList());
    assertEquals("[prop]", et.toString());
  }
  
  @Test
  void fromNestedVariable() {
    ExpressionTree et = new ExpressionTree("obj.prop", asList(new VariableOperand()), asList());
    assertEquals("[obj.prop]", et.toString());
  }

  @Test
  void fromBinaryExpression() {
    ExpressionTree et = new ExpressionTree("12 + 4", asList(new NumberOperand()), asList(new BinaryOperator("+", 10)));
    assertEquals("[[12]+[4]]", et.toString());
  }

  @Test
  void fromTwoSameWeightBinaryOperators() {
    ExpressionTree et = new ExpressionTree("98 + 12 - 43", asList(new NumberOperand()), asList(new BinaryOperator("+", 10), new BinaryOperator("-", 10)));
    assertEquals("[[[98]+[12]]-[43]]", et.toString());
  }

  @Test
  void fromBinaryOperatorsDifferentWeight() {
    ExpressionTree et = new ExpressionTree("3 + 2 * 4", asList(new NumberOperand()), asList(new BinaryOperator("+", 10), new BinaryOperator("*", 20)));
    assertEquals("[[3]+[[2]*[4]]]", et.toString());
  }

  @Test
  void fromBinaryOperatorsSparcedDifferentWeights() {
    ExpressionTree et = new ExpressionTree("3 + 2 * 4 + 8 * 2", asList(new NumberOperand()), asList(new BinaryOperator("+", 10), new BinaryOperator("*", 20)));
    assertEquals("[[[3]+[[2]*[4]]]+[[8]*[2]]]", et.toString());
  }

  @Test
  void fromBinaryAndLeftUnaryOperator() {
    ExpressionTree et = new ExpressionTree("2 + -3", asList(new NumberOperand()), asList(new BinaryOperator("+", 10), new UnaryOperator("-", 30, Order.LEFT)));
    assertEquals("[[2]+[-[3]]]", et.toString());
  }
  
  @Test
  void fromLeftUnaryAndBinaryOperator() {
    ExpressionTree et = new ExpressionTree("-3 + 2", asList(new NumberOperand()), asList(new BinaryOperator("+", 10), new UnaryOperator("-", 30, Order.LEFT)));
    assertEquals("[[-[3]]+[2]]", et.toString());
  }
  
  @Test
  void fromBinaryAndRightUnaryOperator() {
    ExpressionTree et = new ExpressionTree("3 + 5 Is Not Null", asList(new NumberOperand()), asList(new BinaryOperator("+", 10), new UnaryOperator("Is Not Null", 30, Order.RIGHT)));
    assertEquals("[[3]+[[5]Is Not Null]]", et.toString());
  }
  
  @Test
  void fromExpressionContainingParentheses() {
    ExpressionTree et = new ExpressionTree("(3 * (5 - 2))", asList(new NumberOperand()), asList(new BinaryOperator("*", 20), new BinaryOperator("-", 10)));
    assertEquals("[[3]*[[5]-[2]]]", et.toString());
  }
  
  @Test
  void negativeParenthesesCount() {
    Throwable t = assertThrows(ParseException.class, () -> new ExpressionTree("(3 * (5 - 2)))", operands, operators));
    assertEquals("Parentheses don't match", t.getMessage());
  }

  @Test
  void stringContainsBinaryOperator() {
    ExpressionTree et = new ExpressionTree("'ii+d' + 'txt'", operands, operators);
    assertEquals("[[ii+d]+[txt]]", et.toString());
  }
  
  @Test
  void stringContainsLeftUnaryOperator() {
    ExpressionTree et = new ExpressionTree("-'i-id'", operands, asList(new UnaryOperator("-", 30, Order.LEFT)));
    assertEquals("[-[i-id]]", et.toString());
  }
  
  @Test
  void stringContainsRightUnaryOperator() {
    ExpressionTree et = new ExpressionTree("'data IS NULL' IS NULL", operands, asList(new UnaryOperator("IS NULL", 30, Order.RIGHT)));
    assertEquals("[[data IS NULL]IS NULL]", et.toString());
  }
  
}
