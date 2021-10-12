/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import static java.lang.String.format;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 * @author david
 */
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ParenthesesCounter {
  private int multiplier = 1;
  private int total = 0;
  
  public ParenthesesCounter(int multiplier) {
    this.multiplier = multiplier;
  }
  
  public ParenthesesCounter count(String left, String right) {
    int newTotal = total + count(left, '(') - count(right, ')');
    assertValidCount(newTotal);
    return new ParenthesesCounter(multiplier, newTotal);
  }

  private void assertValidCount(int count) {
    if (count < 0) {
      throw new ParseException("Parentheses don't match");
    }
  }
  
  private int count(String text, char character) {
    int count = 0;
    for(int i=0; i<text.length(); i++) {
      if (text.charAt(i) == ' ') {
        continue;
      }
      
      if (text.charAt(i) == character) {
        count++;
      } else {
        throw new ParseException(format("Invalid character [%s] in subexpression [%s]", text.charAt(i), text));
      }
    }
    return count;
  }
  
  public int getCurrentCount() {
    return total * multiplier;
  }
}
