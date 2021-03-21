package io.dvp.jpa.filter.el;

import java.util.EnumMap;
import java.util.Optional;

public interface Symbol {

  Symbol merge(Symbol s);

  Optional<Symbol> copy(String exp, EnumMap<ContextItem, Object> context);

  int getWeight();

  void visit(Visitor visitor);
}
