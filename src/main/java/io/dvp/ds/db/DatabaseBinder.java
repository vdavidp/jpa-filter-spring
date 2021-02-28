package io.dvp.ds.db;

import io.dvp.ds.el.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class DatabaseBinder<T> implements Visitor {

    private final Root<T> root;
    private final CriteriaBuilder cb;

    private Deque<Object> deque = new LinkedList<>();

    @Setter
    private Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> mappers;

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

    public Predicate getPredicate() {
        return (Predicate) deque.removeFirst();
    }
}
