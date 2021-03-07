package io.dvp.jpa.filter.el;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class DecimalFactory extends Operand {

    private static final Pattern pattern = Pattern.compile("^[0-9]+\\.[0-9]+$");
    private BigDecimal number;

    @Override
    public Optional<Symbol> copy(String exp) {
        if (pattern.matcher(exp).find()) {
            return Optional.of(new DecimalFactory(new BigDecimal(exp)));
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
        return "[" + number + "]";
    }
}
