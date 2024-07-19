package ca.mcscert.jpipe.tests.unit.errors;

import ca.mcscert.jpipe.compiler.exceptions.ParsingError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class MissingScopeTest extends AbstractFaultyLoadingTest<ParsingError> {
    @Override
    protected String source() {
        return "errors/missing_scope.jd";
    }

    @Override
    protected Class<ParsingError> expectedClass() {
        return ParsingError.class;
    }

    @Test
    public void exceptionIsAtTheRightLocation() {
        assertNotNull(this.exception);
        ParsingError e = (ParsingError) this.exception;
        assertEquals(1, e.line);
        assertEquals(0, e.column);
    }


}
