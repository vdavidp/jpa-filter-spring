package io.dvp.ds.spring;

import io.dvp.ds.db.Binder;
import io.dvp.ds.db.DatabaseBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Deque;
import java.util.Map;
import java.util.function.BiFunction;

@Configuration
public class ExpressionTreeBeans {

    @Bean
    @Scope("prototype")
    Binder databaseVisitor(Root<Object> root,
                           CriteriaBuilder cb,
                           Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> mappers) {

        return new DatabaseBinder<>(root, cb, mappers);
    }
}
