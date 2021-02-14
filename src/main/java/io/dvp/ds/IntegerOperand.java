package io.dvp.ds;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.regex.Pattern;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegerOperand implements Symbol {
    private static Pattern integerPtrn = Pattern.compile("[0-9]+");
    private BigInteger number;

    @Override
    public boolean matches(String symbol) {
        return integerPtrn.matcher(symbol).matches();
    }

    @Override
    public Symbol merge(Symbol s) {
        s.merge(this);
        return s;
    }

    @Override
    public Symbol copy(String exp) {
        return new IntegerOperand(new BigInteger(exp));
    }

    @Override
    public String toString() {
        return "[" + number + "]";
    }
}
