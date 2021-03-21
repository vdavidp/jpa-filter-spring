package io.dvp.jpa.filter.el;

import static io.dvp.jpa.filter.el.ContextItem.IDENTITY_MULTIPLIER;

import java.util.EnumMap;

public class Helper {
  public static final EnumMap<ContextItem, Object> DEFAULT_CONTEXT;

  static {
    DEFAULT_CONTEXT = new EnumMap<>(ContextItem.class);
    DEFAULT_CONTEXT.put(IDENTITY_MULTIPLIER, 1);
  }
}
