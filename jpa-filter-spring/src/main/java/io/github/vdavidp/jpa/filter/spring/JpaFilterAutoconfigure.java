package io.github.vdavidp.jpa.filter.spring;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JpaFilterAutoconfigure {
  
  @Bean
  SpecificationProvider expressionTreeProvider(
      @Autowired(required = false) SpecificationConfigurator configurator) {
    return new SpecificationProvider(configurator);
  }
  
  @Bean
  HqlProvider hqlProvider(
      @Autowired(required = false) HqlConfigurator configurator) {
    return new HqlProvider(configurator);
  }
  
  @Bean
  @ConditionalOnWebApplication
  WebMvcConfigurer expressionTreeLinker(
      SpecificationProvider provider) {

    return new WebMvcConfigurer() {
      @Override
      public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new JpaFilterArgumentResolver(provider));
      }
    };
  }
}
