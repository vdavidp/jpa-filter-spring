package io.dvp.ds.el;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CompoundVariableFactory extends Operand {
    private static final Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*(\\.([a-zA-Z][a-zA-Z0-9]*))+$");
    private List<String> values;

    @Override
    public Optional<Symbol> copy(String exp) {
        if (pattern.matcher(exp).find()) {
            return Optional.of(new CompoundVariableFactory(Arrays.asList(exp.split("\\."))));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "[" + String.join(".", values) + "]";
    }
}
