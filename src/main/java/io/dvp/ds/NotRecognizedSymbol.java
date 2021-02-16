package io.dvp.ds;

import java.util.Optional;

public class NotRecognizedSymbol implements Symbol {

    @Override
    public Symbol merge(Symbol s) {
        return null;
    }

    @Override
    public Optional<Symbol> copy(String symbol) {
        return Optional.empty();
    }

    @Override
    public int getWeight() {
        return 0;
    }
}
