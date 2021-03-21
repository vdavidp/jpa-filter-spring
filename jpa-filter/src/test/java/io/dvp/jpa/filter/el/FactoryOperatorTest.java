package io.dvp.jpa.filter.el;

import static io.dvp.jpa.filter.el.Helper.DEFAULT_CONTEXT;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class FactoryOperatorTest {

  FactoryOperator op = new FactoryOperator(".", 50, singletonList(new DecimalFactory()));

  @Test
  void copy() {
    assertNotSame(op, op.copy(".", DEFAULT_CONTEXT).get());
    assertFalse(op.copy(" . ", DEFAULT_CONTEXT).isPresent());
    assertFalse(op.copy("a", DEFAULT_CONTEXT).isPresent());
    assertFalse(op.copy(",", DEFAULT_CONTEXT).isPresent());
  }

  @Test
  void merge() {
    Symbol left = new IntegerOperand().copy("938", DEFAULT_CONTEXT).get();
    Symbol right = new IntegerOperand().copy("32", DEFAULT_CONTEXT).get();

    assertSame(op, op.merge(left));

    Symbol other = op.merge(right);
    assertNotSame(other, op);
    assertEquals("[938.32]", other.toString());
  }

  @Test
  void mergeChained() {

  }
}
