package io.dvp.ds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ExpressionTreeIT {

    @Test
    public void parseOneBinaryOperation() {
        ExpressionTree tree = ExpressionTree.build("2 : 3", new EqualsOperator(), new NumberOperand());
        assertEquals("[[2]:[3]]", tree.toString());
    }

    @Test
    public void parseTwoChainedBinaryOperations() {
        ExpressionTree tree = ExpressionTree.build("23 : 4 : 331", new EqualsOperator(), new NumberOperand());
        assertEquals("[[[23]:[4]]:[331]]", tree.toString());
    }
}
