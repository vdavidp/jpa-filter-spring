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
package io.github.vdavidp.jpa.filter.el;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author david
 */
public class CloseParenthesesOperandTest {
  CloseParenthesesOperand proto = new CloseParenthesesOperand();
  
  @Test
  void rejectEmpty() {
    assertNull(proto.parse("", new ParenthesesCounter()));
  }
  
  @Test
  void acceptCloseParentheses() {
    ReducedPair pair = proto.parse(") )", new ParenthesesCounter(1, 2));
    assertTrue(pair.getSymbol() instanceof CloseParenthesesOperand);
    assertNotSame(pair.getSymbol(), proto);
    assertEquals(0, pair.getCounter().getCurrentCount());
  }
  
  @Test
  void rejectOpenParentheses() {
    assertNull(proto.parse("( ( (", new ParenthesesCounter()));
  }
}
