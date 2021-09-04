package com.github.vdavidp.jpa.filter.spring;

import com.github.vdavidp.jpa.filter.el.Symbol;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.persistence.criteria.CriteriaBuilder;

public interface ExpressionTreeConfigurator {

  void modifyMappers(Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Object>> mappers);

  void modifySymbols(List<Symbol> symbols);
}
