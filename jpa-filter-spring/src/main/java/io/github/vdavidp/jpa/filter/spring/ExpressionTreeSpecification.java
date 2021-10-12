package io.github.vdavidp.jpa.filter.spring;

import static io.github.vdavidp.jpa.filter.db.Mappers.defaultMappers;

import io.github.vdavidp.jpa.filter.db.Binder;
import io.github.vdavidp.jpa.filter.el.Defaults;
import io.github.vdavidp.jpa.filter.el.ExpressionTree;
import io.github.vdavidp.jpa.filter.el.Operand;
import io.github.vdavidp.jpa.filter.el.Operator;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class ExpressionTreeSpecification implements Specification<Object> {

  private final String expression;
  private final ExpressionTreeConfigurator configurator;
  private final ObjectProvider<Binder> binderProvider;

  @Override
  public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> criteriaQuery,
      CriteriaBuilder criteriaBuilder) {
    Map<String, BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate>> mappers = new HashMap<>(
        defaultMappers());
    List<Operand> operands = new ArrayList<>(Defaults.operands());
    List<Operator> operators = new ArrayList<>(Defaults.operators());

    if (configurator != null) {
      configurator.modifyOperands(operands);
      configurator.modifyOperators(operators);
      configurator.modifyMappers(mappers);
    }

    Binder binder = binderProvider.getObject(root, criteriaQuery, criteriaBuilder, mappers);

    ExpressionTree et = new ExpressionTree(expression, operands, operators);
    et.visit(binder);

    return binder.getPredicate();
  }
}
