/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import io.github.vdavidp.jpa.filter.el.UnaryOperator.Order;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author david
 */
@Disabled
public class UnaryOperatorTest {
  
  
  private Operand number(String text) {
    return new NumberOperand(new BigInteger(text));
  }
  

}
