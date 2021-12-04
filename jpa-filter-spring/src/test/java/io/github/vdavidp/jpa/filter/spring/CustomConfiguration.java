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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CustomConfiguration {

  @Bean
  SpecificationConfigurator configurator() {
    return new ExpTreeConfiguration();
  }
}

class ExpTreeConfiguration implements SpecificationConfigurator {

//  @Override
//  public void modifyMappers(
//      Map<String, BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate>> mappers) {
//    mappers.put(":", mappers.remove("="));
//    mappers.put("||", mappers.remove("or"));
//  }
//
//  @Override
//  public void modifySymbols(List<Symbol> symbols) {
//    symbols.add(new BinaryOperator(":", 20));
//    symbols.add(new BinaryOperator("||", 10));
//  }

  @Override
  public void modifyOperands(List<Operand> operands) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void modifyOperators(List<Operator> operators) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void modifyMappers(Map<String, BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate>> mappers) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
