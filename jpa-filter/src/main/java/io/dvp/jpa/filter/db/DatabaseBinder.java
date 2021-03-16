package io.dvp.jpa.filter.db;

import io.dvp.jpa.filter.el.BinaryOperator;
import io.dvp.jpa.filter.el.DecimalFactory;
import io.dvp.jpa.filter.el.IntegerOperand;
import io.dvp.jpa.filter.el.VarcharOperand;
import io.dvp.jpa.filter.el.VariableOperand;
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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.PluralAttribute.CollectionType;

public class DatabaseBinder<T> implements Binder {

  private Root<T> root;
  private final CriteriaQuery<T> query;
  private final CriteriaBuilder builder;
  private final Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> mappers;

  private final Deque<Object> deque = new LinkedList<>();

  private final Map<String, Join<T, ?>> joins = new HashMap<>();
  private Map<CollectionType, Function<String, Join<T, ?>>> joiners;
  private Subquery<T> subQuery;

  public DatabaseBinder(
      Root<T> root,
      CriteriaQuery<T> query,
      CriteriaBuilder builder,
      Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> mappers) {

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
    deque.addFirst(mappers.get(operator.getSymbol()).apply(deque, builder));
  }

  @Override
  public void accept(VarcharOperand operand) {
    deque.addFirst(operand.getValue());
  }

  @Override
  public void accept(VariableOperand operand) {
    String[] props = operand.getValue().split("\\.");
    if (props.length == 1) {
      deque.addFirst(root.get(operand.getValue()));
    } else if (props.length == 2) {
      initializeSubQuery();
      installJoin(props[0]);
      deque.addFirst(joins.get(props[0]).get(props[1]));
    } else {
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
  public void accept(IntegerOperand operand) {
    deque.addFirst(operand.getValue());
  }

  @Override
  public void accept(DecimalFactory operand) {
    deque.addFirst(operand.getValue());
  }

  @Override
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
