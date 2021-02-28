package io.dvp.ds.el;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class VarcharOperandTest {
    final VarcharOperand factory = new VarcharOperand();

    @Test
    void copy() {
        assertValid("'Some Text'", "[Some Text]");
        assertValid("''", "[]");
        assertValid("'That\\'s'", "[That's]");
        assertValid("'ooo\\'\\'s'", "[ooo''s]");
        assertValid("'\\\\'", "[\\]");

        assertFalse(factory.copy("'''").isPresent());
        assertFalse(factory.copy("'Not valid").isPresent());
        assertFalse(factory.copy("other'").isPresent());
        assertFalse(factory.copy("without quotes").isPresent());
        assertFalse(factory.copy("123").isPresent());
        assertFalse(factory.copy("'didi''d").isPresent());
        assertFalse(factory.copy("'").isPresent());
    }

    void assertValid(String rawSymbol, String interpretedSymbol) {
        Optional<Symbol> obj = factory.copy(rawSymbol);
        assertTrue(obj.isPresent());
        assertNotSame(factory, obj.get());
        assertEquals(interpretedSymbol, obj.get().toString());
    }
}
