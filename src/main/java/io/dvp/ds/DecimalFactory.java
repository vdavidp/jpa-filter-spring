package io.dvp.ds;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class DecimalFactory extends Operand {
    private static Pattern decimalPattern = Pattern.compile("^[0-9]+\\.[0-9]+$");
    private BigDecimal number;

    @Override
    public Optional<Symbol> copy(String exp) {
        if (decimalPattern.matcher(exp).find()) {
            return Optional.of(new DecimalFactory(new BigDecimal(exp)));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "[" + number + "]";
    }
}
