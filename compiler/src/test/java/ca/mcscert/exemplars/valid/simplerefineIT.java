package ca.mcscert.exemplars.valid;

import ca.mcscert.exemplars.Counter;
import ca.mcscert.exemplars.SourceFileTest;
import ca.mcscert.jpipe.model.HierarchyMap;
import ca.mcscert.jpipe.model.elements.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DisplayName("Source file: src/test/resources/valid/simplerefine.jd")
public class simplerefineIT extends SourceFileTest {
    @Override
    protected String source() {
        return "valid/simplerefine.jd";
    }

    @Test
    public void containsOneJustification() {
        Counter counter = new Counter();
        this.unit.accept(counter);
        assertEquals(3, unit.getContents().size());
    }

    @Nested
    @DisplayName("Composition 'phone_reuse'")
    class ProvingJustificationTest {
        @Test
        public void containsTheRightModels() {
            assertTrue(unit.exists("phone_reuse"));
        }

        @Test
        public void modelsHaveTheRightType() {
            assertDoesNotThrow(() -> (Justification) unit.get("phone_reuse"));
        }

        @Test
        public void patternContainsRightElements() {
            Counter counter = new Counter();
            unit.get("phone_reuse").accept(counter);
            assertEquals(3, counter.getAccumulator().getOrDefault(Counter.Element.EVIDENCE,0));
            assertEquals(2, counter.getAccumulator().getOrDefault(Counter.Element.STRATEGY,0));
            assertEquals(1, counter.getAccumulator().getOrDefault(Counter.Element.SUB_CONCLUSION,0));
            assertEquals(1, counter.getAccumulator().getOrDefault(Counter.Element.CONCLUSION,0));
        }

        @Test
        public void elementsCanBeAccessed() {
            JustificationModel j = unit.get("phone_reuse");
            assertEquals(j.get("c1"),   j.get("phone_reuse:phone:c1"));
            assertEquals(j.get("calling"), j.get("phone_reuse:phone:calling"));
            assertEquals(j.get("script"), j.get("phone_reuse:phone:script"));
            assertEquals(j.get("c4"), j.get("phone_reuse:existing_directory:c4"));
            assertEquals(j.get("check"), j.get("phone_reuse:existing_directory:check"));
            assertEquals(j.get("directory"), j.get("phone_reuse:existing_directory:directory"));
            assertEquals(j.get("tou"), j.get("phone_reuse:existing_directory:tou"));
        }

        @Test
        public void elementsHaveTheRightType() {
            JustificationModel j = unit.get("phone_reuse");
            assertInstanceOf(Conclusion.class,   j.get("c1"));
            assertInstanceOf(Strategy.class, j.get("calling"));
            assertInstanceOf(Evidence.class, j.get("script"));
            assertInstanceOf(SubConclusion.class, j.get("c4"));
            assertInstanceOf(Strategy.class, j.get("check"));
            assertInstanceOf(Evidence.class, j.get("directory"));
            assertInstanceOf(Evidence.class, j.get("tou"));
        }

        @Test
        public void elementsAreCorrectlySupported() {
            JustificationModel j = unit.get("phone_reuse");
            assertEquals(Set.of("calling"), j.get("c1").getSupportingIds());
            assertEquals(Set.of("script", "c4"), j.get("calling").getSupportingIds());
            assertEquals(Set.of("check"), j.get("c4").getSupportingIds());
            assertEquals(Set.of("directory", "tou"), j.get("check").getSupportingIds());
            assertEquals(Set.of(), j.get("directory").getSupportingIds());
            assertEquals(Set.of(), j.get("tou").getSupportingIds());
        }

        @Test
        public void elementsAreCorrectlyLinked() {
            JustificationModel j1 = unit.get("phone");
            JustificationModel j2 = unit.get("existing_directory");
            JustificationModel j = unit.get("phone_reuse");
            HierarchyMap<JustificationElement> map = j.representations();
            assertEquals(map.getAllParents(j.get("c4")), Set.of(j1.get("directory")));
        }



    }
}
