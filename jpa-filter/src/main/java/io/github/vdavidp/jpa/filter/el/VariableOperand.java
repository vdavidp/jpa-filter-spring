package io.github.vdavidp.jpa.filter.el;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class VariableOperand extends Operand {

  private static final Pattern pattern = Pattern.compile("^\\{([^{\\s}]+)}$");
  @Getter
  private String value;

  @Override
  public Optional<Symbol> copy(String exp, Map<ContextItem, Object> context) {
    Matcher matcher = pattern.matcher(exp);
    if (matcher.find() && matcher.groupCount() == 1) {
      return Optional.of(new VariableOperand(matcher.group(1)));
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
