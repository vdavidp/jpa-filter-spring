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
    this.operands.add(new EmptyOperand());
    this.operands.add(new CloseParenthesesOperand());
    this.operands.add(new OpenParenthesesOperand());
    this.operands.add(new NullOperand());
    
    this.operators = operators;
    this.root = parse(new DummyToken(), new NullSymbol(), text, 0, new ParenthesesCounter(100));
  }
  
  @Override
  public String toString() {
    return root.toString();
  }
  
  private Symbol parse(Token previousToken, Symbol tree, String text, int startIndex, ParenthesesCounter counter) {
    Token token = findToken(text, startIndex);
    if (token == null) {
      Symbol op = findOperand(text, counter).getSymbol();
      if (!previousToken.isValidClosing(op)) {
        throw new ParseException("Not able to parse expression: [" + text + "]");
      }
      return tree.merge(op);
    } else {
      ReducedPair pair = findOperand(token.getLeftText(), counter);
      if (!previousToken.isValidClosing(pair.getSymbol()) && !token.isValidOpening(pair.getSymbol())) {
        return parse(token, tree, text, token.getIndex() + 1, counter);
      } else {
        tree = tree.merge(pair.getSymbol());
        tree = tree.merge(token.build(pair.getCounter()));
        return parse(token, tree, token.getRightText(), 0, pair.getCounter());
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
  
  static class NullSymbol implements Symbol {
    
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
  
  static class DummyToken implements Token {

    @Override
    public int getIndex() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLeftText() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRightText() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Operator build(ParenthesesCounter counter) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isValidOpening(Symbol s) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isValidClosing(Symbol s) {
      return !(s instanceof NullOperand);
    }
    
  }
}
