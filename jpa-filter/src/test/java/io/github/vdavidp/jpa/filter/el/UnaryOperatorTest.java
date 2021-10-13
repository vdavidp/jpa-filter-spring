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
import org.junit.jupiter.api.Test;

/**
 *
 * @author david
 */
public class UnaryOperatorTest {
  
  @Test
  void nextTokenForLeft() {
    UnaryOperator factory = new UnaryOperator("-", 10, Order.LEFT);
    TokenDetails td = factory.nextToken("-1");
    assertEquals(0, td.getIndex());
    
    ReducedPair result = td.builder()
        .withReducer((t, c) -> {
          if (t.equals("")) {
            return new ReducedPair(null, new ParenthesesCounter());
          } else {
            return new ReducedPair(number("1"));
          }
        })
        .withCounter(new ParenthesesCounter())
        .build();
    
    assertNotSame(result.getSymbol(), factory);
    assertEquals("[-[1]]", result.getSymbol().toString());
  }
  
  @Test
  void nextTokenForRight() {
    UnaryOperator factory = new UnaryOperator("Is Number", 10, Order.RIGHT);
    TokenDetails td = factory.nextToken("23 Is Number");
    assertEquals(3, td.getIndex());
    
    ReducedPair result = td.builder()
        .withReducer((t, c) -> {
          if (t.equals("23 ")) {
            return new ReducedPair(number("23"), new ParenthesesCounter());
          } else {
            return new ReducedPair(null);
          }
        })
        .withCounter(new ParenthesesCounter())
        .build();
    
    assertNotSame(result.getSymbol(), factory);
    assertEquals("[[23]Is Number]", result.getSymbol().toString());
  }
  
  @Test
  void leftUnaryWithLighterOperator() {
    UnaryOperator factory = new UnaryOperator("-", 20, Order.LEFT);
    TokenDetails td = factory.nextToken("-3 + 2");
    ReducedPair result = td.builder()
        .withReducer((t, c) -> {
          if (t.equals("")) {
            return new ReducedPair(null, new ParenthesesCounter());
          } else {
            return new ReducedPair(new BinaryOperator("+", 10, number("3"), number("2")));
          }
        })
        .withCounter(new ParenthesesCounter())
        .build();
    
    assertEquals("[[-[3]]+[2]]", result.getSymbol().toString());
  }
  
  private Operand number(String text) {
    return new NumberOperand(new BigInteger(text));
  }
}