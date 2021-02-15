package io.dvp.ds;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class DotOperator implements Symbol {
    private static Pattern extractor = Pattern.compile("^\\[(.+)]$");
    @Getter
    private final int weight;
    private Symbol left, right;

    @Override
    public Symbol merge(Symbol s) {
        if (left == null) {
            left = s;
            return this;
        } else {
            right = s;
            return new DecimalOperand().copy(findRaw(left) + "." + findRaw(s)).get();
        }
    }

    private String findRaw(Symbol s) {
        Matcher match = extractor.matcher(s.toString());
        if (!match.find()) {
            throw new RuntimeException("Not able to get raw symbol of " + s);
        }
        return match.group(1);
    }

    @Override
    public Optional<Symbol> copy(String symbol) {
        if (".".equals(symbol)) {
            return Optional.of(new DotOperator(weight));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "{" + left + "." + right + "}";
    }
}
