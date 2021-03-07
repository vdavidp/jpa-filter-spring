package io.dvp.jpa.filter.el;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class VariableOperandTest {
    VariableOperand var = new VariableOperand();

    @Test
    void copy() {
        assertValid("{prop123}");
        assertValid("{Other}");
        assertValid("{_12moreThan4you}");
        assertValid("{$hello}");
        assertValid("{some-variable}");
        assertValid("{inside/almost-anything.is=valid}");

        assertFalse(var.copy("123").isPresent());
        assertFalse(var.copy("3Three").isPresent());
        assertFalse(var.copy("_more").isPresent());
        assertFalse(var.copy("$following").isPresent());
        assertFalse(var.copy("{symbols { and } not allowed inside}").isPresent());
        assertFalse(var.copy("{}").isPresent());
        assertFalse(var.copy("{ }").isPresent());
        assertFalse(var.copy("{\t}").isPresent());
    }

    private void assertValid(String prop) {
        Optional<Symbol> opt = var.copy(prop);
        assertTrue(opt.isPresent());
        assertNotSame(var, opt.get());
        assertEquals("[" + prop.replaceAll("[{}]", "") + "]", opt.get().toString());
    }
}
