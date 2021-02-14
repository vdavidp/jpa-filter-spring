package io.dvp.ds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EqualsOperatorTest {
    EqualsOperator equalsOperator = new EqualsOperator();

    @Test
    public void getSymbol() {
        assertEquals(":", equalsOperator.getSymbol());
    }

    @Test
    public void copy() {
        Symbol s = equalsOperator.copy("");
        assertNotNull(s);
        assertNotEquals(s, equalsOperator);
        assertSame(s.getClass(), equalsOperator.getClass());
    }
}
