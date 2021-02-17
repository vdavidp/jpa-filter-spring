package io.dvp.ds;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class VariableOperand extends Operand {
    private static final Pattern variablePattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*$");
    private String value;

    @Override
    public Optional<Symbol> copy(String exp) {
        if (variablePattern.matcher(exp).find()) {
            return Optional.of(new VariableOperand(exp));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "[" + value + "]";
    }
}
