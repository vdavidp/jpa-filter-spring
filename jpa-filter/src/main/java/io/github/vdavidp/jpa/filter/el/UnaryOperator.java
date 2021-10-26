/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author david
 */
public class UnaryOperator extends Operator {

  public enum Order {
    LEFT, RIGHT
  }
  
  private Symbol leaf;
  private Order order;
  
  
  public UnaryOperator(String symbol, int weight, Order order) {
    super(symbol, weight);
    this.order = order;
  }
  
  public UnaryOperator(String symbol, int weight, Order order, Symbol leaf) {
    this(symbol, weight, order);
    this.leaf = leaf;
  }

  @Override
  protected Token createDetails(int index, String left, String right) {
    return new Details(index, left, right);
  }
  
  @Override
  public Symbol merge(Symbol symbol) {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
  @Override
  public String toString() {
    if (order == Order.LEFT)
      return "[" + symbol + leaf + "]";
    else
      return "[" + leaf + symbol + "]";
  }
  
  
  @Override
  protected Operator executeAfter(Function<Symbol, Operator> factory) {
    Operator op = factory.apply(leaf);
    return new UnaryOperator(symbol, weight, order, op);
  }
  
  @Override
  public void visit(Visitor visitor) {
    visitor.accept(this);
  }
  
  @RequiredArgsConstructor
  class Details implements Token {
    @Getter
    private final int index;
    @Getter
    private final String leftText;
    @Getter
    private final String rightText;

    @Override
    public Operator build(ParenthesesCounter counter) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
  }
}