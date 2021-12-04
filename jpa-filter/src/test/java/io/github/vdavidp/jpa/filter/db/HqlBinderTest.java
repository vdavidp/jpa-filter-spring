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

import static io.github.vdavidp.jpa.filter.db.Mappers.defaultHqlMappers;
import io.github.vdavidp.jpa.filter.el.Defaults;
import io.github.vdavidp.jpa.filter.el.ExpressionTree;
import io.github.vdavidp.jpa.filter.el.ParseException;
import static java.util.Arrays.asList;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 *
 * @author david
 */
public class HqlBinderTest {
  
  @Test
  void generatesPredicate() {
    String text = "name:'david' AND amount>50.20 AND (uuid:603b3872-ca1b-4974-88d3-df738794e6e8 OR age<20)";
    ExpressionTree et = new ExpressionTree(text, Defaults.operands(), Defaults.operators());
    HqlBinder binder = new HqlBinder(defaultHqlMappers());
    
    et.visit(binder);
    assertEquals("(((name = 'david') AND (amount > 50.20)) AND ((uuid = cast('603b3872-ca1b-4974-88d3-df738794e6e8' as java.util.UUID)) OR (age < 20)))", 
        binder.getPredicate());
  }
  
  @Test
  void restrictVariableToWhiteList() {
    ExpressionTree et = new ExpressionTree("sale:34 AND amount>54.22 AND age<50", Defaults.operands(), Defaults.operators());
    HqlBinder binder = new HqlBinder(defaultHqlMappers(), new HashSet(asList("sale", "amount")));
    
    Throwable t = assertThrows(ParseException.class, () -> et.visit(binder));
    assertEquals("Variable age not recognized", t.getMessage());
  }
  
  @Test
  void mapVariableNames() {
    ExpressionTree et = new ExpressionTree("sale:34 AND amount > 55", Defaults.operands(), Defaults.operators());
    HqlBinder binder = new HqlBinder(defaultHqlMappers(), v -> {
      if ("sale".equals(v)) {
        return "s.id";
      } if ("amount".equals(v)) {
        return "s.amount";
      } else {
        throw new RuntimeException("Invalid prop");
      }
    });
    
    et.visit(binder);
    assertEquals("((s.id = 34) AND (s.amount > 55))", binder.getPredicate());
  }
  
  @Test
  void mapsAndRestricts() {
    ExpressionTree et = new ExpressionTree("sale:34 AND amount < 53", Defaults.operands(), Defaults.operators());
    HqlBinder binder = new HqlBinder(defaultHqlMappers(), new HashSet(asList("sale", "amount")), v -> {
      if ("sale".equals(v)) {
        return "entityId";
      } else {
        return v;
      }
    });
    
    et.visit(binder);
    assertEquals("((entityId = 34) AND (amount < 53))", binder.getPredicate());
  }

}