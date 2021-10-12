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
import java.util.Optional;

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
    this.root = parseExpression(text);
  }
  
  private Symbol parseExpression(String text) {
    if (text.trim().equals("")) {
      return new NullSymbol();
    } else {
      return reduce(text, new ParenthesesCounter(100)).getSymbol();
    }
  }
  
  @Override
  public String toString() {
    return root.toString();
  }
  
  private ReducedPair reduce(String text, ParenthesesCounter counter) {
    Optional<TokenDetails> token = operators.stream()
          .map(op -> op.nextToken(text))
          .filter(Objects::nonNull)
          .min(Comparator.comparing(TokenDetails::getIndex));
      
    if (token.isPresent()) {
      TokenDetails td = token.get();
      return td.builder()
          .withReducer(this::reduce)
          .withCounter(counter)
          .build();
    } else {
      return operands.stream()
          .map(o -> o.parse(text, counter))
          .filter(Objects::nonNull)
          .findFirst()
          .orElse(null);
    }
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
  }
}
