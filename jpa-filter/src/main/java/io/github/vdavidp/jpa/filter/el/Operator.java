/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import java.util.function.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author david
 */
@RequiredArgsConstructor
public abstract class Operator implements Symbol {
  @Getter
  protected final String symbol;
  @Getter
  protected final int weight;
  
  public Token nextToken(String text) {
    return nextToken(text, 0);
  }
  
  public Token nextToken(String text, int startIndex) {
    int i = text.indexOf(symbol, startIndex);
    if (i > -1) {
      return createDetails(i, text.substring(0, i), text.substring(i + symbol.length()));
    } else {
      return null;
    }
  }
  
  protected abstract Token createDetails(int index, String left, String right);
  
  protected abstract Operator executeAfter(Function<Symbol, Operator> factory);
}
