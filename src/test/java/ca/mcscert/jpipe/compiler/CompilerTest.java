package ca.mcscert.jpipe.compiler;

import ca.mcscert.jpipe.compiler.visitors.EvidenceCounter;
import ca.mcscert.jpipe.model.Justification;
import ca.mcscert.jpipe.model.Unit;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class CompilerTest {

    private Unit unitUnderTest;
    private static final String SOURCE = "simple.jd";

    @BeforeEach
    public void loadUnit(){
        Compiler compiler = new Compiler();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(SOURCE)) {
            unitUnderTest = compiler.compile(CharStreams.fromStream(is), SOURCE);
        } catch (IOException ioe) {
            fail("Unexpected exception was thrown");
        }
    }

    @Test
    public void unitHasTheSize(){
        assertEquals(1, unitUnderTest.getJustificationSet().size());
    }

    @Test
    public void modelConclusionIsTheRightOne(){
        Justification justificationUnderTest = unitUnderTest.getJustificationSet().iterator().next();
        String label = justificationUnderTest.getConclusion().getLabel();
        assertEquals("Model is correct", label);
    }

    @Test
    public void modelNameIsTheRightOne(){
        Justification justificationUnderTest = unitUnderTest.getJustificationSet().iterator().next();
        String name = justificationUnderTest.getName();
        assertEquals("prove_models", name);
    }

    @Test
    public void rightNumberOfEvidences(){
        Justification justificationUnderTest = unitUnderTest.getJustificationSet().iterator().next();
        EvidenceCounter counter = new EvidenceCounter();
        justificationUnderTest.accept(counter);
        assertEquals(3, counter.getResult());
    }



}
