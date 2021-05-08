package io.dvp.jpa.filter.el;

import static io.dvp.jpa.filter.el.Symbol.WEIGHT_10;
import static io.dvp.jpa.filter.el.Symbol.WEIGHT_20;
import static io.dvp.jpa.filter.el.Symbol.WEIGHT_30;
import static io.dvp.jpa.filter.el.Symbol.WEIGHT_40;
import static io.dvp.jpa.filter.el.Symbol.WEIGHT_MAX;
import static io.dvp.jpa.filter.el.Symbol.WEIGHT_MIN;
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
    private int multiplier = WEIGHT_MIN;

    public ExpressionTree build(String expression, Symbol... symbols) {
      context = new EnumMap<>(ContextItem.class);
      context.put(ContextItem.MULTIPLIER, multiplier);

      Symbol root = new NullSymbol();
      String subExpr = "";

      int i = 0;
      for (; i < expression.length() - 1; i++) {

        if (tryIncreaseMultiplier(expression.charAt(i))
            || tryDecreaseMultiplier(expression.charAt(i))) {
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
      } //TODO If remaining exp is not recognized then throw exception

      return new ExpressionTree(root);
    }

    private boolean tryIncreaseMultiplier(char character) {
      return modifyMultiplier(character, '(', +WEIGHT_MAX);
    }

    private boolean tryDecreaseMultiplier(char character) {
      return modifyMultiplier(character, ')', -WEIGHT_MAX);
    }

    private boolean modifyMultiplier(char character, char symbol, int offset) {
      if (symbol == character) {
        multiplier += offset;
        context.put(ContextItem.MULTIPLIER, multiplier);
        return true;
      } else {
        return false;
      }
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
        new BinaryOperator("=", WEIGHT_20),
        new BinaryOperator("and", WEIGHT_10),
        new BinaryOperator("or", WEIGHT_10),
        new RightUnaryOperator("Is True", WEIGHT_40),
        new RightUnaryOperator("Is False", WEIGHT_40),
        new RightUnaryOperator("Is Null", WEIGHT_40),
        new RightUnaryOperator("Is Not Null", WEIGHT_40),
        new BinaryOperator(">", WEIGHT_30),
        new BinaryOperator(">=", WEIGHT_30),
        new BinaryOperator("<", WEIGHT_30),
        new BinaryOperator("<=", WEIGHT_30)
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
