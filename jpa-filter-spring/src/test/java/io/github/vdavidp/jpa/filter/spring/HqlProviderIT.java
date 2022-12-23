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

import io.github.vdavidp.jpa.filter.el.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 *
 * @author david
 */
@DataJpaTest
@Import(JpaFilterAutoconfigure.class)
public class HqlProviderIT {
  
  @PersistenceContext
  EntityManager entityManager;    
  
  @Autowired
  HqlProvider hqlProvider;
  
  @Test
  void withFilter() {
    Set<String> validNames = new HashSet<>();
    validNames.add("id");
    validNames.add("article.title");
    
    Function<String, String> nameMapper = (n) -> "c." + n;
    
    String query = "select c.author from Comment c where " + 
        hqlProvider.create("id:2 AND article.title:'Article 2'", validNames, nameMapper);
    Query q = entityManager.createQuery(query);
    String name = (String)q.getSingleResult();
    
    assertEquals("john", name);
  }
  
  @Test
  void nullPointerWithoutFilter() {
    assertThrows(NullPointerException.class, () -> hqlProvider.create(null, new HashSet(), n -> n));
  }
  
  @Test 
  void parseExceptionWhenEmptyFilter() {
    assertThrows(ParseException.class, () -> hqlProvider.create("", new HashSet(), n -> n));
  }
}
