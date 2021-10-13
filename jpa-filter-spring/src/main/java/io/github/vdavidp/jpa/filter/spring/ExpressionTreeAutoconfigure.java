package io.github.vdavidp.jpa.filter.spring;

import io.github.vdavidp.jpa.filter.db.Binder;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnWebApplication
public class ExpressionTreeAutoconfigure {
  
  @Bean
  ExpressionTreeProvider expressionTreeProvider(
      @Autowired(required = false) ExpressionTreeConfigurator configurator) {
    return new ExpressionTreeProvider(configurator);
  }

  @Bean
  WebMvcConfigurer expressionTreeLinker(
      ExpressionTreeProvider provider,
      ObjectProvider<Binder> binderProvider) {

    return new WebMvcConfigurer() {
      @Override
      public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ExpressionTreeArgumentResolver(provider));
      }
    };
  }
}
