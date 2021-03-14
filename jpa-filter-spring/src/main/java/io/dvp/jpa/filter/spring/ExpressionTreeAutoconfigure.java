package io.dvp.jpa.filter.spring;

import io.dvp.jpa.filter.db.Binder;
import io.dvp.jpa.filter.db.DatabaseBinder;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnWebApplication
public class ExpressionTreeAutoconfigure {

  @Bean
  @Scope("prototype")
  <T> Binder databaseVisitor(
      Root<T> root,
      CriteriaQuery<T> cq,
      CriteriaBuilder cb,
      Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> mappers) {

    return new DatabaseBinder<>(root, cq, cb, mappers);
  }

  @Bean
  WebMvcConfigurer expressionTreeLinker(
      @Autowired(required = false) ExpressionTreeConfigurator configurator,
      ObjectProvider<Binder> binderProvider) {

    return new WebMvcConfigurer() {
      @Override
      public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ExpressionTreeArgumentResolver(configurator, binderProvider));
      }
    };
  }
}
