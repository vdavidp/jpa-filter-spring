package io.dvp.ds;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpressionTree {
    private Symbol root;

    public static ExpressionTree build(String expression, Symbol...symbols) {
        Function<String, Optional<Symbol>> matches = s -> findSymbol(symbols, s);
        Symbol root = new TextSymbol(expression);
        String exp = "";
        int i = 0;

        for ( ; i < expression.length() - 1; i++) {
            if (expression.charAt(i) == ' ') {
                if (!"".equals(exp)) {
                    Optional<Symbol> node = matches.apply(exp);
                    if (node.isPresent()) {
                        root = root.merge(node.get());
                        exp = "";
                    }
                }
                continue;
            }
            exp += expression.charAt(i);
            Optional<Symbol> curr = matches.apply(exp);
            Optional<Symbol> next = matches.apply(exp + expression.charAt(i + 1));
            if (curr.isPresent() && !next.isPresent()) {
                root = root.merge(curr.get());
                exp = "";
            }
        }

        Optional<Symbol> node = matches.apply(exp + expression.substring(i));
        if (node.isPresent()) {
            root = root.merge(node.get());
        }

        return new ExpressionTree(root);
    }

    private static Optional<Symbol> findSymbol(Symbol[] symbols, String exp) {
        for (Symbol s: symbols) {
            if (s.matches(exp)) {
                return Optional.of(s.copy(exp));
            }
        }
        return Optional.empty();
    }

    public String toString() {
        return root.toString();
    }

}

@AllArgsConstructor
class TextSymbol implements Symbol {
    private String text;

    @Override
    public boolean matches(String symbol) {
        return true;
    }

    @Override
    public Symbol merge(Symbol s) {
        return s;
    }

    @Override
    public Symbol copy(String exp) {
        throw new RuntimeException("Method not implemented");
    }

    public String toString() {
        return text;
    }
}
