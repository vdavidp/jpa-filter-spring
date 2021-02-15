package io.dvp.ds;

import lombok.Getter;

public abstract class NumberOperand implements Symbol {
    @Getter
    private int weight = 100;

    @Override
    public Symbol merge(Symbol s) {
        return s.merge(this);
    }
}
