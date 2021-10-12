/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import io.github.vdavidp.jpa.filter.el.UnaryOperator.Order;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 *
 * @author david
 */
public class ExpressionTreeTest {

  @Test
  void fromEmptyText() {
    ExpressionTree et = new ExpressionTree("", List.of(new NumberOperand()), List.of(new BinaryOperator("+", 10)));
    assertEquals("[]", et.toString());
  }

  @Test
  void fromNumber() {
    ExpressionTree et = new ExpressionTree("12", List.of(new NumberOperand()), List.of());
    assertEquals("[12]", et.toString());
  }
  
  @Test
  void fromDecimal() {
    ExpressionTree et = new ExpressionTree("9543.4332", List.of(new DecimalOperand()), List.of());
    assertEquals("[9543.4332]", et.toString());
  }
  
  @Test
  void fromString() {
    ExpressionTree et = new ExpressionTree("'text'", List.of(new StringOperand()), List.of());
    assertEquals("[text]", et.toString());
  }
  
  @Test
  void fromInvalidExpression() {
    assertThrows(ParseException.class, () -> {
      ExpressionTree et = new ExpressionTree("( and )", List.of(), List.of());
      et.toString();
    });
  }
  
  @Test
  void fromSingleVariable() {
    ExpressionTree et = new ExpressionTree("prop", List.of(new VariableOperand()), List.of());
    assertEquals("[prop]", et.toString());
  }
  
  @Test
  void fromNestedVariable() {
    ExpressionTree et = new ExpressionTree("obj.prop", List.of(new VariableOperand()), List.of());
    assertEquals("[obj.prop]", et.toString());
  }

  @Test
  void fromBinaryExpression() {
    ExpressionTree et = new ExpressionTree("12 + 4", List.of(new NumberOperand()), List.of(new BinaryOperator("+", 10)));
    assertEquals("[[12]+[4]]", et.toString());
  }

  @Test
  void fromTwoSameWeightBinaryOperators() {
    ExpressionTree et = new ExpressionTree("98 + 12 - 43", List.of(new NumberOperand()), List.of(new BinaryOperator("+", 10), new BinaryOperator("-", 10)));
    assertEquals("[[[98]+[12]]-[43]]", et.toString());
  }

  @Test
  void fromBinaryOperatorsDifferentWeight() {
    ExpressionTree et = new ExpressionTree("3 + 2 * 4", List.of(new NumberOperand()), List.of(new BinaryOperator("+", 10), new BinaryOperator("*", 20)));
    assertEquals("[[3]+[[2]*[4]]]", et.toString());
  }

  @Test
  void fromBinaryOperatorsSparcedDifferentWeights() {
    ExpressionTree et = new ExpressionTree("3 + 2 * 4 + 8 * 2", List.of(new NumberOperand()), List.of(new BinaryOperator("+", 10), new BinaryOperator("*", 20)));
    assertEquals("[[[3]+[[2]*[4]]]+[[8]*[2]]]", et.toString());
  }

  @Test
  void fromBinaryAndLeftUnaryOperator() {
    ExpressionTree et = new ExpressionTree("2 + -3", List.of(new NumberOperand()), List.of(new BinaryOperator("+", 10), new UnaryOperator("-", 30, Order.LEFT)));
    assertEquals("[[2]+[-[3]]]", et.toString());
  }
  
  @Test
  void fromLeftUnaryAndBinaryOperator() {
    ExpressionTree et = new ExpressionTree("-3 + 2", List.of(new NumberOperand()), List.of(new BinaryOperator("+", 10), new UnaryOperator("-", 30, Order.LEFT)));
    assertEquals("[[-[3]]+[2]]", et.toString());
  }
  
  @Test
  void fromBinaryAndRightUnaryOperator() {
    ExpressionTree et = new ExpressionTree("3 + 5 Is Not Null", List.of(new NumberOperand()), List.of(new BinaryOperator("+", 10), new UnaryOperator("Is Not Null", 30, Order.RIGHT)));
    assertEquals("[[3]+[[5]Is Not Null]]", et.toString());
  }
  
  @Test
  void fromExpressionContainingParentheses() {
    ExpressionTree et = new ExpressionTree("(3 * (5 - 2))", List.of(new NumberOperand()), List.of(new BinaryOperator("*", 20), new BinaryOperator("-", 10)));
    assertEquals("[[3]*[[5]-[2]]]", et.toString());
  }
}
