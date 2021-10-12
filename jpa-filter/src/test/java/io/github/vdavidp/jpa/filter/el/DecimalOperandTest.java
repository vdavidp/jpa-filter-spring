/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author david
 */
public class DecimalOperandTest {
  DecimalOperand factory = new DecimalOperand();
  ParenthesesCounter counter = new ParenthesesCounter();
  
  @Test
  void withoutParentheses() {
    ReducedPair result = factory.parse("4993.223", counter);
    assertNotSame(result.getSymbol(), factory);
    assertEquals("[4993.223]", result.getSymbol().toString());
    assertEquals(0, result.getCounter().getCurrentCount());
  }
  
  @Test
  void invalidCases() {
    assertNull(factory.parse("334.", counter));
    assertNull(factory.parse(".449", counter));
    assertNull(factory.parse("94948", counter));
  }
  
  @Test
  void withParentheses() {
    ReducedPair result = factory.parse("( ( 94.33)", counter);
    assertNotSame(result.getSymbol(), factory);
    assertEquals("[94.33]", result.getSymbol().toString());
    assertEquals(1, result.getCounter().getCurrentCount());
  }
}
