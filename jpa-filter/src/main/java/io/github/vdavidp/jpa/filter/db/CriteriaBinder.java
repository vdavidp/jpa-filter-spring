package io.github.vdavidp.jpa.filter.db;

import io.github.vdavidp.jpa.filter.el.BinaryOperator;
import io.github.vdavidp.jpa.filter.el.DecimalOperand;
import io.github.vdavidp.jpa.filter.el.NumberOperand;
import io.github.vdavidp.jpa.filter.el.StringOperand;
import io.github.vdavidp.jpa.filter.el.UnaryOperator;
import io.github.vdavidp.jpa.filter.el.UuidOperand;
import io.github.vdavidp.jpa.filter.el.VariableOperand;
import io.github.vdavidp.jpa.filter.el.Visitor;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.PluralAttribute.CollectionType;

public class CriteriaBinder<T> implements Visitor {

  private Root<T> root;
  private final CriteriaQuery<T> query;
  private final CriteriaBuilder builder;
  private final Map<String, BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate>> mappers;

  private final Deque<Expression<?>> deque = new LinkedList<>();

  private final Map<String, Join<T, ?>> joins = new HashMap<>();
  private Map<CollectionType, Function<String, Join<T, ?>>> joiners;
  private Subquery<T> subQuery;

  public CriteriaBinder(
      Root<T> root,
      CriteriaQuery<T> query,
      CriteriaBuilder builder,
      Map<String, BiFunction<Deque<Expression<?>>, CriteriaBuilder, Predicate>> mappers) {

    this.root = root;
    this.query = query;
    this.builder = builder;
    this.mappers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    this.mappers.putAll(mappers);

    initializeJoiners();
  }

  private void initializeJoiners() {
    joiners = new HashMap<>();
    joiners.put(CollectionType.LIST, s -> root.join(root.getModel().getList(s)));
    joiners.put(CollectionType.MAP, s -> root.join(root.getModel().getMap(s)));
    joiners.put(CollectionType.SET, s -> root.join(root.getModel().getSet(s)));
    joiners.put(CollectionType.COLLECTION, s -> root.join(root.getModel().getCollection(s)));
  }

  @Override
  public void accept(BinaryOperator operator) {
    if (!mappers.containsKey(operator.getSymbol())) {
      throw new RuntimeException("Database binder not found for operator: " + operator.getSymbol());
    }
    deque.addFirst(mappers.get(operator.getSymbol()).apply(deque, builder));
  }

  @Override
  public void accept(StringOperand operand) {
    deque.addFirst(builder.literal(operand.getValue()));
  }

  @Override
  public void accept(VariableOperand operand) {
    String[] props = operand.getValue().split("\\.");
    switch (props.length) {
      case 1:
        deque.addFirst(root.get(operand.getValue()));
        break;
      case 2:
        initializeSubQuery();
        installJoin(props[0]);
        deque.addFirst(joins.get(props[0]).get(props[1]));
        break;
      default:
        throw new RuntimeException("No supported recursive join of depth 2");
    }
  }

  private void initializeSubQuery() {
    if (subQuery != null) {
      return;
    }

    @SuppressWarnings("unchecked")
    Class<T> clazz = (Class<T>) root.getJavaType();
    subQuery = query.subquery(clazz);
    root = subQuery.correlate(root);
  }

  private void installJoin(String prop) {
    if (joins.containsKey(prop)) {
      return;
    }

    Optional<Attribute<? super T, ?>> attribute = root.getModel().getAttributes().stream()
        .filter(attr -> attr.getName().equals(prop))
        .findFirst();

    if (attribute.isPresent() && attribute.get() instanceof PluralAttribute) {
      CollectionType type = ((PluralAttribute<?, ?, ?>) attribute.get()).getCollectionType();
      Join<T, ?> join = joiners.get(type).apply(prop);
      joins.put(prop, join);
    } else {
      Join<T, ?> join = root.join(root.getModel().getSingularAttribute(prop));
      joins.put(prop, join);
    }
  }

  @Override
  public void accept(NumberOperand operand) {
    deque.addFirst(builder.literal(operand.getValue()));
  }

  @Override
  public void accept(DecimalOperand operand) {
    deque.addFirst(builder.literal(operand.getValue()));
  }

  @Override
  public void accept(UnaryOperator operator) {
    deque.addFirst(mappers.get(operator.getSymbol()).apply(deque, builder));
  }
  
  @Override
  public void accept(UuidOperand operand) {
    deque.addFirst(builder.literal(operand.getValue()));
  }

  public Predicate getPredicate() {
    if (subQuery != null) {
      Predicate subPredicate = (Predicate) deque.removeFirst();
      subQuery.select(root).where(subPredicate);
      return builder.exists(subQuery);
    } else {
      return (Predicate) deque.removeFirst();
    }
  }
}
