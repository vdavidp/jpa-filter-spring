/*
 * The MIT License
 *
 * Copyright 2021 david.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.vdavidp.jpa.filter.db;

import io.github.vdavidp.jpa.filter.el.BinaryOperator;
import io.github.vdavidp.jpa.filter.el.DecimalOperand;
import io.github.vdavidp.jpa.filter.el.NumberOperand;
import io.github.vdavidp.jpa.filter.el.ParseException;
import io.github.vdavidp.jpa.filter.el.StringOperand;
import io.github.vdavidp.jpa.filter.el.UnaryOperator;
import io.github.vdavidp.jpa.filter.el.UuidOperand;
import io.github.vdavidp.jpa.filter.el.VariableOperand;
import io.github.vdavidp.jpa.filter.el.Visitor;
import static java.lang.String.format;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 *
 * @author david
 */
public class HqlBinder implements Visitor {
  private Deque<String> stack = new ArrayDeque<>();
  
  private Set<String> validVariables = new HashSet() {
    @Override
    public boolean contains(Object o) {
      return true;
    }
  };
  
  private Function<String, String> mapName = n -> n;
  
  private final Function<String, String> operatorMapper;
  
  public HqlBinder(Function<String, String> operatorMapper) {
    this.operatorMapper = operatorMapper;
  }
  
  public HqlBinder(Function<String, String> operatorMapper, Set<String> validVariables) {
    this(operatorMapper);
    this.validVariables = validVariables;
  }
  
  public HqlBinder(Function<String, String> operatorMapper, Function<String, String> mapName) {
    this(operatorMapper);
    this.mapName = mapName;
  }
  
  public HqlBinder(Function<String, String> operatorMapper, Set<String> validVariables, Function<String, String> mapName) {
    this(operatorMapper, validVariables);
    this.mapName = mapName;
  }
  
  public void accept(StringOperand operand) {
    stack.push("'" + operand.getValue() + "'");
  }

  public void accept(VariableOperand operand) {
    if (!validVariables.contains(operand.getValue())) {
      throw new ParseException("Variable " + operand.getValue() + " not recognized");
    }
    
    stack.push(mapName.apply(operand.getValue()));
  }
  
  public void accept(NumberOperand operand) {
    stack.push(operand.getValue().toString());
  }
  
  public void accept(DecimalOperand operand) {
    stack.push(operand.getValue().toString());
  }

  public void accept(UnaryOperator operator) {
    throw new UnsupportedOperationException();
  }
  
  public void accept(BinaryOperator operator) {
    String right = stack.pop();
    String left = stack.pop();
    stack.push(format("(%s %s %s)", left, operatorMapper.apply(operator.getSymbol()), right));
  }
  
  public void accept(UuidOperand operand) {
    stack.push(format("cast('%s' as java.util.UUID)", operand.getValue()));
  }
  
  public String getPredicate() {
    return stack.pop();
  }
}
