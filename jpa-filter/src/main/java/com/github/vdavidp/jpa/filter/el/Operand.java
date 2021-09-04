package com.github.vdavidp.jpa.filter.el;

import lombok.Getter;

public abstract class Operand implements Symbol {

  @Getter
  private final int weight = Integer.MAX_VALUE;

  @Override
  public Symbol merge(Symbol s) {
    return s.merge(this);
  }
}
