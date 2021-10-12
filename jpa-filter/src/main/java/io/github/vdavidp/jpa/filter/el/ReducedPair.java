/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author david
 */
@AllArgsConstructor
@Getter
public class ReducedPair {
  private Symbol symbol;
  private ParenthesesCounter counter;
  
  public ReducedPair(Symbol symbol) {
    this.symbol = symbol;
    this.counter = null;
  }
}
