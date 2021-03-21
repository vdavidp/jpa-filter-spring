package io.dvp.jpa.filter.el;

import static io.dvp.jpa.filter.el.Helper.DEFAULT_CONTEXT;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DecimalFactoryTest {

  DecimalFactory op = new DecimalFactory();

  @Test
  void copy() {
    assertNotSame(op, op.copy("23.32", DEFAULT_CONTEXT).get());
    assertTrue(op.copy("34.0", DEFAULT_CONTEXT).isPresent());
    assertTrue(op.copy("0.33", DEFAULT_CONTEXT).isPresent());

    assertFalse(op.copy("34", DEFAULT_CONTEXT).isPresent());
    assertFalse(op.copy(".32", DEFAULT_CONTEXT).isPresent());
    assertFalse(op.copy("34.", DEFAULT_CONTEXT).isPresent());
    assertFalse(op.copy("2.2.2", DEFAULT_CONTEXT).isPresent());
    assertFalse(op.copy("a", DEFAULT_CONTEXT).isPresent());
    assertFalse(op.copy("", DEFAULT_CONTEXT).isPresent());
  }
}
