/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

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
  public Symbol merge(Symbol other) {
    if (other instanceof EmptyOperand) {
      return this;
    }
    
    if (order == Order.LEFT && other instanceof OpenParenthesesOperand) {
      return this;
    }
    
    if (order == Order.RIGHT && other instanceof CloseParenthesesOperand) {
      return this;
    }
    
    if (leaf == null) {
      return new UnaryOperator(symbol, weight, order, other);
    } else if (other.getWeight() > weight) {
      return new UnaryOperator(symbol, weight, order, leaf.merge(other));
    } else {
      return ((Operator)other).executeAfter(this);
    }
  }
  
  @Override
  protected Symbol executeAfter(Symbol other) {
    return new UnaryOperator(symbol, weight, order, other);
  }
  
  @Override
  public String toString() {
    if (order == Order.LEFT)
      return "[" + symbol + leaf + "]";
    else
      return "[" + leaf + symbol + "]";
  }
  
  @Override
  public void visit(Visitor visitor) {
    leaf.visit(visitor);
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
      return new UnaryOperator(symbol, weight + counter.getCurrentCount(), order);
    }
    
    @Override
    public boolean isValidOpening(Symbol s) {
      if (order == Order.LEFT) {
        return (s instanceof OpenParenthesesOperand) || (s instanceof EmptyOperand);
      } else {
        return !(s instanceof NullOperand);
      }
    }
    
    @Override
    public boolean isValidClosing(Symbol s) {
      if (order == Order.LEFT) {
        return !(s instanceof NullOperand);
      } else {
        return (s instanceof CloseParenthesesOperand) || (s instanceof EmptyOperand);
      }
    }
    
  }
}