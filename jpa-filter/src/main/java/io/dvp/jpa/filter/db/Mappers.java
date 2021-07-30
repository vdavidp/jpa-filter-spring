package io.dvp.jpa.filter.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> equalTo() {
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
  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> and() {
    return (deque, cb) -> {
      Expression<Boolean> right = (Expression<Boolean>) deque.removeFirst();
      Expression<Boolean> left = (Expression<Boolean>) deque.removeFirst();
      return cb.and(left, right);
    };
  }

  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> or() {
    return (deque, cb) -> {
      Expression<Boolean> right = (Expression<Boolean>) deque.removeFirst();
      Expression<Boolean> left = (Expression<Boolean>) deque.removeFirst();
      return cb.or(left, right);
    };
  }

  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> isTrue() {
    return (deque, cb) -> {
      Expression<Boolean> bool = (Expression<Boolean>) deque.removeFirst();
      return cb.isTrue(bool);
    };
  }

  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> isFalse() {
    return (deque, cb) -> {
      Expression<Boolean> bool = (Expression<Boolean>) deque.removeFirst();
      return cb.isFalse(bool);
    };
  }

  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> isNull() {
    return (deque, cb) -> {
      Expression<Object> obj = (Expression<Object>) deque.removeFirst();
      return cb.isNull(obj);
    };
  }

  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> isNotNull() {
    return (deque, cb) -> {
      Expression<Object> obj = (Expression<Object>) deque.removeFirst();
      return cb.isNotNull(obj);
    };
  }
  
  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> date() {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    return (deque, cb) -> {
      try {
        String value = (String)deque.removeFirst();
        return formatter.parse(value);
      } catch (ParseException ex) {
        throw new RuntimeException("Expecting date format yyyy-MM-dd", ex);
      }
    };
  }

  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> greaterThan() {
    return (deque, cb) -> {
      Comparable<Object> right = (Comparable<Object>)deque.removeFirst();
      Expression<Comparable<Object>> left = (Expression<Comparable<Object>>) deque.removeFirst();
      return cb.greaterThan(left, right);
    };
  }

  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> greaterThanOrEqual() {
    return (deque, cb) -> {
      Comparable<Object> right = (Comparable<Object>) deque.removeFirst();
      Expression<Comparable<Object>> left = (Expression<Comparable<Object>>) deque.removeFirst();
      return cb.greaterThanOrEqualTo(left, right);
    };
  }

  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> lessThan() {
    return (deque, cb) -> {
      Comparable<Object> right = (Comparable<Object>) deque.removeFirst();
      Expression<Comparable<Object>> left = (Expression<Comparable<Object>>) deque.removeFirst();
      return cb.lessThan(left, right);
    };
  }

  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> lessThanOrEqual() {
    return (deque, cb) -> {
      Comparable<Object> right = (Comparable<Object>) deque.removeFirst();
      Expression<Comparable<Object>> left = (Expression<Comparable<Object>>) deque.removeFirst();
      return cb.lessThanOrEqualTo(left, right);
    };
  }

  public static Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Object>> defaultMappers() {
    Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Object>> map = new HashMap<>();
    map.put("=", Mappers.equalTo());
    map.put("and", Mappers.and());
    map.put("or", Mappers.or());
    map.put("is true", Mappers.isTrue());
    map.put("is false", Mappers.isFalse());
    map.put("is null", Mappers.isNull());
    map.put("is not null", Mappers.isNotNull());
    map.put(">", Mappers.greaterThan());
    map.put(">=", Mappers.greaterThanOrEqual());
    map.put("<", Mappers.lessThan());
    map.put("<=", Mappers.lessThanOrEqual());
    map.put("date", Mappers.date());
    return unmodifiableMap(map);
  }
}
