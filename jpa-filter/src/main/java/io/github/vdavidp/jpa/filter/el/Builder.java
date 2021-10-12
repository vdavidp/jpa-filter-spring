/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import java.util.function.BiFunction;

/**
 *
 * @author david
 */
public interface Builder {
  Builder withReducer(BiFunction<String, ParenthesesCounter, ReducedPair> reducer);
  Builder withCounter(ParenthesesCounter counter);
  ReducedPair build();
}
