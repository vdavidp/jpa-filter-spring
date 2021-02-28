package io.dvp.ds.el;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class FactoryOperator implements Symbol {

    private static final Pattern extractorRegex = Pattern.compile("^\\[(.+)]$");

    private final String symbol;
    @Getter
    private final int weight;
    private final List<Symbol> symbols;

    private Symbol left, right;

    @Override
    public Symbol merge(Symbol s) {
        if (left == null) {
            left = s;
            return this;
        } else {
            right = s;
            return findSymbol(findRawContent(left) + "." + findRawContent(s));
        }
    }

    private Symbol findSymbol(String exp) {
        List<Symbol> result = symbols.stream()
                .map(s -> s.copy(exp))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        if (result.size() != 1) {
            throw new RuntimeException("Symbols found for " + exp + ": " +
                    result.stream().map(Symbol::toString).collect(toList()));
        }
        return result.get(0);
    }

    private String findRawContent(Symbol s) {
        Matcher match = extractorRegex.matcher(s.toString());
        if (!match.find()) {
            throw new RuntimeException("Not able to get raw symbol of " + s);
        }
        return match.group(1);
    }

    @Override
    public Optional<Symbol> copy(String exp) {
        if (symbol.equals(exp)) {
            return Optional.of(new FactoryOperator(symbol, weight, symbols));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void visit(Visitor visitor) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String toString() {
        return "{" + left + "." + right + "}";
    }
}
