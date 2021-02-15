package io.dvp.ds;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class BinaryOperator implements Symbol {
    private final String symbol;
    @Getter
    private final int weight;

    private Symbol left, right;

    @Override
    public Symbol merge(Symbol s) {
        if (left == null) {
            left = s;
            return this;
        } if (right == null) {
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
    public Optional<Symbol> copy(String exp) {
        if (symbol.equals(exp)) {
            return Optional.of(new BinaryOperator(this.symbol, this.weight));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "[" + left + symbol + right + "]";
    }
}
