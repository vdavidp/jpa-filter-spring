package io.dvp.ds.el;

public interface Visitor {

    void accept(BinaryOperator operator);

    void accept(VarcharOperand operand);

    void accept(VariableOperand operand);
}
