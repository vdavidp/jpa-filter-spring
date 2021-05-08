package io.dvp.jpa.filter.el;

import java.util.Map;
import java.util.Optional;

public interface Symbol {
  int WEIGHT_MIN = 1;
  int WEIGHT_10 = 10;
  int WEIGHT_20 = 20;
  int WEIGHT_30 = 30;
  int WEIGHT_40 = 40;
  int WEIGHT_50 = 50;
  int WEIGHT_60 = 60;
  int WEIGHT_70 = 70;
  int WEIGHT_80 = 80;
  int WEIGHT_90 = 90;
  int WEIGHT_MAX = 100;


  Symbol merge(Symbol s);

  Optional<Symbol> copy(String exp, Map<ContextItem, Object> context);

  int getWeight();

  void visit(Visitor visitor);
}
