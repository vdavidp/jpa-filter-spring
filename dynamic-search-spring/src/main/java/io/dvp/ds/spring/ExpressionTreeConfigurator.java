package io.dvp.ds.spring;

import io.dvp.ds.el.Symbol;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public interface ExpressionTreeConfigurator {

    void modifyMappers(Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> mappers);

    void modifySymbols(List<Symbol> symbols);
}
