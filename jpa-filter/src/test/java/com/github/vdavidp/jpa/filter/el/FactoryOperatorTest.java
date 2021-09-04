package com.github.vdavidp.jpa.filter.el;

import static com.github.vdavidp.jpa.filter.el.ContextItem.MULTIPLIER;
import static com.github.vdavidp.jpa.filter.el.TestHelper.IDENTITY_CONTEXT;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

public class FactoryOperatorTest {

  FactoryOperator op = new FactoryOperator(".", 50, singletonList(new DecimalFactory()));

  @Test
  void copy() {
    assertNotSame(op, op.copy(".", IDENTITY_CONTEXT).get());
    assertFalse(op.copy(" . ", IDENTITY_CONTEXT).isPresent());
    assertFalse(op.copy("a", IDENTITY_CONTEXT).isPresent());
    assertFalse(op.copy(",", IDENTITY_CONTEXT).isPresent());
  }

  @Test
  void merge() {
    Symbol left = new IntegerOperand().copy("938", IDENTITY_CONTEXT).get();
    Symbol right = new IntegerOperand().copy("32", IDENTITY_CONTEXT).get();

    assertSame(op, op.merge(left));

    Symbol other = op.merge(right);
    assertNotSame(other, op);
    assertEquals("[938.32]", other.toString());
  }

  @Test
  void copyWithWeightMultiplier() {
    Optional<Symbol> child = op.copy(".", singletonMap(MULTIPLIER, 200));
    assertTrue(child.isPresent());
    assertEquals(250, child.get().getWeight());
  }
}
