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
import org.junit.jupiter.api.Test;

/**
 *
 * @author david
 */
public class BinaryOperatorTest {
  
  @Test
  void nextToken() {
    BinaryOperator factory = new BinaryOperator("+", 0);
    Token td = factory.nextToken("3 + 2");
    assertEquals(2, td.getIndex());
    
    ReducedPair result = td.builder()
        .withReducer((t, c) -> {
          if (t.equals("3 ")) {
            return new ReducedPair(number("3"), new ParenthesesCounter());
          } else {
            return new ReducedPair(number("2"), new ParenthesesCounter());
          }
        })
        .withCounter(new ParenthesesCounter())
        .build();
    
    assertNotSame(factory, result.getSymbol());
    assertEquals("[[3]+[2]]", result.getSymbol().toString());
  }
  
  @Test
  void incresesWeight() {
    BinaryOperator factory = new BinaryOperator("*", 20);
    Token td = factory.nextToken("(5 * 2)");
    ReducedPair result = td.builder()
        .withReducer((t, c) -> {
          if (t.equals("(5 ")) {
          return new ReducedPair(number("5"), new ParenthesesCounter(100, 1));
          } else {
            return new ReducedPair(number("2"), new ParenthesesCounter());
          }
        })
        .withCounter(new ParenthesesCounter())
        .build();
    
    assertEquals(120, ((BinaryOperator)result.getSymbol()).weight);
  }
  
  @Test
  void joinWithWeighterOperator() {
    BinaryOperator factory = new BinaryOperator("*", 20);
    Token td = factory.nextToken("3 * (5"); // - 2)
    
    ReducedPair result = td.builder()
        .withReducer((t, c) -> {
          if (t.equals("3 ")) {
            return new ReducedPair(number("3"), new ParenthesesCounter(100, 0));
          } else {
            return new ReducedPair(
                new BinaryOperator("-", 110, number("5"), number("2")), new ParenthesesCounter());
          }
        })
        .withCounter(new ParenthesesCounter())
        .build();
    
    assertEquals("[[3]*[[5]-[2]]]", result.getSymbol().toString());
  }
  
  private NumberOperand number(String s) {
    return new NumberOperand(new BigInteger(s));
  }
  
  @Test
  void nullWhenLeftSideNotParsable() {
    BinaryOperator factory = new BinaryOperator("+", 10);
    Token td = factory.nextToken("3 + 2");
    
    ReducedPair result = td.builder()
        .withReducer((t, c) -> {
          if (t.equals("3 ")) {
            return new ReducedPair(new NullOperand("3'"), new ParenthesesCounter());
          } else {
            return new ReducedPair(number("2"), new ParenthesesCounter());
          }
        })
        .withCounter(new ParenthesesCounter())
        .build();
    
    assertNull(result);
  }
  
  @Test
  void nullWhenRightSideNotParsable() {
    BinaryOperator factory = new BinaryOperator("+", 10);
    Token td = factory.nextToken("3 + 2");
    
    ReducedPair result = td.builder()
        .withReducer((t, c) -> {
          if (t.equals("3 ")) {
            return new ReducedPair(number("3"), new ParenthesesCounter());
          } else {
            return new ReducedPair(new NullOperand("2"), new ParenthesesCounter());
          }
        })
        .withCounter(new ParenthesesCounter())
        .build();
    
    assertNull(result);
  }
}
