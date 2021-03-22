package io.dvp.jpa.filter.el;

import static io.dvp.jpa.filter.el.TestHelper.IDENTITY_CONTEXT;
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

    assertFalse(var.copy("123", IDENTITY_CONTEXT).isPresent());
    assertFalse(var.copy("3Three", IDENTITY_CONTEXT).isPresent());
    assertFalse(var.copy("_more", IDENTITY_CONTEXT).isPresent());
    assertFalse(var.copy("$following", IDENTITY_CONTEXT).isPresent());
    assertFalse(var.copy("{symbols { and } not allowed inside}", IDENTITY_CONTEXT).isPresent());
    assertFalse(var.copy("{}", IDENTITY_CONTEXT).isPresent());
    assertFalse(var.copy("{ }", IDENTITY_CONTEXT).isPresent());
    assertFalse(var.copy("{\t}", IDENTITY_CONTEXT).isPresent());
  }

  private void assertValid(String prop) {
    Optional<Symbol> opt = var.copy(prop, IDENTITY_CONTEXT);
    assertTrue(opt.isPresent());
    assertNotSame(var, opt.get());
    assertEquals("[" + prop.replaceAll("[{}]", "") + "]", opt.get().toString());
  }
}
