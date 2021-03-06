package io.dvp.ds.db;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static java.util.Collections.unmodifiableMap;

public class Mappers {

    @SuppressWarnings("rawtypes")
    public static BiFunction<Deque<Object>, CriteriaBuilder, Predicate> equalTo() {
        return (deque, cb) -> {
            Object right = deque.removeFirst();
            Expression left = (Expression) deque.removeFirst();
            if (right instanceof Expression) {
                return cb.equal(left, (Expression)right);
            } else {
                return cb.equal(left, right);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static BiFunction<Deque<Object>, CriteriaBuilder, Predicate> and() {
        return (deque, cb) -> {
            Expression<Boolean> right = (Expression<Boolean>) deque.removeFirst();
            Expression<Boolean> left = (Expression<Boolean>) deque.removeFirst();
            return cb.and(left, right);
        };
    }

    @SuppressWarnings("unchecked")
    public static BiFunction<Deque<Object>, CriteriaBuilder, Predicate> or() {
        return (deque, cb) -> {
            Expression<Boolean> right = (Expression<Boolean>)deque.removeFirst();
            Expression<Boolean> left = (Expression<Boolean>) deque.removeFirst();
            return cb.or(left, right);
        };
    }

    public static Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> defaultMappers() {
        Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> map = new HashMap<>();
        map.put("=", Mappers.equalTo());
        map.put("and", Mappers.and());
        map.put("or", Mappers.or());
        return unmodifiableMap(map);
    }
}
