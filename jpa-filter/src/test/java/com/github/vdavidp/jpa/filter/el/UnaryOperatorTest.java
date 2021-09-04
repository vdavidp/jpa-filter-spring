package com.github.vdavidp.jpa.filter.el;

import static com.github.vdavidp.jpa.filter.el.ContextItem.MULTIPLIER;
import static com.github.vdavidp.jpa.filter.el.TestHelper.IDENTITY_CONTEXT;
import com.github.vdavidp.jpa.filter.el.UnaryOperator.Order;
import static java.util.Collections.singletonMap;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class UnaryOperatorTest {
  UnaryOperator proto = new UnaryOperator("Is Null", 50, Order.RIGHT);
  Symbol operand = new VariableOperand().copy("{data}", IDENTITY_CONTEXT).get();

  @Test
  void copy() {
    assertIsValid("is null");
    assertIsValid("Is Null");
    assertIsValid("  IS NULL \t\t ");
    assertIsValid("  Is null \t\t ");
    assertFalse(proto.copy("is  null", IDENTITY_CONTEXT).isPresent());
    assertFalse(proto.copy("isnull", IDENTITY_CONTEXT).isPresent());
  }

  void assertIsValid(String exp) {
    Optional<Symbol> copy = proto.copy(exp, IDENTITY_CONTEXT);
    assertTrue(copy.isPresent());
    assertNotSame(proto, copy.get());
    assertSame(UnaryOperator.class, copy.get().getClass());
    assertEquals("[nullIs Null]", copy.get().toString());
  }

  @Test
  void merge() {
    Symbol root = operand.merge(proto);
    assertSame(root, proto);
    assertEquals("[[data]Is Null]", proto.toString());
  }

  @Test
  void mergeSameWeightOrWeighterOperator() {
    Symbol sameWeight = new UnaryOperator("is true", 50, Order.RIGHT);

    proto.merge(operand);
    Symbol root = proto.merge(sameWeight);
    assertSame(sameWeight, root);
    assertEquals("[[[data]Is Null]is true]", root.toString());
  }

  @Test
  void mergeLighterOperator() {
    Symbol lightOperator = new BinaryOperator("+", 40);
    proto.merge(operand);
    assertThrows(RuntimeException.class, () -> proto.merge(lightOperator));
  }

  @Test
  void copyWithWeightMultiplier() {
    Optional<Symbol> child = proto.copy("Is null", singletonMap(MULTIPLIER, 200));
    assertTrue(child.isPresent());
    assertEquals(250, child.get().getWeight());
  }
  
  @Test
  void leftToString() {
    UnaryOperator op = new UnaryOperator("date", 50, Order.LEFT);
    op.merge(new VarcharOperand().copy("'2010-10-23'", IDENTITY_CONTEXT).get());
    assertEquals("[date[2010-10-23]]", op.toString());
  }
}
