/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.   
 */
package io.github.vdavidp.jpa.filter.el;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author david
 */
public class NumberOperandTest {
  NumberOperand factory = new NumberOperand();
  
  @Test
  void parseWithoutParentheses() {
    ReducedPair no = factory.parse("12", new ParenthesesCounter());
    assertNotEquals(no.getSymbol(), factory);
    assertEquals("[12]", no.getSymbol().toString());
    assertEquals(0, no.getCounter().getCurrentCount());
  }
  
  @Test
  void parseWithParenthesesAndSpaces() {
    ReducedPair no = factory.parse("(( 12)", new ParenthesesCounter());
    assertNotEquals(no.getSymbol(), factory);
    assertEquals("[12]", no.getSymbol().toString());
    assertEquals(1, no.getCounter().getCurrentCount());
  }
}
