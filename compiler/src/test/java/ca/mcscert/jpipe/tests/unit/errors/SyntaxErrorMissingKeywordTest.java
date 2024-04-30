package ca.mcscert.jpipe.tests.unit.errors;

import ca.mcscert.jpipe.compiler.exceptions.ParsingError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class SyntaxErrorMissingKeywordTest extends AbstractFaultyLoadingTest<ParsingError> {
    @Override
    protected String source() {
        return "errors/missing_keyword.jd";
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
        assertEquals(17, e.column);
    }

    @Test
    public void exceptionGivesTheRightAdviceToFix() {
        assertNotNull(this.exception);
        String msg = exception.getMessage().toLowerCase();
        assertTrue(msg.contains("'is'"));
        assertTrue(msg.contains("missing"));
    }

}
