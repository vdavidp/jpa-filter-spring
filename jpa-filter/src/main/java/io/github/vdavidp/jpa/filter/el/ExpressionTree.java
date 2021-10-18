/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import static java.lang.String.format;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import static java.util.stream.Collectors.toCollection;

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
    
    if (this.root instanceof NullOperand) {
      throw new ParseException(format("Not able to parse expression: [%s]", ((NullOperand)this.root).value));
    }
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
    ReducedPair result = findOperator(text, 0, counter);
    if (result != null) {  
      return result;
    } else {
      return operands.stream()
        .map(o -> o.parse(text, counter))
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
    }
  }
  
  private ReducedPair findOperator(String text, int startIndex, ParenthesesCounter counter) {
    Optional<Token> tokenOp = operators.stream()
          .map(op -> op.nextToken(text, startIndex))
          .filter(Objects::nonNull)
          .min(Comparator.comparing(Token::getIndex));
    
    if (!tokenOp.isPresent()) {
      return null;
    }
    
    ReducedPair result = build(tokenOp.get(), counter);
    if (result != null) {
      return result;
    } else if (tokenOp.get().getIndex() + 1 < text.length()) {
      return findOperator(text, tokenOp.get().getIndex() + 1, counter);
    } else {
      return null;
    }
  }
  
  private ReducedPair build(Token token, ParenthesesCounter counter) {
    return token.builder()
          .withReducer(this::reduce)
          .withCounter(counter)
          .build();
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
