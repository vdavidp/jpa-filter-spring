package io.dvp.jpa.filter.db;

import static java.util.Collections.unmodifiableMap;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public class Mappers {

  @SuppressWarnings("rawtypes")
  public static BiFunction<Deque<Object>, CriteriaBuilder, Predicate> equalTo() {
    return (deque, cb) -> {
      Object right = deque.removeFirst();
      Expression left = (Expression) deque.removeFirst();
      if (right instanceof Expression) {
        return cb.equal(left, (Expression) right);
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
      Expression<Boolean> right = (Expression<Boolean>) deque.removeFirst();
      Expression<Boolean> left = (Expression<Boolean>) deque.removeFirst();
      return cb.or(left, right);
    };
  }

  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Object>, CriteriaBuilder, Predicate> isTrue() {
    return (deque, cb) -> {
      Expression<Boolean> bool = (Expression<Boolean>) deque.removeFirst();
      return cb.isTrue(bool);
    };
  }

  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Object>, CriteriaBuilder, Predicate> isFalse() {
    return (deque, cb) -> {
      Expression<Boolean> bool = (Expression<Boolean>) deque.removeFirst();
      return cb.isFalse(bool);
    };
  }

  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Object>, CriteriaBuilder, Predicate> isNull() {
    return (deque, cb) -> {
      Expression<Object> obj = (Expression<Object>) deque.removeFirst();
      return cb.isNull(obj);
    };
  }

  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Object>, CriteriaBuilder, Predicate> isNotNull() {
    return (deque, cb) -> {
      Expression<Object> obj = (Expression<Object>) deque.removeFirst();
      return cb.isNotNull(obj);
    };
  }

  public static Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> defaultMappers() {
    Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> map = new HashMap<>();
    map.put("=", Mappers.equalTo());
    map.put("and", Mappers.and());
    map.put("or", Mappers.or());
    map.put("is true", Mappers.isTrue());
    map.put("is false", Mappers.isFalse());
    map.put("is null", Mappers.isNull());
    map.put("is not null", Mappers.isNotNull());
    return unmodifiableMap(map);
  }
}
