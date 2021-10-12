/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import java.util.regex.Matcher;

/**
 *
 * @author david
 */
public interface Operand extends Symbol {
  
  ReducedPair parse(String text, ParenthesesCounter counter);
  
  default String leftSide(String text, Matcher m) {
    return text.substring(0, m.start(1));
  }
  
  default String rightSide(String text, Matcher m) {
    return text.substring(m.end(1), text.length());
  }
}
