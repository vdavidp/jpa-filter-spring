/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author david
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class NumberOperand implements Operand {
  private static final Pattern pattern = Pattern.compile("^[\\(\\s]*([0-9]+)[\\)\\s]*$");
  
  @Getter
  private BigInteger value;
  
  public ReducedPair parse(String text, ParenthesesCounter counter) {
    Matcher m = pattern.matcher(text);
    if (m.find()) {
      counter = counter.count(text.substring(0, m.start(1)), text.substring(m.end(1), text.length()));
      return new ReducedPair(new NumberOperand(new BigInteger(m.group(1))), counter);
    } else {
      return null;
    }
  }
  
  @Override
  public void visit(Visitor visitor) {
    visitor.accept(this);
  }
  
  @Override
  public String toString() {
    return "[" + value + "]";
  }
}
