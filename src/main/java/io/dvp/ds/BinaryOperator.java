package io.dvp.ds;

public abstract class BinaryOperator implements Symbol {
    private Symbol left, right;

    @Override
    public boolean matches(String symbol) {
        return getSymbol().equals(symbol);
    }

    protected abstract String getSymbol();

    @Override
    public Symbol merge(Symbol s) {
        if (left == null) {
            left = s;
        } else if (right == null) {
            right = s;
        }
        return this;
    }

    @Override
    public Symbol copy(String symbol) {
        return this;
    }

    @Override
    public String toString() {
        return "[" + left + getSymbol() + right + "]";
    }
}
