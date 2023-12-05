package ca.mcscert.jpipe.tests.unit.errors;

import ca.mcscert.jpipe.compiler.exceptions.TypeError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
public class MissingConclusionTest extends AbstractFaultyLoadingTest<TypeError> {
    @Override
    protected String source() {
        return "errors/missing_conclusion.jd";
    }

    @Override
    protected Class<TypeError> expectedClass() {
        return TypeError.class;
    }

    @Test
    public void exceptionIsInTheRightScope() {
        assertNotNull(this.exception);
        TypeError e = (TypeError) this.exception;
        assertEquals("j", e.getScopeIdentifier());
    }
}
