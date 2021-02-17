package io.dvp.ds.el;

import java.util.Optional;

public interface Symbol {

    Symbol merge(Symbol s);

    Optional<Symbol> copy(String exp);

    int getWeight();
}
