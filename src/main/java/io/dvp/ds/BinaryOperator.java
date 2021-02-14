package io.dvp.ds;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BinaryOperator implements Symbol {
    private final String symbol;
    private Symbol left, right;

    @Override
    public boolean matches(String exp) {
        return symbol.equals(exp);
    }

    @Override
    public Symbol merge(Symbol s) {
        if (left == null) {
            left = s;
            return this;
        } if (right == null) {
            right = s;
            return this;
        } else {
            s.merge(this);
            return s;
        }
    }

    @Override
    public Symbol copy(String symbol) {
        return new BinaryOperator(this.symbol);
    }

    @Override
    public String toString() {
        return "[" + left + symbol + right + "]";
    }
}
