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

  public static class Builder {
    private EnumMap<ContextItem, Object> context;
    private int multiplier = 1;

    public ExpressionTree build(String expression, Symbol... symbols) {
      context = new EnumMap<>(ContextItem.class);
      context.put(ContextItem.MULTIPLIER, multiplier);

      Symbol root = new NullSymbol();
      String subExpr = "";

      int i = 0;
      for (; i < expression.length() - 1; i++) {

        if (expression.charAt(i) == '(') {
          context.put(ContextItem.MULTIPLIER, ++multiplier);
          continue;
        } else if (expression.charAt(i) == ')') {
          context.put(ContextItem.MULTIPLIER, --multiplier);
          continue;
        }

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

    private Optional<Symbol> findMatch(String exp, Symbol[] symbols) {
      List<Symbol> result = Arrays.stream(symbols)
          .map(s -> s.copy(exp, context))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .collect(toList());
      return result.size() > 0 ? Optional.of(result.get(0)) : Optional.empty();
    }
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
        new RightUnaryOperator("Is True", 40),
        new RightUnaryOperator("Is False", 40),
        new RightUnaryOperator("Is Null", 40),
        new RightUnaryOperator("Is Not Null", 40),
        new BinaryOperator(">", 30),
        new BinaryOperator(">=", 30),
        new BinaryOperator("<", 30),
        new BinaryOperator("<=", 30)
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
    public Optional<Symbol> copy(String symbol, Map<ContextItem, Object> context) {
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
