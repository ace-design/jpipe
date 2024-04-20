package ca.mcscert.jpipe.tests.unit;

import ca.mcscert.jpipe.compiler.Compiler;
import ca.mcscert.jpipe.model.Unit;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class AbstractFileTest {

    protected Unit unitUnderTest;

    @BeforeEach
    public void loadUnit(){
        Compiler compiler = new Compiler();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(source())) {
            unitUnderTest = compiler.compile(CharStreams.fromStream(is), source());
        } catch (IOException ioe) {
            fail("Unexpected exception was thrown");
        }
    }

    protected abstract String source();

}
