package ca.mcscert.jpipe.tests.unit;

import ca.mcscert.jpipe.compiler.Compiler;
import ca.mcscert.jpipe.tests.helpers.ElementCounter;
import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.model.Unit;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class CompilerTest extends AbstractFileTest {

    protected String source() {
        return "simple.jd";
    }

    @Test
    public void unitHasTheSize(){
        assertEquals(1, unitUnderTest.getJustificationSet().size());
    }

    @Test
    public void modelConclusionIsTheRightOne(){
        JustificationDiagram justificationUnderTest =
                unitUnderTest.getJustificationSet().iterator().next();
        String label = justificationUnderTest.conclusion().getLabel();
        assertEquals("Model is correct", label);
    }

    @Test
    public void modelNameIsTheRightOne(){
        JustificationDiagram justificationUnderTest =
                unitUnderTest.getJustificationSet().iterator().next();
        String name = justificationUnderTest.name();
        assertEquals("prove_models", name);
    }

    @Test
    public void rightNumberOfEvidences(){
        JustificationDiagram justificationUnderTest =
                unitUnderTest.getJustificationSet().iterator().next();
        ElementCounter counter = new ElementCounter();
        justificationUnderTest.accept(counter);
        assertEquals(3, counter.getResult().get(ElementCounter.EVIDENCE));
    }

    @Test
    public void rightNumberOfJustifications(){
        ElementCounter counter = new ElementCounter();
        unitUnderTest.accept(counter);
        assertEquals(1, counter.getResult().get(ElementCounter.JUSTIFICATION));
    }

    @Test
    public void rightNumberOfSubConclusions(){
        ElementCounter counter = new ElementCounter();
        unitUnderTest.accept(counter);
        assertEquals(1, counter.getResult().get(ElementCounter.SUBCONCLUSION));
    }

    @Test
    public void rightNumberOfStrategies(){
        ElementCounter counter = new ElementCounter();
        unitUnderTest.accept(counter);
        assertEquals(2, counter.getResult().get(ElementCounter.STRATEGY));
    }


}

