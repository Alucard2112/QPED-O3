package eu.qped.java.checkers.coverage.framework.ast;

import eu.qped.java.checkers.coverage.enums.StatementType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AstMethodTest {

    private class Parameters {
        private StatementType type;
        private List<String> content;

        public Parameters(StatementType type, List<String> content) {
            this.type = type;
            this.content = content;
        }
    }

    private AstMethod create(Parameters p) {
        return new AstMethod(p.type, "Class", "Method", 1, 2, p.content);
    }

    @Test
    public void constructor() {
        AstMethod toTest = create(new Parameters(StatementType.METHOD, List.of("MSG")));
        assertEquals(StatementType.METHOD, toTest.type());
        assertEquals("Class", toTest.className());
        assertEquals("Method", toTest.methodName());
        assertEquals(1, toTest.start());
        assertEquals(2, toTest.end());
        assertEquals(List.of("MSG"), toTest.content());
    }

    @Test
    public void constructorException() {
        Parameters[] nullPointerException = new Parameters[] {
                new Parameters(StatementType.IF, List.of("MSG")),
                new Parameters(StatementType.ELSE_IF, List.of("MSG")),
                new Parameters(StatementType.ELSE, List.of("MSG")),
                new Parameters(StatementType.CASE, List.of("MSG")),
                new Parameters(StatementType.WHILE, List.of("MSG")),
                new Parameters(StatementType.FOR, List.of("MSG")),
                new Parameters(StatementType.FOREACH, List.of("MSG")),
                new Parameters(StatementType.METHOD, null),
        };
        for (Parameters p : nullPointerException) {
            assertThrows(Exception.class, () -> {
                create(p);
            });
        }
    }

    @Test
    public void equalsTest() {
        Parameters d = new Parameters(StatementType.METHOD, List.of("MSG"));
        AstMethod a = create(d);
        AstMethod b = create(d);
        AstMethod c = create(d);
        assertEquals(a, b);
        assertEquals(a, c);
        assertEquals(b, c);
        assertNotEquals(a, create(new Parameters(StatementType.METHOD, List.of("MSG1"))));
    }


}