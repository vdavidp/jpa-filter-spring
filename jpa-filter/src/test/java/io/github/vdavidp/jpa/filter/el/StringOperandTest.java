/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author david
 */
public class StringOperandTest {
  StringOperand factory = new StringOperand();
  ParenthesesCounter counter = new ParenthesesCounter();
  
  @Test
  void withoutParentheses() {
    ReducedPair result = factory.parse("'some text'", counter);
    assertNotSame(result.getSymbol(), factory);
    assertEquals("[some text]", result.getSymbol().toString());
    assertEquals(0, result.getCounter().getCurrentCount());
  }
  
  @Test
  void invalidCases() {
    assertNull(factory.parse("'Some text", counter));
    assertNull(factory.parse("some'", counter));
    assertNull(factory.parse("without single quotes", counter));
    assertNull(factory.parse("234", counter));
  }
  
  @Test
  void validCases() {
    assertEquals("[inside single quotes]", factory.parse("'inside single quotes'", counter).getSymbol().toString());
    assertEquals("[]", factory.parse("''", counter).getSymbol().toString());
    assertEquals("[That's]", factory.parse("'That\\'s'", counter).getSymbol().toString());
    assertEquals("[ooo''s]", factory.parse("'ooo\\'\\'s'", counter).getSymbol().toString());
    assertEquals("[\\]", factory.parse("'\\\\'", counter).getSymbol().toString());
  }
  
  @Test
  void withPrentheses() {
    ReducedPair result = factory.parse(" ( ( 'ii' )", counter);
    assertNotSame(result.getSymbol(), factory);
    assertEquals("[ii]", result.getSymbol().toString());
    assertEquals(1, result.getCounter().getCurrentCount());
  }
}
