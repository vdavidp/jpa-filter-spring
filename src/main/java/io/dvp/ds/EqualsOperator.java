package io.dvp.ds;

public class EqualsOperator extends BinaryOperator {
    @Override
    protected String getSymbol() {
        return ":";
    }

    @Override
    public Symbol copy(String symbol) {
        return new EqualsOperator();
    }
}
