package io.dvp.ds;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CompoundVariableFactoryTest {
    CompoundVariableFactory var = new CompoundVariableFactory();

    @Test
    void copy() {
        assertValid("obj.Prop4you");
        assertValid("obj.prop1.innerProp");

        assertFalse(var.copy("prop1").isPresent());
        assertFalse(var.copy("123").isPresent());
        assertFalse(var.copy(" ").isPresent());
        assertFalse(var.copy("3Props").isPresent());
        assertFalse(var.copy("Other.").isPresent());
        assertFalse(var.copy(".invalid").isPresent());
    }

    private void assertValid(String prop) {
        Optional<Symbol> obj = var.copy(prop);
        assertTrue(obj.isPresent());
        assertNotSame(obj.get(), var);
        assertEquals("[" + prop + "]", obj.get().toString());
    }
}
