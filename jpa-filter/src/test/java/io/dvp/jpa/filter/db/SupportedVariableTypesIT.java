package io.dvp.jpa.filter.db;

import static java.util.Collections.singletonList;

import io.dvp.jpa.filter.db.entity.Dummy;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class SupportedVariableTypesIT {
  @PersistenceContext
  EntityManager entityManager;
  AssertContext<Dummy>.Verifier<Long> ctx;

  @BeforeEach
  void init() {
    ctx = new AssertContext<>(entityManager, Dummy.class).new Verifier<Long>(Dummy::getId);
  }

  @Test
  void acceptsInteger() {
    String exp = "{integerValue}=12";
    ctx.assertResultContains(exp, singletonList(2L));
  }

  @Test
  void acceptsLong() {
    String exp = "{longValue}=88";
    ctx.assertResultContains(exp, singletonList(1L));
  }

  @Test
  void acceptsBigInteger() {
    String exp = "{bigIntegerValue}=998";
    ctx.assertResultContains(exp, singletonList(2L));
  }

  @Test
  void acceptsFloat() {
    String exp = "{floatValue}=1.333";
    ctx.assertResultContains(exp, singletonList(2L));
  }

  @Test
  void acceptsDouble() {
    String exp = "{doubleValue}=9.123";
    ctx.assertResultContains(exp, singletonList(1L));
  }

  @Test
  void acceptsBigDecimal() {
    String exp = "{bigDecimalValue}=39.22";
    ctx.assertResultContains(exp, singletonList(2L));
  }
}
