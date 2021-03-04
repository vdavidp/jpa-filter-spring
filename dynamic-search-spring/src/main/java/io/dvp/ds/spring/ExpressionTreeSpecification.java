package io.dvp.ds.spring;

import io.dvp.ds.db.DatabaseBinder;
import io.dvp.ds.db.Mappers;
import io.dvp.ds.el.ExpressionTree;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class ExpressionTreeSpecification implements Specification {
    private final String expression;

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> map = new HashMap<>();
        map.put("=", Mappers.equalTo());
        map.put("and", Mappers.and());
        map.put("or", Mappers.or());

        ExpressionTree et = ExpressionTree.build(expression, ExpressionTree.defaultSymbols());
        DatabaseBinder binder = new DatabaseBinder(root, criteriaBuilder);
        binder.setMappers(map);
        et.getRoot().visit(binder);

        return binder.getPredicate();
    }
}
