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
  class Details implements Builder, Token {
    @Getter
    private final int index;
    private final String left;
    private final String right;
    private BiFunction<String, ParenthesesCounter, ReducedPair> reducer;
    private ParenthesesCounter counter;

    @Override
    public Builder withReducer(BiFunction<String, ParenthesesCounter, ReducedPair> reducer) {
      this.reducer = reducer;
      return this;
    }
    
    @Override
    public Builder withCounter(ParenthesesCounter counter) {
      this.counter = counter;
      return this;
    }

    @Override
    public ReducedPair build() {
      ReducedPair leftResult = reducer.apply(left, counter);
      ReducedPair rightResult = reducer.apply(right, leftResult.getCounter());
      
      Symbol leafSymbol;
      
      if (order == Order.LEFT 
          && (!(leftResult.getSymbol() instanceof NullOperand)
            || rightResult.getSymbol() instanceof NullOperand)) {
          return null;
      }
      
      if (order == Order.RIGHT
          && (leftResult.getSymbol() instanceof NullOperand
            || !(rightResult.getSymbol() instanceof NullOperand))) {
        return null;
      }
      
      leafSymbol = (order == Order.LEFT ? rightResult.getSymbol() : leftResult.getSymbol());
      
      int myWeight = weight + leftResult.getCounter().getCurrentCount();
      
      if (rightResult.getSymbol() instanceof Operator && myWeight >= ((Operator)rightResult.getSymbol()).weight) {
        Operator other = (Operator)rightResult.getSymbol();
        return new ReducedPair(
            other.executeAfter(otherLeaf -> new UnaryOperator(symbol, myWeight, order, otherLeaf)));
      }
      
      return new ReducedPair(
          new UnaryOperator(symbol, weight + leftResult.getCounter().getCurrentCount(), order, leafSymbol));
    }

    @Override
    public Builder builder() {
      return this;
    }
    
  }
}