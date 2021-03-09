package io.dvp.jpa.filter.db;

import io.dvp.jpa.filter.el.BinaryOperator;
import io.dvp.jpa.filter.el.VarcharOperand;
import io.dvp.jpa.filter.el.VariableOperand;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiFunction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DatabaseBinder<T> implements Binder {

  private final Root<T> root;
  private final CriteriaBuilder cb;
  private final Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> mappers;
  private final Deque<Object> deque = new LinkedList<>();

  @Override
  public void accept(BinaryOperator operator) {
    deque.addFirst(mappers.get(operator.getSymbol()).apply(deque, cb));
  }

  @Override
  public void accept(VarcharOperand operand) {
    deque.addFirst(operand.getValue());
  }

  @Override
  public void accept(VariableOperand operand) {
    deque.addFirst(root.get(operand.getValue()));
  }

  @Override
  public Predicate getPredicate() {
    return (Predicate) deque.removeFirst();
  }
}
