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

  void accept(StringOperand operand);

  void accept(VariableOperand operand);

  void accept(NumberOperand operand);

  void accept(DecimalOperand operand);

  void accept(UnaryOperator operator);
  
  void accept(BinaryOperator operator);
  
  
}
