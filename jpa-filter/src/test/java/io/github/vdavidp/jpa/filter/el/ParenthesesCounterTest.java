/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 *
 * @author david
 */
public class ParenthesesCounterTest {
  ParenthesesCounter counter = new ParenthesesCounter();
  
  @Test
  void count() {
    ParenthesesCounter counter2 = counter.count(" (((", ") )");
    assertNotSame(counter2, counter);
    assertEquals(0, counter.getCurrentCount());
    assertEquals(1, counter2.getCurrentCount());
    
    ParenthesesCounter counter3 = counter2.count("", ")");
    assertEquals(0, counter3.getCurrentCount());
  }
  
  @Test
  void exceptionWhenNegativeCount() {
    Throwable t2 = assertThrows(ParseException.class, () -> counter.count("(", "))"));
    assertEquals("Parentheses don't match", t2.getMessage());
  }
  
  @Test
  void countWithMultiplier() {
    counter = new ParenthesesCounter(100);
    counter = counter.count("((((", "");
    assertEquals(400, counter.getCurrentCount());
  }
}
