package io.dvp.jpa.filter.el;

import static io.dvp.jpa.filter.el.ContextItem.MULTIPLIER;
import static io.dvp.jpa.filter.el.Helper.cast;

import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RightUnaryOperator implements Symbol {

  @Getter
  private final String symbol;
  @Getter
  private final int weight;
  private Symbol operand;

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
      return Optional.of(new RightUnaryOperator(this.symbol, weight * multiplier));
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
    return "[" + operand + symbol + "]";
  }
}
