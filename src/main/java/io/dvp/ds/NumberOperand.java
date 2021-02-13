package io.dvp.ds;

public class NumberOperand implements Symbol {
    private Integer number;

    @Override
    public boolean matches(String symbol) {
        return symbol.matches("[1234567890]+");
    }

    @Override
    public Symbol merge(Symbol s) {
        if (s instanceof BinaryOperator) {
            s.merge(this);
            return s;
        }

        throw new RuntimeException("Only operator is allowed [" + s.toString() + "]");
    }

    @Override
    public Symbol copy(String symbol) {
        NumberOperand op = new NumberOperand();
        op.number = Integer.parseInt(symbol);
        return op;
    }

    @Override
    public String toString() {
        return "[" + number + "]";
    }
}
