package io.dvp.ds;

public abstract class BinaryOperator implements Symbol {
    protected Symbol left, right;

    @Override
    public boolean matches(String symbol) {
        return getSymbol().equals(symbol);
    }

    protected abstract String getSymbol();

    @Override
    public Symbol merge(Symbol s) {
        if (left == null) {
            left = s;
            return this;
        } else if (right == null) {
            right = s;
            return this;
        } else {
            s.merge(this);
            return s;
        }
    }

    @Override
    public String toString() {
        return "[" + left + getSymbol() + right + "]";
    }
}
