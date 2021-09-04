package com.github.vdavidp.jpa.filter.el;

import static com.github.vdavidp.jpa.filter.el.ContextItem.MULTIPLIER;
import static com.github.vdavidp.jpa.filter.el.Helper.cast;

import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BinaryOperator implements Symbol {

  @Getter
  private final String symbol;
  @Getter
  private final int weight;

  private Symbol left, right;

  @Override
  public Symbol merge(Symbol s) {
    if (left == null) {
      left = s;
      return this;
    } else if (right == null) {
      right = s;
      return this;
    } else if (s.getWeight() > weight) {
      right = s.merge(right);
      return this;
    } else {
      s.merge(this);
      return s;
    }
  }

  @Override
  public Optional<Symbol> copy(String exp, Map<ContextItem, Object> context) {
    if (symbol.equalsIgnoreCase(exp.trim())) {
      Integer multiplier = cast(context.get(MULTIPLIER), Integer.class);
      return Optional.of(new BinaryOperator(this.symbol, this.weight + multiplier));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void visit(Visitor visitor) {
    if (left == null || right == null) {
      throw new RuntimeException("Error at parsing binary operator: " + toString());
    }

    left.visit(visitor);
    right.visit(visitor);
    visitor.accept(this);
  }

  @Override
  public String toString() {
    return "[" + left + symbol + right + "]";
  }
}
