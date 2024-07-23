package ca.mcscert.jpipe.tests.unit.errors;

import ca.mcscert.jpipe.compiler.exceptions.TypeError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@Disabled
public class MissingDependencyTest extends AbstractFaultyLoadingTest<TypeError> {
    @Override
    protected String source() {
        return "errors/missing_dependency.jd";
    }

    @Override
    protected Class<TypeError> expectedClass() {
        return TypeError.class;
    }

    @Test
    public void exceptionIsInTheRightScope() {
        assertNotNull(this.exception);
        TypeError e = (TypeError) this.exception;
        assertEquals("J4", e.getScopeIdentifier());
    }

    @Test
    public void messagePointsToTheRightReason() {
        assertNotNull(this.exception);
        TypeError e = (TypeError) this.exception;
        assertTrue(e.getMessage().contains("missing_pattern"));
    }


}
