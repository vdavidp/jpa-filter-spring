package io.github.vdavidp.jpa.filter.el;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OperandTest {

  Operand number = spy(Operand.class);

  @BeforeEach
  void init() {
    when(number.toString()).thenReturn("[2]");
  }

  @Test
  void merge() {
    Symbol other = number.merge(new BinaryOperator("+", 10));
    assertEquals("[[2]+null]", other.toString());
  }
}
