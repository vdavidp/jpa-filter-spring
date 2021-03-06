package io.dvp.ds.db;

import io.dvp.ds.el.BinaryOperator;
import io.dvp.ds.el.VarcharOperand;
import io.dvp.ds.el.VariableOperand;
import lombok.RequiredArgsConstructor;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class DatabaseBinder<T> implements Binder {

    private final Root<T> root;
    private final CriteriaBuilder cb;
    private final Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> mappers;
    private Deque<Object> deque = new LinkedList<>();

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
