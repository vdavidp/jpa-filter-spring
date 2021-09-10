package io.github.vdavidp.jpa.filter.el;

import static lombok.AccessLevel.PRIVATE;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class VarcharOperand extends Operand {

  private static final Pattern pattern = Pattern.compile("^'((?:[^\\\\']|\\\\'|\\\\\\\\)*)'$");

  @Getter
  private String value;

  @Override
  public Optional<Symbol> copy(String exp, Map<ContextItem, Object> context) {
    Matcher matcher = pattern.matcher(exp);
    if (matcher.find() && matcher.groupCount() == 1) {
      String value = matcher.group(1).replace("\\'", "'").replace("\\\\", "\\");
      return Optional.of(new VarcharOperand(value));
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
