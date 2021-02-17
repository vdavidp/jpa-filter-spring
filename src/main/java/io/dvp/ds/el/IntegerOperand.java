package io.dvp.ds.el;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Optional;
import java.util.regex.Pattern;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegerOperand extends Operand {
    private static Pattern integerPattern = Pattern.compile("^[0-9]+$");
    private BigInteger number;

    @Override
    public Optional<Symbol> copy(String exp) {
        if (integerPattern.matcher(exp).find()) {
            return Optional.of(new IntegerOperand(new BigInteger(exp)));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "[" + number + "]";
    }
}
