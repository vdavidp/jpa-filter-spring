package io.dvp.jpa.filter.el;

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
  public Optional<Symbol> copy(String exp, int multiplier) {
    if (symbol.equalsIgnoreCase(exp.trim())) {
      return Optional.of(new BinaryOperator(this.symbol, this.weight));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void visit(Visitor visitor) {
    left.visit(visitor);
    right.visit(visitor);
    visitor.accept(this);
  }

  @Override
  public String toString() {
    return "[" + left + symbol + right + "]";
  }
}
