package io.dvp.jpa.filter.spring;

import io.dvp.jpa.filter.el.Symbol;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

public interface ExpressionTreeConfigurator {

  void modifyMappers(Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> mappers);

  void modifySymbols(List<Symbol> symbols);
}
