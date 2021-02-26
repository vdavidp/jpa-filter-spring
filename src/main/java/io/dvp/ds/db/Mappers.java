package io.dvp.ds.db;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Queue;
import java.util.function.BiFunction;

public class Mappers {

    public static BiFunction<Queue<Object>, CriteriaBuilder, Predicate> equalTo() {
        return (queue, cb) -> {
            Expression left = (Expression)queue.poll();
            if (queue.peek() instanceof Expression) {
                return cb.equal(left, (Expression)queue.poll());
            } else {
                return cb.equal(left, queue.poll());
            }
        };
    }

    public static BiFunction<Queue<Object>, CriteriaBuilder, Predicate> and() {
        return (queue, cb) -> cb.and((Expression) queue.poll(), (Expression) queue.poll());
    }

    public static BiFunction<Queue<Object>, CriteriaBuilder, Predicate> or() {
        return (queue, cb) -> cb.or((Expression) queue.poll(), (Expression) queue.poll());
    }
}
