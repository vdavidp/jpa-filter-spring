package com.github.vdavidp.jpa.filter.el;

public interface Visitor {

  void accept(BinaryOperator operator);

  void accept(VarcharOperand operand);

  void accept(VariableOperand operand);

  void accept(IntegerOperand operand);

  void accept(DecimalFactory operand);

  void accept(UnaryOperator operator);
}