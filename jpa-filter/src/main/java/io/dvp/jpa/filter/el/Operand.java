package io.dvp.jpa.filter.el;

import lombok.Getter;

public abstract class Operand implements Symbol {

    @Getter
    private final int weight = 100;

    @Override
    public Symbol merge(Symbol s) {
        return s.merge(this);
    }
}
