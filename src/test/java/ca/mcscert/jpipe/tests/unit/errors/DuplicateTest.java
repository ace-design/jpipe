package ca.mcscert.jpipe.tests.unit.errors;

import ca.mcscert.jpipe.compiler.exceptions.CompilationError;
import ca.mcscert.jpipe.compiler.exceptions.ParsingError;
import ca.mcscert.jpipe.compiler.exceptions.TypeError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class DuplicateTest extends AbstractFaultyLoadingTest<TypeError> {
    @Override
    protected String source() {
        return "errors/duplicate.jd";
    }

    @Override
    protected Class<TypeError> expectedClass() {
        return TypeError.class;
    }

    @Test
    public void exceptionIndicatesTheDuplicationIssue() {
        assertNotNull(this.exception);
        TypeError e = (TypeError) this.exception;
        assertTrue(e.getMessage().toLowerCase().contains("duplicate"));
        assertTrue(e.getMessage().contains("dupli_diag"));
    }

    @Test
    public void exceptionIsAtTheRightLocation() {
        assertNotNull(this.exception);
        TypeError e = (TypeError) this.exception;
        assertTrue(e.getScopeIdentifier().contains("duplicate.jd"));
    }

}
