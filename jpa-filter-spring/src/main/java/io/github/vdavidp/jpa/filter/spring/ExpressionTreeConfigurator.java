package io.github.vdavidp.jpa.filter.spring;

import io.github.vdavidp.jpa.filter.el.Operand;
import io.github.vdavidp.jpa.filter.el.Operator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public interface ExpressionTreeConfigurator {

  void modifyMappers(Map<String, BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate>> mappers);

  void modifyOperands(List<Operand> operands);
  
  void modifyOperators(List<Operator> operators);
}