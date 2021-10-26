/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import java.math.BigDecimal;
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
public class DecimalOperand extends Operand {
  private static final Pattern pattern = Pattern.compile("^[\\(\\s]*([0-9]+\\.[0-9]+)[\\)\\s]*$");
  
  @Getter
  private BigDecimal value;

  @Override
  public ReducedPair parse(String text, ParenthesesCounter counter) {
    Matcher m = pattern.matcher(text);
    if (m.find()) {
      counter = counter.count(leftSide(text, m), rightSide(text, m));
      return new ReducedPair(new DecimalOperand(new BigDecimal(m.group(1))), counter);
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
