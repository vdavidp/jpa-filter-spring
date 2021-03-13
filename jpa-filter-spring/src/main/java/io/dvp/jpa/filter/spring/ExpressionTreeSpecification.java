package io.dvp.jpa.filter.spring;

import static io.dvp.jpa.filter.db.Mappers.defaultMappers;
import static io.dvp.jpa.filter.el.ExpressionTree.defaultSymbols;
import static java.util.Arrays.asList;

import io.dvp.jpa.filter.db.Binder;
import io.dvp.jpa.filter.el.ExpressionTree;
import io.dvp.jpa.filter.el.Symbol;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
    Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> mappers = new HashMap<>(
        defaultMappers());
    List<Symbol> symbols = new ArrayList<>(asList(defaultSymbols()));

    if (configurator != null) {
      configurator.modifySymbols(symbols);
      configurator.modifyMappers(mappers);
    }

    Binder binder = binderProvider.getObject(root, criteriaQuery, criteriaBuilder, mappers);

    ExpressionTree et = ExpressionTree.build(expression, symbols.toArray(new Symbol[]{}));
    et.getRoot().visit(binder);

    return binder.getPredicate();
  }
}
