package io.github.vdavidp.jpa.filter.spring;

import io.github.vdavidp.jpa.filter.el.Operand;
import io.github.vdavidp.jpa.filter.el.Operator;
import io.github.vdavidp.jpa.filter.el.UnaryOperator;
import io.github.vdavidp.jpa.filter.el.UnaryOperator.Order;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CustomConfiguration {

  @Bean
  SpecificationConfigurator configurator() {
    return new ExpTreeConfiguration();
  }
}

class ExpTreeConfiguration implements SpecificationConfigurator {

  @Override
  public void modifyOperands(List<Operand> operands) {
    
  }

  @Override
  public void modifyOperators(List<Operator> operators) {
    operators.add(new UnaryOperator("IS TRUE", 40, Order.RIGHT));
  }

  @Override
  public void modifyMappers(Map<String, BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate>> mappers) {
    mappers.put("IS TRUE", (stack, cb) -> {
      Expression<Boolean> operand = (Expression<Boolean>)stack.pop();
      return cb.isTrue(operand);
    });
  }
}
