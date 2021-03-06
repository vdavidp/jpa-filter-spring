package io.dvp.ds;

import io.dvp.ds.el.BinaryOperator;
import io.dvp.ds.el.Symbol;
import io.dvp.ds.spring.ExpressionTreeConfigurator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@TestConfiguration
public class CustomConfiguration {

    @Bean
    ExpressionTreeConfigurator configurator() {
        return new ExpTreeConfiguration();
    }
}

class ExpTreeConfiguration implements ExpressionTreeConfigurator {

    @Override
    public void modifyMappers(Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> mappers) {
        mappers.put(":", mappers.remove("="));
        mappers.put("||", mappers.remove("or"));
    }

    @Override
    public void modifySymbols(List<Symbol> symbols) {
        symbols.add(new BinaryOperator(":", 20));
        symbols.add(new BinaryOperator("||", 10));
    }
}
