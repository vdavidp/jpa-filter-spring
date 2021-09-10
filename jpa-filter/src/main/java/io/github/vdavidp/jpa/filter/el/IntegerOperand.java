package io.github.vdavidp.jpa.filter.el;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegerOperand extends Operand {

  private static final Pattern pattern = Pattern.compile("^[0-9]+$");
  @Getter
  private BigInteger value;

  @Override
  public Optional<Symbol> copy(String exp, Map<ContextItem, Object> context) {
    if (pattern.matcher(exp).find()) {
      return Optional.of(new IntegerOperand(new BigInteger(exp)));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void visit(Visitor visitor) {
    visitor.accept(this);
  }

  @Override
  public String toString() {
    return "[" + value + "]";
  }
}
