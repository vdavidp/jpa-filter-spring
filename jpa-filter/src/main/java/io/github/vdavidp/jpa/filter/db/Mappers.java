package io.github.vdavidp.jpa.filter.db;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableMap;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public class Mappers {

//  @SuppressWarnings("rawtypes")
  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> equalTo() {
    return (stack, cb) -> {
      Expression<?> right = stack.pop();
      Expression<?> left = stack.pop();
      return cb.equal(left, right);
    };
  }
  
  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> notEqualsTo() {
    return (stack, cb) -> {
      Expression<?> right = stack.pop();
      Expression<?> left = stack.pop();
      return cb.notEqual(left, right);
    };
  }

//  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> and() {
    return (stack, cb) -> {
      Expression<Boolean> right = stack.pop().as(Boolean.class);
      Expression<Boolean> left = stack.pop().as(Boolean.class);
      return cb.and(left, right);
    };
  }

//  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> or() {
    return (stack, cb) -> {
      Expression<Boolean> right = stack.pop().as(Boolean.class);
      Expression<Boolean> left = stack.pop().as(Boolean.class);
      return cb.or(left, right);
    };
  }

//  @SuppressWarnings("unchecked")
//  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Object> isTrue() {
//    return (stack, cb) -> {
//      Expression<Boolean> bool = (Expression<Boolean>) stack.pop();
//      return cb.isTrue(bool);
//    };
//  }
//
//  @SuppressWarnings("unchecked")
//  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> isFalse() {
//    return (stack, cb) -> {
//      Expression<Boolean> bool = (Expression<Boolean>) stack.pop();
//      return cb.isFalse(bool);
//    };
//  }
//
//  @SuppressWarnings("unchecked")
//  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> isNull() {
//    return (stack, cb) -> {
//      Expression<?> obj = stack.pop();
//      return cb.isNull(obj);
//    };
//  }
//
//  @SuppressWarnings("unchecked")
//  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> isNotNull() {
//    return (stack, cb) -> {
//      Expression<Object> obj = (Expression<Object>) stack.pop();
//      return cb.isNotNull(obj);
//    };
//  }
//  
//  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> date() {
//    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//    return (stack, cb) -> {
//      try {
//        String value = (String)stack.pop();
//        return formatter.parse(value);
//      } catch (ParseException ex) {
//        throw new RuntimeException("Expecting date format yyyy-MM-dd", ex);
//      }
//    };
//  }

//  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> greaterThan() {
    return (stack, cb) -> {
      Expression<Comparable<Object>> right = (Expression<Comparable<Object>>)stack.pop();
      Expression<Comparable<Object>> left = (Expression<Comparable<Object>>)stack.pop();
      return cb.greaterThan(left, right);
    };
  }

//  @SuppressWarnings("unchecked")
//  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> greaterThanOrEqual() {
//    return (stack, cb) -> {
//      Comparable<Object> right = (Comparable<Object>) stack.pop();
//      Expression<Comparable<Object>> left = (Expression<Comparable<Object>>) stack.pop();
//      return cb.greaterThanOrEqualTo(left, right);
//    };
//  }

//  @SuppressWarnings("unchecked")
  public static BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate> lessThan() {
    return (stack, cb) -> {
      Expression<Comparable<Object>> right = (Expression<Comparable<Object>>)stack.pop();
      Expression<Comparable<Object>> left = (Expression<Comparable<Object>>)stack.pop();
      return cb.lessThan(left, right);
    };
  }

//  @SuppressWarnings("unchecked")
//  public static BiFunction<Deque<Object>, CriteriaBuilder, Object> lessThanOrEqual() {
//    return (stack, cb) -> {
//      Comparable<Object> right = (Comparable<Object>) stack.pop();
//      Expression<Comparable<Object>> left = (Expression<Comparable<Object>>) stack.pop();
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
  
  public static Map<String, Function<Deque<String>, String>> defaultHqlMappers() {
    Map<String, Function<Deque<String>, String>> map = new HashMap<>();
    map.put(":", generateBinaryOperator("="));
    map.put("!", generateBinaryOperator("!="));
    map.put(">", generateBinaryOperator(">"));
    map.put("<", generateBinaryOperator("<"));
    map.put("AND", generateBinaryOperator("AND"));
    map.put("OR", generateBinaryOperator("OR"));
    return map;
  }
  
  static Function<Deque<String>, String> generateBinaryOperator(String symbol) {
    return (stack) -> {
      String right = stack.pop();
      String left = stack.pop();
      return format("(%s %s %s)", left, symbol, right);
    };
  }
}
