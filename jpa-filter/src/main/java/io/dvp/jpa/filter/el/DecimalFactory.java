package io.dvp.jpa.filter.el;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class DecimalFactory extends Operand {

  private static final Pattern pattern = Pattern.compile("^[0-9]+\\.[0-9]+$");
  @Getter
  private BigDecimal value;

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
    visitor.accept(this);
  }

  @Override
  public String toString() {
    return "[" + value + "]";
  }
}
