package io.dvp.jpa.filter.el;

import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

public class FactoryOperatorTest {
    FactoryOperator op = new FactoryOperator(".", 50, singletonList(new DecimalFactory()));

    @Test
    void copy() {
        assertNotSame(op, op.copy(".").get());
        assertFalse(op.copy(" . ").isPresent());
        assertFalse(op.copy("a").isPresent());
        assertFalse(op.copy(",").isPresent());
    }

    @Test
    void merge() {
        Symbol left = new IntegerOperand().copy("938").get();
        Symbol right = new IntegerOperand().copy("32").get();

        assertSame(op, op.merge(left));

        Symbol other = op.merge(right);
        assertNotSame(other, op);
        assertEquals("[938.32]", other.toString());
    }

    @Test
    void mergeChained() {

    }
}
