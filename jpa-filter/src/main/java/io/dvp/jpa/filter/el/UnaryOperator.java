/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.dvp.jpa.filter.el;

import static io.dvp.jpa.filter.el.ContextItem.MULTIPLIER;
import static io.dvp.jpa.filter.el.Helper.cast;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnaryOperator implements Symbol {
  
  @Getter
  private final String symbol;
  @Getter
  private final int weight;
  private final Order order;
  
  private Symbol operand;
  
  public enum Order {
    LEFT, RIGHT
  }

  @Override
  public Symbol merge(Symbol s) {
    if (operand == null) {
      operand = s;
      return this;
    } else if (s.getWeight() >= this.weight) {
      return s.merge(this);
    } else {
      throw new RuntimeException("Invalid expression");
    }
  }

  @Override
  public Optional<Symbol> copy(String exp, Map<ContextItem, Object> context) {
    if (this.symbol.equalsIgnoreCase(exp.trim())) {
      Integer multiplier = cast(context.get(MULTIPLIER), Integer.class);
      return Optional.of(new UnaryOperator(this.symbol, weight * multiplier, order));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void visit(Visitor visitor) {
    operand.visit(visitor);
    visitor.accept(this);
  }

  @Override
  public String toString() {
    if (order == Order.LEFT) {
      return "[" + symbol + operand + "]";
    } else {
      return "[" + operand + symbol + "]";
    }
  }
}
