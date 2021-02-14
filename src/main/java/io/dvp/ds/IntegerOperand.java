package io.dvp.ds;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Optional;
import java.util.regex.Pattern;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegerOperand implements Symbol {
    private static Pattern integerPtrn = Pattern.compile("[0-9]+");
    private BigInteger number;

    @Override
    public Symbol merge(Symbol s) {
        s.merge(this);
        return s;
    }

    @Override
    public Optional<Symbol> copy(String exp) {
        if (integerPtrn.matcher(exp).matches()) {
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
