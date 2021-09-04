package com.github.vdavidp.jpa.filter.el;

import static java.lang.String.format;

public class Helper {

  public static <T> T cast(Object o, Class<T> clazz) {
    if (clazz.isInstance(o)) {
      return clazz.cast(o);
    } else {
      throw new RuntimeException(format("Object {%s} is not of type %s", o, clazz));
    }
  }
}
