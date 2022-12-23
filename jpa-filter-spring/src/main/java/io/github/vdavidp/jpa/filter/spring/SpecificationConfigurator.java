package io.github.vdavidp.jpa.filter.spring;

import io.github.vdavidp.jpa.filter.el.Operand;
import io.github.vdavidp.jpa.filter.el.Operator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public interface SpecificationConfigurator {

  void modifyMappers(Map<String, BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate>> mappers);

  void modifyOperands(List<Operand> operands);
  
  void modifyOperators(List<Operator> operators);
}
