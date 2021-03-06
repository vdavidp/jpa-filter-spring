package io.github.vdavidp.jpa.filter.spring;


import io.github.vdavidp.jpa.filter.db.CriteriaBinder;
import io.github.vdavidp.jpa.filter.el.Defaults;
import io.github.vdavidp.jpa.filter.el.ExpressionTree;
import io.github.vdavidp.jpa.filter.el.Operand;
import io.github.vdavidp.jpa.filter.el.Operator;
import io.github.vdavidp.jpa.filter.spring.visitor.FieldExistingVerifier;
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
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import static io.github.vdavidp.jpa.filter.db.Mappers.defaultCriteriaMappers;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class ExpressionTreeSpecification<T> implements Specification<T> {

  private String expression;
  private SpecificationConfigurator configurator;
  private FieldExistingVerifier fieldExistingVerifier;

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery,
      CriteriaBuilder criteriaBuilder) {
    Map<String, BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate>> mappers = new HashMap<>(
        defaultCriteriaMappers());
    List<Operand> operands = new ArrayList<>(Defaults.operands());
    List<Operator> operators = new ArrayList<>(Defaults.operators());

    if (configurator != null) {
      configurator.modifyOperands(operands);
      configurator.modifyOperators(operators);
      configurator.modifyMappers(mappers);
    }

    ExpressionTree et = new ExpressionTree(expression, operands, operators);
    log.debug("expression tree: {}", et.toString());
    et.visit(fieldExistingVerifier);
    
    CriteriaBinder binder = new CriteriaBinder(root, criteriaQuery, criteriaBuilder, mappers);
    et.visit(binder);

    return binder.getPredicate();
  }
}
