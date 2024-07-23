package ca.mcscert.jpipe.tests.unit.errors;

import ca.mcscert.jpipe.compiler.Compiler;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@Disabled
public abstract class AbstractFaultyLoadingTest<T> {

    protected Exception exception;

    protected abstract String source();

    protected abstract Class<T> expectedClass();

    @BeforeEach
    public void loadUnit(){
        Compiler compiler = new Compiler();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(source())) {
            if (is != null) {
                compiler.compile(CharStreams.fromStream(is), source());
            } else {
                throw new FileNotFoundException(source());
            }
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Test
    public void exceptionWasThrown() {
        assertNotNull(exception);
    }

    @Test
    public void exceptionIsOfTheRightType() {
        if (exception == null) {
            fail("No exception was thrown");
        }
        assertEquals(expectedClass(), exception.getClass());
    }

}
