/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

/**
 *
 * @author david
 */
public interface Token {
  int getIndex();
  String getLeftText();
  String getRightText();
  Operator build(ParenthesesCounter counter);
  boolean isValidOpening(Symbol s);
  boolean isValidClosing(Symbol s);
}
