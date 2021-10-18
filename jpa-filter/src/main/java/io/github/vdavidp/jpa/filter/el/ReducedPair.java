/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vdavidp.jpa.filter.el;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author david
 */
@RequiredArgsConstructor
@Getter
public class ReducedPair {
  private final Symbol symbol;
  private final ParenthesesCounter counter;
}
