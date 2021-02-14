package io.dvp.ds;

public interface Symbol {
    boolean matches(String symbol);
    Symbol merge(Symbol s);
    Symbol copy(String symbol);
}
