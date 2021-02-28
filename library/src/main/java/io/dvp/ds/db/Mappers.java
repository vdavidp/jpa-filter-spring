package io.dvp.ds.db;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Deque;
import java.util.function.BiFunction;

public class Mappers {

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

    public static BiFunction<Deque<Object>, CriteriaBuilder, Predicate> and() {
        return (deque, cb) -> {
            Expression right = (Expression)deque.removeFirst();
            Expression left = (Expression) deque.removeFirst();
            return cb.and(left, right);
        };
    }

    public static BiFunction<Deque<Object>, CriteriaBuilder, Predicate> or() {
        return (deque, cb) -> {
            Expression right = (Expression)deque.removeFirst();
            Expression left = (Expression) deque.removeFirst();
            return cb.or(left, right);
        };
    }
}
