package io.github.vdavidp.jpa.filter.el;

import static io.github.vdavidp.jpa.filter.el.TestHelper.IDENTITY_CONTEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

public class VarcharOperandTest {

  final VarcharOperand factory = new VarcharOperand();

  @Test
  void copy() {
    assertValid("'Some Text'", "[Some Text]");
    assertValid("''", "[]");
    assertValid("'That\\'s'", "[That's]");
    assertValid("'ooo\\'\\'s'", "[ooo''s]");
    assertValid("'\\\\'", "[\\]");

    assertFalse(factory.copy("'''", IDENTITY_CONTEXT).isPresent());
    assertFalse(factory.copy("'Not valid", IDENTITY_CONTEXT).isPresent());
    assertFalse(factory.copy("other'", IDENTITY_CONTEXT).isPresent());
    assertFalse(factory.copy("without quotes", IDENTITY_CONTEXT).isPresent());
    assertFalse(factory.copy("123", IDENTITY_CONTEXT).isPresent());
    assertFalse(factory.copy("'didi''d", IDENTITY_CONTEXT).isPresent());
    assertFalse(factory.copy("'", IDENTITY_CONTEXT).isPresent());
  }

  void assertValid(String rawSymbol, String interpretedSymbol) {
    Optional<Symbol> obj = factory.copy(rawSymbol, IDENTITY_CONTEXT);
    assertTrue(obj.isPresent());
    assertNotSame(factory, obj.get());
    assertEquals(interpretedSymbol, obj.get().toString());
  }
}
