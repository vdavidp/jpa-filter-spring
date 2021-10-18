package io.github.vdavidp.jpa.filter.el;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author david
 */
public class NullOperandTest {
  NullOperand factory = new NullOperand();
  ParenthesesCounter counter = new ParenthesesCounter();
  
  @Test
  void parsesAnything() {
    assertNotNull(factory.parse("", counter).getSymbol());
    assertNotNull(factory.parse("kid-,^+¬", counter).getSymbol());
  }
  
  @Test
  void noParenthesesCount() {
    ReducedPair result = factory.parse("(3", counter);
    assertEquals(0, result.getCounter().getCurrentCount());
  }
  
  @Test
  void toStringMethod() {
    ReducedPair result = factory.parse("kdi{}+|¬", counter);
    Throwable ex = assertThrows(ParseException.class, () -> result.getSymbol().toString());
    assertEquals("This expression is not recognized: [kdi{}+|¬]", ex.getMessage());
  }
}
