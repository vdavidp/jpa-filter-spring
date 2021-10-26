/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author david
 */
public class ExpressionTree {
  private final List<Operand> operands;
  private final List<Operator> operators;
  private Symbol root;
  
  public ExpressionTree(String text, List<Operand> operands, List<Operator> operators) {
    this.operands = new ArrayList<>(operands);
    this.operands.add(new NullOperand());
    
    this.operators = operators;
    this.root = parse(new NullSymbol(), text, 0, new ParenthesesCounter());
  }
  
  @Override
  public String toString() {
    return root.toString();
  }
  
  private Symbol parse(Symbol tree, String text, int startIndex, ParenthesesCounter counter) {
    Token t = findToken(text, startIndex);
    if (t == null) {
      Symbol op = findOperand(text, counter).getSymbol();
      if (op instanceof NullOperand) {
        throw new ParseException("Not able to parse expression: [" + text + "]");
      }
      return tree.merge(op);
    } else {
      ReducedPair pair = findOperand(t.getLeftText(), counter);
      if (pair.getSymbol() instanceof NullOperand) {
        return parse(tree, text, t.getIndex() + 1, counter);
      } else {
        tree = tree.merge(pair.getSymbol());
        tree = tree.merge(t.build(pair.getCounter()));
        return parse(tree, t.getRightText(), 0, pair.getCounter());
      }
    }
  }
  
  private Token findToken(String text, int startIndex) {
    return operators.stream()
          .map(op -> op.nextToken(text, startIndex))
          .filter(Objects::nonNull)
          .min(Comparator.comparing(Token::getIndex))
          .orElse(null);
  }
  
  private ReducedPair findOperand(String text, ParenthesesCounter counter) {
    return operands.stream()
        .map(o -> o.parse(text, counter))
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }
  
  public void visit(Visitor visitor) {
    root.visit(visitor);
  }
  
  class NullSymbol implements Symbol {
    
    @Override
    public String toString() {
      return "[]";
    }

    @Override
    public void visit(Visitor visitor) {
      throw new UnsupportedOperationException("Null Symbol not able to be visited");
    }

    @Override
    public Symbol merge(Symbol symbol) {
      return symbol;
    }

    @Override
    public int getWeight() {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}
