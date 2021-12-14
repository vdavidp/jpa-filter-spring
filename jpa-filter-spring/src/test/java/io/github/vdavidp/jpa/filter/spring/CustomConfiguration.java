package io.github.vdavidp.jpa.filter.spring;

import io.github.vdavidp.jpa.filter.el.BinaryOperator;
import io.github.vdavidp.jpa.filter.el.Operand;
import io.github.vdavidp.jpa.filter.el.Operator;
import io.github.vdavidp.jpa.filter.el.UnaryOperator;
import io.github.vdavidp.jpa.filter.el.UnaryOperator.Order;
import static java.lang.String.format;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CustomConfiguration {

  @Bean
  SpecificationConfigurator specificationConfig() {
    return new SpecificationConfig();
  }
  
  @Bean
  HqlConfigurator hqlConfig() {
    return new HqlConfig();
  }
}

class SpecificationConfig implements SpecificationConfigurator {

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
  
class HqlConfig implements HqlConfigurator {

    @Override
  public void modifyOperands(List<Operand> operands) {
    
  }

  @Override
  public void modifyOperators(List<Operator> operators) {
    operators.add(new BinaryOperator("~", 40));
  }

  @Override
  public void modifyMappers(Map<String, Function<Deque<String>, String>> operatorMapper) {
    operatorMapper.put("~", (stack) -> {
      String right = stack.pop();
      String left = stack.pop();
      return format("(%s like %s)", left, right);
    });
  }
}
