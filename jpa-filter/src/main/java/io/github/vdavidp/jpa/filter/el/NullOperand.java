/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 * @author david
 */
@NoArgsConstructor
@AllArgsConstructor
public class NullOperand implements Operand {
  protected String value;

  @Override
  public ReducedPair parse(String text, ParenthesesCounter counter) {
    return new ReducedPair(new NullOperand(text), counter);
  }

  @Override
  public void visit(Visitor visitor) {
    throw new UnsupportedOperationException("Null Operand no able to be visit");
  }

  @Override
  public String toString() {
    throw new ParseException("This expression is not recognized: [" + value + "]");
  }
    
}
