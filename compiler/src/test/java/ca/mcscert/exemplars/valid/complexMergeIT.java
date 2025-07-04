package ca.mcscert.exemplars.valid;

import ca.mcscert.exemplars.Counter;
import ca.mcscert.exemplars.SourceFileTest;
import ca.mcscert.jpipe.model.elements.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Source file: src/test/resources/valid/complexMerge.jd")
public class complexMergeIT extends SourceFileTest {

    @Override
    protected String source() { return "valid/complexMerge.jd";}

    @Test
    public void containsTheRightModels(){
        Counter counter = new Counter();
        this.unit.accept(counter);
        assertEquals(3, unit.getContents().size());
    }

    @Nested
    @DisplayName("Justification 'j1'")
    class j1JustificationTest {

        @Test
        public void containsTheRightModels(){ assertTrue(unit.exists("j1")); }

        @Test
        public void modelsHavetheRightType() { assertDoesNotThrow(() -> (Justification) unit.get("j1")); }

        @Test
        public void justificationContainsRightElements() {
            Counter counter = new Counter();
            unit.get("j1").accept(counter);
            assertEquals(1, counter.getAccumulator().get(Counter.Element.EVIDENCE));
            assertEquals(1, counter.getAccumulator().get(Counter.Element.STRATEGY));
            assertEquals(1, counter.getAccumulator().get(Counter.Element.CONCLUSION));
        }

        @Test
        public void elementsCanBeAccessedInsideTheJustification() {
            JustificationModel j = unit.get("j1");
            assertEquals(j.get("c1"),   j.get("j1:c1"));
            assertEquals(j.get("s1"), j.get("j1:s1"));
            assertEquals(j.get("e1"), j.get("j1:e1"));
        }

        @Test
        public void elementsHaveTheRightType() {
            JustificationModel j =  unit.get("j1");
            assertInstanceOf(Conclusion.class,   j.get("j1:c1"));
            assertInstanceOf(Strategy.class, j.get("j1:s1"));
            assertInstanceOf(Evidence.class, j.get("j1:e1"));
        }

        @Test
        public void elementsAreCorrectlySupported() {
            JustificationModel j = unit.get("j1");
            assertEquals(Set.of("s1"), asIdentifiers(j.get("c1").getSupports()));
            assertEquals(Set.of("e1"), asIdentifiers(j.get("s1").getSupports()));
            assertEquals(Set.of(), asIdentifiers(j.get("e1").getSupports()));
        }

    }

    @Nested
    @DisplayName("Justification 'j2'")
    class j2JustificationTest {

        @Test
        public void containsTheRightModels(){ assertTrue(unit.exists("j2")); }

        @Test
        public void modelsHavetheRightType() { assertDoesNotThrow(() -> (Justification) unit.get("j2")); }

        @Test
        public void justificationContainsRightElements() {
            Counter counter = new Counter();
            unit.get("j2").accept(counter);
            assertEquals(1, counter.getAccumulator().get(Counter.Element.EVIDENCE));
            assertEquals(1, counter.getAccumulator().get(Counter.Element.STRATEGY));
            assertEquals(1, counter.getAccumulator().get(Counter.Element.CONCLUSION));
        }

        @Test
        public void elementsCanBeAccessedInsideTheJustification() {
            JustificationModel j = unit.get("j2");
            assertEquals(j.get("c2"),   j.get("j2:c2"));
            assertEquals(j.get("s2"), j.get("j2:s2"));
            assertEquals(j.get("e2"), j.get("j2:e2"));
        }

        @Test
        public void elementsHaveTheRightType() {
            JustificationModel j =  unit.get("j2");
            assertInstanceOf(Conclusion.class,   j.get("j2:c2"));
            assertInstanceOf(Strategy.class, j.get("j2:s2"));
            assertInstanceOf(Evidence.class, j.get("j2:e2"));
        }

        @Test
        public void elementsAreCorrectlySupported() {
            JustificationModel j = unit.get("j2");
            assertEquals(Set.of("s2"), asIdentifiers(j.get("c2").getSupports()));
            assertEquals(Set.of("e2"), asIdentifiers(j.get("s2").getSupports()));
            assertEquals(Set.of(), asIdentifiers(j.get("e2").getSupports()));
        }

    }

    @Nested
    @DisplayName("Composition j")
    class compositionTest {

        @Test
        public void containsTheRightModels(){ assertTrue(unit.exists("j")); }

        @Test
        public void modelsHavetheRightType() { assertDoesNotThrow(() -> (Justification) unit.get("j")); }

        @Test
        public void justificationContainsRightElements() {
            Counter counter = new Counter();
            unit.get("j").accept(counter);
            assertEquals(2, counter.getAccumulator().get(Counter.Element.EVIDENCE));
            assertEquals(1, counter.getAccumulator().get(Counter.Element.STRATEGY));
            assertEquals(1, counter.getAccumulator().get(Counter.Element.CONCLUSION));
        }

        @Test
        public void elementsCanBeAccessedInsideTheJustification() {
            JustificationModel j = unit.get("j");
            assertEquals(j.get("c1"), j.get("j:c1"));
            assertEquals(j.get("s1"), j.get("j:s1"));
            assertEquals(j.get("e1"), j.get("j:e1"));
            assertEquals(j.get("e2"), j.get("j:e2"));
        }

        @Test
        public void elementsHaveTheRightType() {
            JustificationModel j =  unit.get("j");
            assertInstanceOf(Conclusion.class,   j.get("j:c1"));
            assertInstanceOf(Strategy.class, j.get("j:s1"));
            assertInstanceOf(Evidence.class, j.get("j:e1"));
            assertInstanceOf(Evidence.class, j.get("j:e2"));
        }

        @Test
        public void elementsAreCorrectlySupported() {
            JustificationModel j = unit.get("j");
            assertEquals(Set.of("s1"), asIdentifiers(j.get("c1").getSupports()));
            assertEquals(Set.of("e1", "e2"), asIdentifiers(j.get("s1").getSupports()));
            assertEquals(Set.of(), asIdentifiers(j.get("e1").getSupports()));
            assertEquals(Set.of(), asIdentifiers(j.get("e2").getSupports()));
        }

    }
}
