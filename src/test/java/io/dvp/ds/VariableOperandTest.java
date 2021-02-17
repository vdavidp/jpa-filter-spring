package io.dvp.ds;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class VariableOperandTest {
    VariableOperand var = new VariableOperand();

    @Test
    void copy() {
        assertValid("prop123");
        assertValid("Other");
        assertValid("moreThan4you");
        assertFalse(var.copy("123").isPresent());
        assertFalse(var.copy("3Three").isPresent());
        assertFalse(var.copy("_more").isPresent());
        assertFalse(var.copy("$following").isPresent());
    }

    private void assertValid(String prop) {
        Optional<Symbol> opt = var.copy(prop);
        assertTrue(opt.isPresent());
        assertNotSame(var, opt.get());
        assertEquals("[" + prop + "]", opt.get().toString());
    }
}
