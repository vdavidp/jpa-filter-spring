/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import io.github.vdavidp.jpa.filter.el.UnaryOperator.Order;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author david
 */
public class UnaryOperatorTest {
  UnaryOperator protoLeft = new UnaryOperator("-", 30, Order.LEFT);
  UnaryOperator protoRight = new UnaryOperator("Is Null", 30, Order.RIGHT);
  
  @Test
  void mergeWithOpenParenthesesOperand_left() {
    Symbol s = protoLeft.merge(new OpenParenthesesOperand());
    assertEquals("[-null]", s.toString());
  }
  
  @Test
  void mergeWithEmptyOperand_left() {
    Symbol s = protoLeft.merge(new EmptyOperand());
    assertEquals("[-null]", s.toString());
  }
  
  @Test
  void mergeWithOperand_left() {
    Symbol s = protoLeft.merge(number("2"));
    assertNotSame(s, protoLeft);
    assertEquals("[-[2]]", s.toString());
  }
  
  @Test
  void mergeWithLighterOperator_left() {
    Symbol s = protoLeft.merge(number("2")).merge(new BinaryOperator("+", 20));
    assertNotSame(s, protoLeft);
    assertEquals("[[-[2]]+null]", s.toString());
  }
  
  @Test
  void mergeWithWeighterOperator_left() {
    Symbol s = protoLeft.merge(number("2")).merge(new BinaryOperator("*", 100));
    assertEquals("[-[[2]*null]]", s.toString());
  }
  
  @Test
  void mergeWithAnotherUnaryOperator_left() {
    Symbol s = protoLeft.merge(new UnaryOperator("+", 30, Order.LEFT)).merge(number("3"));
    assertEquals("[-[+[3]]]", s.toString());
  }
  
  @Test
  void mergeWithOperand_right() {
    Symbol s = number("2").merge(protoRight);
    assertNotSame(s, protoRight);
    assertEquals("[[2]Is Null]", s.toString());
  }
  
  private Operand number(String text) {
    return new NumberOperand(new BigInteger(text));
  }
  
  @Test
  void tokenAcceptsEmptyOrParenthesesOperand_left() {
    Token t = protoLeft.nextToken("-3", 0);
    assertTrue(t.isValidOpening(new OpenParenthesesOperand()));
    assertTrue(t.isValidOpening(new EmptyOperand()));
  }
  
  @Test
  void mergeWithEmptyOperand_right() {
    Symbol s = protoRight.merge(number("2")).merge(new EmptyOperand());
    assertEquals("[[2]Is Null]", s.toString());
  }
  
  @Test
  void mergeWithCloseParenthesesOperand_right() {
    Symbol s = protoRight.merge(number("2")).merge(new CloseParenthesesOperand());
    assertEquals("[[2]Is Null]", s.toString());
  }
  
  @Test
  void tokenAcceptsEmptyOrParenthesesOperand_right() {
    Token t = protoRight.nextToken("3 Is Null", 0);
    assertTrue(t.isValidClosing(new EmptyOperand()));
    assertTrue(t.isValidClosing(new CloseParenthesesOperand()));
  }
}
