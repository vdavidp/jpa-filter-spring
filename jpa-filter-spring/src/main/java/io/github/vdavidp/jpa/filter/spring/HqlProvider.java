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
package io.github.vdavidp.jpa.filter.spring;

import io.github.vdavidp.jpa.filter.db.HqlBinder;
import io.github.vdavidp.jpa.filter.db.Mappers;
import io.github.vdavidp.jpa.filter.el.Defaults;
import io.github.vdavidp.jpa.filter.el.ExpressionTree;
import io.github.vdavidp.jpa.filter.el.Operand;
import io.github.vdavidp.jpa.filter.el.Operator;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;

/**
 *
 * @author david
 */
@AllArgsConstructor
public class HqlProvider {
  private HqlConfigurator configurator;
  
  public String create(String filter) {
    List<Operand> operands = new ArrayList<>(Defaults.operands());
    List<Operator> operators = new ArrayList<>(Defaults.operators());
    
    Function<String, String> operatorMapper = Mappers.defaultHqlMappers();
    if (configurator != null) {
      configurator.modifyOperands(operands);
      configurator.modifyOperators(operators);
      operatorMapper = configurator.modifyMappers(operatorMapper);
    }
    
    ExpressionTree et = new ExpressionTree(filter, operands, operators);
    
    HqlBinder binder = new HqlBinder(operatorMapper);
    et.visit(binder);
    return binder.getPredicate();
  }
}
