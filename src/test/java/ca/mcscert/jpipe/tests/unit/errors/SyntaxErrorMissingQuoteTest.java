package ca.mcscert.jpipe.tests.unit.errors;

import ca.mcscert.jpipe.compiler.exceptions.LexingError;
import ca.mcscert.jpipe.compiler.exceptions.ParsingError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@Disabled
public class SyntaxErrorMissingQuoteTest extends AbstractFaultyLoadingTest<LexingError> {
    @Override
    protected String source() {
        return "errors/syntax_error.jd";
    }

    @Override
    protected Class<LexingError> expectedClass() {
        return LexingError.class;
    }

    @Test
    public void exceptionIsAtTheRightLocation() {
        assertNotNull(this.exception);
        LexingError e = (LexingError) this.exception;
        assertEquals(3, e.line);
        assertEquals(20, e.column);
    }

    @Test
    public void exceptionGivesTheRightAdviceToFix() {
        assertNotNull(this.exception);
        String msg = exception.getMessage().toLowerCase();
        System.out.println(msg);
        assertTrue(msg.contains("token"));
        assertTrue(msg.contains("recognition"));
    }

}
