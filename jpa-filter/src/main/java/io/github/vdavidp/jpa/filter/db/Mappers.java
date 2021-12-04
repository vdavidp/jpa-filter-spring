package io.github.vdavidp.jpa.filter.db;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static java.util.Collections.unmodifiableMap;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public class Mappers {

//  @SuppressWarnings("rawtypes")
  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> equalTo() {
    return (deque, cb) -> {
      Expression<?> right = deque.removeFirst();
      Expression<?> left = deque.removeFirst();
      return cb.equal(left, right);
    };
  }
  
  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> notEqualsTo() {
    return (deque, cb) -> {
      Expression<?> right = deque.removeFirst();
      Expression<?> left = deque.removeFirst();
      return cb.notEqual(left, right);
    };
  }

//  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> and() {
    return (deque, cb) -> {
      Expression<Boolean> right = deque.removeFirst().as(Boolean.class);
      Expression<Boolean> left = deque.removeFirst().as(Boolean.class);
      return cb.and(left, right);
    };
  }

//  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> or() {
    return (deque, cb) -> {
      Expression<Boolean> right = deque.removeFirst().as(Boolean.class);
      Expression<Boolean> left = deque.removeFirst().as(Boolean.class);
      return cb.or(left, right);
    };
  }

//  @SuppressWarnings("unchecked")
//  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Object> isTrue() {
//    return (deque, cb) -> {
//      Expression<Boolean> bool = (Expression<Boolean>) deque.removeFirst();
//      return cb.isTrue(bool);
//    };
//  }
//
//  @SuppressWarnings("unchecked")
//  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> isFalse() {
//    return (deque, cb) -> {
//      Expression<Boolean> bool = (Expression<Boolean>) deque.removeFirst();
//      return cb.isFalse(bool);
//    };
//  }
//
//  @SuppressWarnings("unchecked")
//  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> isNull() {
//    return (deque, cb) -> {
//      Expression<?> obj = deque.removeFirst();
//      return cb.isNull(obj);
//    };
//  }
//
//  @SuppressWarnings("unchecked")
//  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> isNotNull() {
//    return (deque, cb) -> {
//      Expression<Object> obj = (Expression<Object>) deque.removeFirst();
//      return cb.isNotNull(obj);
//    };
//  }
//  
//  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> date() {
//    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//    return (deque, cb) -> {
//      try {
//        String value = (String)deque.removeFirst();
//        return formatter.parse(value);
//      } catch (ParseException ex) {
//        throw new RuntimeException("Expecting date format yyyy-MM-dd", ex);
//      }
//    };
//  }

//  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> greaterThan() {
    return (deque, cb) -> {
      Expression<Comparable<Object>> right = (Expression<Comparable<Object>>)deque.removeFirst();
      Expression<Comparable<Object>> left = (Expression<Comparable<Object>>)deque.removeFirst();
      return cb.greaterThan(left, right);
    };
  }

//  @SuppressWarnings("unchecked")
//  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> greaterThanOrEqual() {
//    return (deque, cb) -> {
//      Comparable<Object> right = (Comparable<Object>) deque.removeFirst();
//      Expression<Comparable<Object>> left = (Expression<Comparable<Object>>) deque.removeFirst();
//      return cb.greaterThanOrEqualTo(left, right);
//    };
//  }

//  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> lessThan() {
    return (deque, cb) -> {
      Expression<Comparable<Object>> right = (Expression<Comparable<Object>>)deque.removeFirst();
      Expression<Comparable<Object>> left = (Expression<Comparable<Object>>)deque.removeFirst();
      return cb.lessThan(left, right);
    };
  }

//  @SuppressWarnings("unchecked")
//  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> lessThanOrEqual() {
//    return (deque, cb) -> {
//      Comparable<Object> right = (Comparable<Object>) deque.removeFirst();
//      Expression<Comparable<Object>> left = (Expression<Comparable<Object>>) deque.removeFirst();
//      return cb.lessThanOrEqualTo(left, right);
//    };
//  }

  public static Map<String, BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate>> defaultCriteriaMappers() {
    Map<String, BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate>> map = new HashMap<>();
    map.put(":", Mappers.equalTo());
    map.put("!", Mappers.notEqualsTo());
    map.put("AND", Mappers.and());
    map.put("OR", Mappers.or());
//    map.put("is true", Mappers.isTrue());
//    map.put("is false", Mappers.isFalse());
//    map.put("is null", Mappers.isNull());
//    map.put("is not null", Mappers.isNotNull());
    map.put(">", Mappers.greaterThan());
//    map.put(">=", Mappers.greaterThanOrEqual());
    map.put("<", Mappers.lessThan());
//    map.put("<=", Mappers.lessThanOrEqual());
//    map.put("date", Mappers.date());
    return unmodifiableMap(map);
  }
  
  public static Function<String, String> defaultHqlMappers() {
    return (symbol) -> {
      if (":".equals(symbol)) {
        return "=";
      } else if ("!".equals(symbol)) {
        return "!=";
      } else {
        return symbol;
      }
    };
  }
}
