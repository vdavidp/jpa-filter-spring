package io.dvp.ds.spring;

import io.dvp.ds.db.Binder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Component
public class ExpressionTreeWebConfigurer implements WebMvcConfigurer {

    @Autowired(required = false)
    private ExpressionTreeConfigurator configurator;

    @Autowired
    private ObjectProvider<Binder> binderProvider;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ExpressionTreeArgumentResolver(configurator, binderProvider));
    }
}
