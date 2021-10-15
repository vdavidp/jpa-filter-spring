/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

/**
 *
 * @author david
 */
public interface Visitor {

  default void accept(StringOperand operand) {}

  default void accept(VariableOperand operand) {}
  
  default void accept(NumberOperand operand) {}
  
  default void accept(DecimalOperand operand) {}

  default void accept(UnaryOperator operator) {}
  
  default void accept(BinaryOperator operator) {}
  
  default void accept(UuidOperand operand) {}
}
