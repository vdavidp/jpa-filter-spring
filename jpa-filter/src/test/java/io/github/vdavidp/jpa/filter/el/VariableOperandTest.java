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
public class VariableOperandTest {
  VariableOperand factory = new VariableOperand();
  ParenthesesCounter counter = new ParenthesesCounter();
  
  void check(String text, String expected, int parenthesesCount) {
    ReducedPair result = factory.parse(text, counter);
    assertNotSame(result.getSymbol(), factory);
    assertEquals(expected, result.getSymbol().toString());
    assertEquals(parenthesesCount, result.getCounter().getCurrentCount());
  }
  
  @Test
  void singleWithParentheses() {
    check("( (abc4 )", "[abc4]", 1);
  }
  
  @Test
  void singleWithoutParentheses() {
    check("abc1", "[abc1]", 0);
  }
  
  @Test
  void nestedWithParentheses() {
    check("( ( abc.d3ef )", "[abc.d3ef]", 1);
  }
  
  @Test
  void nestedWithoutParentheses() {
    check("ab1c.def", "[ab1c.def]", 0);
  }
  
  @Test
  void invalid() {
    assertNull(factory.parse("abc.def.ghi", counter));
    assertNull(factory.parse("123zds", counter));
    assertNull(factory.parse("abc.", counter));
    assertNull(factory.parse("ab,dde", counter));
  }
}
