package io.dvp.jpa.filter.el;

import java.math.BigInteger;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegerOperand extends Operand {

  private static final Pattern pattern = Pattern.compile("^[0-9]+$");
  private BigInteger number;

  @Override
  public Optional<Symbol> copy(String exp) {
    if (pattern.matcher(exp).find()) {
      return Optional.of(new IntegerOperand(new BigInteger(exp)));
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
