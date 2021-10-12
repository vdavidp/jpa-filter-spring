/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author david
 */
@NoArgsConstructor
@AllArgsConstructor
public class StringOperand implements Operand {
  private static final Pattern pattern = Pattern.compile("^[\\(\\)\\s]*('(?:[^\\\\']|\\\\'|\\\\\\\\)*')[\\(\\)\\s]*$");
  
  @Getter
  private String value;

  @Override
  public ReducedPair parse(String text, ParenthesesCounter counter) {
    Matcher m = pattern.matcher(text);
    if (m.find()) {
      String value = m.group(1).substring(1, m.group(1).length() - 1).replace("\\'", "'").replace("\\\\", "\\");
      counter = counter.count(leftSide(text, m), rightSide(text, m));
      return new ReducedPair(new StringOperand(value), counter);
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
