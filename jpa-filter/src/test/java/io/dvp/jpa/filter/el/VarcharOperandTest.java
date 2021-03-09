package io.dvp.jpa.filter.el;

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

    assertFalse(factory.copy("'''").isPresent());
    assertFalse(factory.copy("'Not valid").isPresent());
    assertFalse(factory.copy("other'").isPresent());
    assertFalse(factory.copy("without quotes").isPresent());
    assertFalse(factory.copy("123").isPresent());
    assertFalse(factory.copy("'didi''d").isPresent());
    assertFalse(factory.copy("'").isPresent());
  }

  void assertValid(String rawSymbol, String interpretedSymbol) {
    Optional<Symbol> obj = factory.copy(rawSymbol);
    assertTrue(obj.isPresent());
    assertNotSame(factory, obj.get());
    assertEquals(interpretedSymbol, obj.get().toString());
  }
}
