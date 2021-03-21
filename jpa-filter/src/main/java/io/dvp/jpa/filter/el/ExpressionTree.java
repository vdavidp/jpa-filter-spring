package io.dvp.jpa.filter.el;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpressionTree {

  private final Symbol root;

  public static ExpressionTree build(String expression, Symbol... symbols) {
    Symbol root = new NullSymbol();
    String subExpr = "";
    int i = 0;
    for (; i < expression.length() - 1; i++) {
      subExpr += expression.charAt(i);
      Optional<Symbol> currMatch = findMatch(subExpr, symbols);
      Optional<Symbol> nextMatch = findMatch(subExpr + expression.charAt(i + 1), symbols);
      if (currMatch.isPresent() && !nextMatch.isPresent()) {
        root = root.merge(currMatch.get());
        subExpr = "";
      }
    }

    Optional<Symbol> match = findMatch(subExpr + expression.substring(i), symbols);
    if (match.isPresent()) {
      root = root.merge(match.get());
    }
    return new ExpressionTree(root);
  }

  private static Optional<Symbol> findMatch(String exp, Symbol[] symbols) {
    List<Symbol> result = Arrays.stream(symbols)
        .map(s -> s.copy(exp, null))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toList());
    return result.size() > 0 ? Optional.of(result.get(0)) : Optional.empty();
  }

  public static Symbol[] defaultSymbols() {
    return new Symbol[] {
        new IntegerOperand(),
        new FactoryOperator(".", 50, singletonList(new DecimalFactory())),
        new VariableOperand(),
        new VarcharOperand(),
        new BinaryOperator("=", 20),
        new BinaryOperator("and", 10),
        new BinaryOperator("or", 10),
        new RightUnaryOperator("Is True", 30),
        new RightUnaryOperator("Is False", 30),
        new RightUnaryOperator("Is Null", 30),
        new RightUnaryOperator("Is Not Null", 30)
    };
  }

  public void visit(Visitor v) {
    root.visit(v);
  }

  @Override
  public String toString() {
    return root.toString();
  }

  private static class NullSymbol implements Symbol {

    @Override
    public Symbol merge(Symbol s) {
      return s;
    }

    @Override
    public Optional<Symbol> copy(String symbol, EnumMap<ContextItem, Object> context) {
      throw new RuntimeException("Not implemented");
    }

    @Override
    public int getWeight() {
      throw new RuntimeException();
    }

    @Override
    public void visit(Visitor visitor) {
      throw new RuntimeException("Not implemented");
    }
  }
}
