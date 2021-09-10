package io.github.vdavidp.jpa.filter.spring;

import io.github.vdavidp.jpa.filter.el.BinaryOperator;
import io.github.vdavidp.jpa.filter.el.Symbol;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.persistence.criteria.CriteriaBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CustomConfiguration {

  @Bean
  ExpressionTreeConfigurator configurator() {
    return new ExpTreeConfiguration();
  }
}

class ExpTreeConfiguration implements ExpressionTreeConfigurator {

  @Override
  public void modifyMappers(
      Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Object>> mappers) {
    mappers.put(":", mappers.remove("="));
    mappers.put("||", mappers.remove("or"));
  }

  @Override
  public void modifySymbols(List<Symbol> symbols) {
    symbols.add(new BinaryOperator(":", 20));
    symbols.add(new BinaryOperator("||", 10));
  }
}
