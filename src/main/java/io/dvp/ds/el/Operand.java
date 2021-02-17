package io.dvp.ds.el;

import lombok.Getter;

public abstract class Operand implements Symbol {
    @Getter
    private int weight = 100;

    @Override
    public Symbol merge(Symbol s) {
        return s.merge(this);
    }
}
