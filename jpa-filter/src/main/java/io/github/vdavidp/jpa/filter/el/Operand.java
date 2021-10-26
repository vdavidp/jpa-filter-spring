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
public abstract class Operand implements Symbol {
  
  public abstract ReducedPair parse(String text, ParenthesesCounter counter);
  
  protected String leftSide(String text, Matcher m) {
    return text.substring(0, m.start(1));
  }
  
  protected String rightSide(String text, Matcher m) {
    return text.substring(m.end(1), text.length());
  }
  
  public Symbol merge(Symbol other) {
    return other.merge(this);
  }
  
  public int getWeight() {
    return Integer.MAX_VALUE;
  }
}
