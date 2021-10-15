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
package io.github.vdavidp.jpa.filter.spring.visitor;

import io.github.vdavidp.jpa.filter.el.ParseException;
import io.github.vdavidp.jpa.filter.el.VariableOperand;
import io.github.vdavidp.jpa.filter.el.Visitor;
import static java.lang.String.format;
import java.lang.reflect.Field;
import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.core.ResolvableType;

/**
 *
 * @author david
 */
@AllArgsConstructor
public class FieldExistingVerifier implements Visitor {
  private Class<?> entity;

  @Override
  public void accept(VariableOperand operand) {
    String[] value = operand.getValue().split("\\.");
    
    Field f = findField(entity, value[0]);
    if (value.length > 1) {
      if (Collection.class.isAssignableFrom(f.getType())) {
        ResolvableType col = ResolvableType.forField(f, entity);
        Class<?> type = col.resolveGeneric();
        findField(type, value[1]);
      } else {
        findField(f.getType(), value[1]);
      }
    }
  }
  
  private Field findField(Class<?> type, String field) {      
    for (Field f : type.getDeclaredFields()) {
      if (f.getName().equals(field)) {
        return f;
      }
    }
    
    if (type.getSuperclass() != null) {
      return findField(type.getSuperclass(), field);
    } else {
      throw new ParseException(format("Field [%s] does not exist", field));
    }
  }
  
  
  
}
