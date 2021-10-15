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

import io.github.vdavidp.jpa.filter.spring.example.Article;
import io.github.vdavidp.jpa.filter.el.Defaults;
import io.github.vdavidp.jpa.filter.el.ExpressionTree;
import io.github.vdavidp.jpa.filter.el.ParseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 *
 * @author david
 */
public class FieldExistingVerifierTest {
  
  private void checkField(String exp, String error) {
    FieldExistingVerifier verifier = new FieldExistingVerifier(Article.class);
    
    ExpressionTree et = new ExpressionTree(exp, Defaults.operands(), Defaults.operators());
    Throwable ex = assertThrows(ParseException.class, () -> et.visit(verifier));
    assertEquals(error, ex.getMessage());
  }
  
  private void assertValidField(String exp) {
    FieldExistingVerifier verifier = new FieldExistingVerifier(Article.class);
    ExpressionTree et = new ExpressionTree(exp, Defaults.operands(), Defaults.operators());
    et.visit(verifier);
  }
  
  @Test
  void throwsExceptionWhenNonExistingField() {
    checkField("nonExisting:'something'", "Field [nonExisting] does not exist");
  }
  
  @Test
  void throwsExceptionWhenNestedFieldNonExisting() {
    checkField("language.status:'Active'", "Field [status] does not exist");
  }
  
  @Test
  void throwsExceptionWhenNestedCollectionFieldNonExisting() {
    checkField("comments.something:'active'", "Field [something] does not exist");
  }
  
  @Test
  void noExceptionWhenExisting() {
    assertValidField("id:4");
  }
  
  @Test
  void noExceptionWhenNestedExisting() {
    assertValidField("language.name:'some name'");
  }
  
  @Test
  void noExceptionWhenNestedCollectionExisting() {
    assertValidField("comments.author:'david'");
  }
}
