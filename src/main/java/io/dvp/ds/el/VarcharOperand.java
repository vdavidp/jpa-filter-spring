package io.dvp.ds.el;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class VarcharOperand extends Operand {

    private static final Pattern pattern = Pattern.compile("^'((?:[^\\\\']|\\\\'|\\\\\\\\)*)'$");

    private String value;

    @Override
    public Optional<Symbol> copy(String exp) {
        Matcher matcher = pattern.matcher(exp);
        if (matcher.find() && matcher.groupCount() == 1) {
            String value = matcher.group(1).replace("\\'", "'").replace("\\\\", "\\");
            return Optional.of(new VarcharOperand(value));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "[" + value + "]";
    }
}
