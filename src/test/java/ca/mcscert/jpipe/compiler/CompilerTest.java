package ca.mcscert.jpipe.compiler;

import ca.mcscert.jpipe.compiler.visitors.ElementCounter;
import ca.mcscert.jpipe.model.Justification;
import ca.mcscert.jpipe.model.Unit;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

public class CompilerTest {

    private Unit unitUnderTest;
    private String implContent;
    private static final String SOURCE = "simple.jd";
    private static final String IMPL_SOURCE = "with_impl.jd";

    @BeforeEach
    public void loadUnit(){
        Compiler compiler = new Compiler();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(SOURCE)) {
            unitUnderTest = compiler.compile(CharStreams.fromStream(is), SOURCE);
        } catch (IOException ioe) {
            fail("Unexpected exception was thrown");
        }
        try (InputStream is = classloader.getResourceAsStream(IMPL_SOURCE)) {
            implContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
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

    @Test //test case 1
    public void rightNumberOfEvidences(){
        Justification justificationUnderTest = unitUnderTest.getJustificationSet().iterator().next();
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
    
    
    @Test
    public void checkAbstractElements() {
        String patternContent = ""; 
        String patternSource = "patterns.jd";
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(patternSource)) {
            patternContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int patternCount = StringUtils.countMatches(patternContent, " pattern ");
        System.out.println("Pattern count: " + patternCount);
        assertEquals(1, patternCount);
    }
    
    @Test
    public void checkJustificationImplementation() {
        Justification justificationUnderTest = unitUnderTest.getJustificationSet().iterator().next();
        String justificationName = justificationUnderTest.getName();
        System.out.println("Justification name: " + justificationName);
        System.out.println(implContent);
        assertTrue(implContent.contains(justificationName));
    }
    @Test
    public void countOperations() {
        // Count the number of "operation" occurrences in implContent
        int operationCount = StringUtils.countMatches(implContent, "operation");
        System.out.println("Number of operations: " + operationCount);


        assertEquals(2, operationCount); 
    }

    @Test
    public void countProbes() {
        // Count the number of "probe" occurrences in implContent
        int probeCount = StringUtils.countMatches(implContent, "probe");
        System.out.println("Number of probes: " + probeCount);
        assertEquals(3, probeCount); 
    }


}
