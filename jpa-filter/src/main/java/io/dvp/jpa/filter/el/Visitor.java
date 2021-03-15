package io.dvp.jpa.filter.el;

public interface Visitor {

  void accept(BinaryOperator operator);

  void accept(VarcharOperand operand);

  void accept(VariableOperand operand);

  void accept(IntegerOperand operand);

  void accept(DecimalFactory operand);
}
