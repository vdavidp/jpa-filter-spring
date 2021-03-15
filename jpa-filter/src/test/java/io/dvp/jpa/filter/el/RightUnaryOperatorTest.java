package io.dvp.jpa.filter.el;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class RightUnaryOperatorTest {
  RightUnaryOperator proto = new RightUnaryOperator("is null", 50);
  Symbol operand = new VariableOperand().copy("{data}").get();

  @Test
  void copy() {
    assertIsValid("is null");
    assertIsValid("Is Null");
    assertIsValid("  IS NULL \t\t ");
    assertFalse(proto.copy("is  null").isPresent());
    assertFalse(proto.copy("isnull").isPresent());
  }

  void assertIsValid(String exp) {
    Optional<Symbol> copy = proto.copy(exp);
    assertTrue(copy.isPresent());
    assertNotSame(proto, copy.get());
    assertSame(RightUnaryOperator.class, copy.get().getClass());
    assertEquals("[nullIS NULL]", copy.get().toString());
  }

  @Test
  void merge() {
    Symbol root = operand.merge(proto);
    assertSame(root, proto);
    assertEquals("[[data]IS NULL]", proto.toString());
  }

  @Test
  void mergeSameWeightOperator() {
    Symbol sameWeight = new RightUnaryOperator("is true", 50);

    proto.merge(operand);
    Symbol root = proto.merge(sameWeight);
    assertSame(sameWeight, root);
    assertEquals("[[[data]IS NULL]IS TRUE]", root.toString());
  }

  @Test
  void mergeLighterOperator() {
    Symbol lightOperator = new BinaryOperator("+", 40);
    proto.merge(operand);
    assertThrows(RuntimeException.class, () -> proto.merge(lightOperator));
  }
}
