package io.dvp.ds.el;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class VariableOperand extends Operand {

    private static final Pattern pattern = Pattern.compile("^\\{([^{\\s}]+)}$");
    private String value;

    @Override
    public Optional<Symbol> copy(String exp) {
        Matcher matcher = pattern.matcher(exp);
        if (matcher.find() && matcher.groupCount() == 1) {
            return Optional.of(new VariableOperand(matcher.group(1)));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "[" + value + "]";
    }
}
