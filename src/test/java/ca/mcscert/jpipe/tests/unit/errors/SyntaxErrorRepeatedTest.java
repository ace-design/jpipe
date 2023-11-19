package ca.mcscert.jpipe.tests.unit.errors;

import ca.mcscert.jpipe.compiler.exceptions.LexingError;
import ca.mcscert.jpipe.compiler.exceptions.ParsingError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class SyntaxErrorRepeatedTest extends AbstractFaultyLoadingTest<ParsingError> {
    @Override
    protected String source() {
        return "errors/repeated_keywords.jd";
    }

    @Override
    protected Class<ParsingError> expectedClass() {
        return ParsingError.class;
    }

    @Test
    public void exceptionIsAtTheRightLocation() {
        assertNotNull(this.exception);
        ParsingError e = (ParsingError) this.exception;
        assertEquals(3, e.line);
        assertEquals(13, e.column);
    }

    @Test
    public void exceptionGivesTheRightAdviceToFix() {
        assertNotNull(this.exception);
        String msg = exception.getMessage().toLowerCase();
        System.out.println(exception);
        assertTrue(msg.contains("extraneous"));
        assertTrue(msg.contains("evidence"));
        assertTrue(msg.contains("expecting"));
        assertTrue(msg.contains("id"));
    }

}
