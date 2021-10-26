/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author david
 */
public class BinaryOperatorTest {
  BinaryOperator proto = new BinaryOperator("+", 10);
  
  @Test
  void buildsPrototype() {
    BinaryOperator bo = new BinaryOperator("+", 10);
    assertEquals("[null+null]", bo.toString());
  }
  
  @Test
  void assignLeft() {
    Symbol s = proto.merge(number("10"));
    assertNotSame(s, proto);
    assertEquals("[[10]+null]", s.toString());
  }
  
  @Test
  void assignRight() {
    Symbol s = proto.merge(number("10")).merge(number("20"));
    assertNotSame(s, proto);
    assertEquals("[[10]+[20]]", s.toString());
  }
  
  @Test
  void mergeWithWeighterOperator() {
    Symbol s = proto.merge(number("1")).merge(number("2"));
    Symbol other = s.merge(new BinaryOperator("*", 20));
    assertNotSame(s, other);
    assertEquals("[[1]+[[2]*null]]", other.toString());
  }
  
  @Test
  void mergeWithLighterOperator() {
    Symbol s = proto.merge(number("1")).merge(number("2"));
    Symbol other = s.merge(new BinaryOperator("AND", 5));
    assertNotSame(s, other);
    assertEquals("[[[1]+[2]]ANDnull]", other.toString());
  }
  
  private NumberOperand number(String s) {
    return new NumberOperand(new BigInteger(s));
  }
  
}
