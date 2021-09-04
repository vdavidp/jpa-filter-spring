package com.github.vdavidp.jpa.filter.el;

import static com.github.vdavidp.jpa.filter.el.ContextItem.MULTIPLIER;
import static com.github.vdavidp.jpa.filter.el.TestHelper.IDENTITY_CONTEXT;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BinaryOperatorTest {

  BinaryOperator operator = new BinaryOperator("+m", 10);
  Symbol left, right, middle;

  Function<String, Symbol> numFactory = number -> new IntegerOperand().copy(number,
      IDENTITY_CONTEXT).get();

  @BeforeEach
  void init() {
    left = numFactory.apply("12");
    right = numFactory.apply("9");
    middle = numFactory.apply("38");
  }

  @Test
  void copy() {
    assertValid("+m");
    assertValid("+M");
    assertValid(" +m");
    assertValid(" +M");
    assertValid("    +m \t\t ");
    assertValid("    +M \t\t ");
    assertFalse(operator.copy("+ m", IDENTITY_CONTEXT).isPresent());
    assertFalse(operator.copy(" + M", IDENTITY_CONTEXT).isPresent());
    assertFalse(operator.copy("2", IDENTITY_CONTEXT).isPresent());
    assertFalse(operator.copy("++", IDENTITY_CONTEXT).isPresent());
  }

  void assertValid(String exp) {
    Optional<Symbol> op = operator.copy(exp, IDENTITY_CONTEXT);
    assertTrue(op.isPresent());
    assertNotSame(operator, op.get());
    assertSame(BinaryOperator.class, op.get().getClass());
    assertEquals("[null" + exp.trim().toLowerCase() + "null]", op.get().toString());
  }

  @Test
  void merge() {
    assertEquals(operator, operator.merge(left));
    assertEquals(operator, operator.merge(right));
    assertEquals("[[12]+m[9]]", operator.toString());

    Symbol op2 = operator.merge(new BinaryOperator("*", 0));
    assertNotEquals(op2, operator);
    assertEquals("[[[12]+m[9]]*null]", op2.toString());
  }

  @Test
  void mergeHeavierOperator() {
    BinaryOperator heavier = new BinaryOperator("/", 20);
    operator.merge(left).merge(middle);
    assertSame(operator, operator.merge(heavier));
    assertSame(operator, operator.merge(right));
    assertEquals("[[12]+m[[38]/[9]]]", operator.toString());
  }

  @Test
  void copyWithWeightMultiplier() {
    BinaryOperator op = new BinaryOperator("+", 10);
    Optional<Symbol> child = op.copy("+", singletonMap(MULTIPLIER, 200));
    assertTrue(child.isPresent());
    assertEquals(210, child.get().getWeight());
  }
}
