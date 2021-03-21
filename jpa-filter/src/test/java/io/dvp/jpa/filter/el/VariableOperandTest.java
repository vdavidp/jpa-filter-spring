package io.dvp.jpa.filter.el;

import static io.dvp.jpa.filter.el.Helper.DEFAULT_CONTEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

public class VariableOperandTest {

  VariableOperand var = new VariableOperand();

  @Test
  void copy() {
    assertValid("{prop123}");
    assertValid("{Other}");
    assertValid("{_12moreThan4you}");
    assertValid("{$hello}");
    assertValid("{some-variable}");
    assertValid("{inside/almost-anything.is=valid}");

    assertFalse(var.copy("123", DEFAULT_CONTEXT).isPresent());
    assertFalse(var.copy("3Three", DEFAULT_CONTEXT).isPresent());
    assertFalse(var.copy("_more", DEFAULT_CONTEXT).isPresent());
    assertFalse(var.copy("$following", DEFAULT_CONTEXT).isPresent());
    assertFalse(var.copy("{symbols { and } not allowed inside}", DEFAULT_CONTEXT).isPresent());
    assertFalse(var.copy("{}", DEFAULT_CONTEXT).isPresent());
    assertFalse(var.copy("{ }", DEFAULT_CONTEXT).isPresent());
    assertFalse(var.copy("{\t}", DEFAULT_CONTEXT).isPresent());
  }

  private void assertValid(String prop) {
    Optional<Symbol> opt = var.copy(prop, DEFAULT_CONTEXT);
    assertTrue(opt.isPresent());
    assertNotSame(var, opt.get());
    assertEquals("[" + prop.replaceAll("[{}]", "") + "]", opt.get().toString());
  }
}
