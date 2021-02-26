package io.dvp.ds.db;

import io.dvp.ds.el.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class DatabaseBinder<T> implements Visitor {

    private final Root<T> root;
    private final CriteriaBuilder cb;

    private Queue<Object> queue = new LinkedList<>();

    @Setter
    private Map<String, BiFunction<Queue<Object>, CriteriaBuilder, Predicate>> mappers;

    @Override
    public void accept(BinaryOperator operator) {
        mappers.get(operator.getSymbol()).apply(queue, cb);
    }

    @Override
    public void accept(VarcharOperand operand) {
        queue.add(operand.getValue());
    }

    @Override
    public void accept(VariableOperand operand) {
        queue.add(root.get(operand.getValue()));
    }
}
