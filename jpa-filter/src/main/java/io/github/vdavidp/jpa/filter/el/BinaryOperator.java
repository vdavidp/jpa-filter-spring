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
public class BinaryOperator extends  Operator {
  protected Symbol left;
  protected Symbol right;
  
  public BinaryOperator(String symbol, int weight) {
    super(symbol, weight);
  }
  
  protected BinaryOperator(String symbol, int weight, Symbol left, Symbol right) {
    super(symbol, weight);
    this.left = left;
    this.right = right;
  }
  
  public Symbol merge(Symbol other) {
    if (left == null) {
      return new BinaryOperator(symbol, weight, other, null);
    } else if (right == null) {
      return new BinaryOperator(symbol, weight, left, other);
    } else if (other.getWeight() > weight) {
      return new BinaryOperator(symbol, weight, left, right.merge(other));
    } else {
      Operator op = (Operator)other;
      return new BinaryOperator(op.symbol, op.weight, this, null);
    }
  }
  
  @Override
  public Token createDetails(int index, String left, String right) {
    return new Details(index, left, right);
  }
  
  @Override
  public String toString() {
    return "[" + left + symbol + right + "]";
  }

  @Override
  protected Operator executeAfter(Function<Symbol, Operator> factory) {
    Operator newLeft = factory.apply(left);
    return new BinaryOperator(symbol, weight, newLeft, right);
  }
  
  @Override
  public void visit(Visitor visitor) {
    left.visit(visitor);
    right.visit(visitor);
    visitor.accept(this);
  }
  
  @RequiredArgsConstructor
  class Details implements Token, Builder {
    @Getter
    private final int index;
    @Getter
    private final String leftText;
    @Getter
    private final String rightText;
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
    public Operator build(ParenthesesCounter counter) {
      return new BinaryOperator(symbol, counter.getCurrentCount() * 100 + weight);
    }

    @Override
    public ReducedPair build() {
      ReducedPair leftResult = reducer.apply(leftText, counter);
      ReducedPair rightResult = reducer.apply(rightText, leftResult.getCounter());
      
      Symbol leftSymbol = leftResult.getSymbol();
      Symbol rightSymbol = rightResult.getSymbol();
      int myWeight = weight + leftResult.getCounter().getCurrentCount();
      
      if (leftSymbol instanceof NullOperand || rightSymbol instanceof NullOperand) {
        return null;
      }
      
      if (rightSymbol instanceof Operator && myWeight >= ((Operator)rightSymbol).weight) {
        Operator other = (Operator)rightSymbol;
        return new ReducedPair(
            other.executeAfter(otherLeft -> new BinaryOperator(symbol, myWeight, leftSymbol, otherLeft)),
            rightResult.getCounter());
      }
    
      return new ReducedPair(
          new BinaryOperator(symbol, myWeight, leftSymbol, rightSymbol), rightResult.getCounter());
    }
    
    @Override
    public Builder builder() {
      return this;
    }
  }  
  
}
