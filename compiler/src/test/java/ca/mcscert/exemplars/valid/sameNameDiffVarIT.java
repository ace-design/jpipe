package ca.mcscert.exemplars.valid;

import ca.mcscert.exemplars.Counter;
import ca.mcscert.exemplars.SourceFileTest;
import ca.mcscert.jpipe.model.elements.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Source file: src/test/resources/valid/sameNameDiffVar.jd")
public class sameNameDiffVarIT extends SourceFileTest {
    @Override
    protected String source() {
        return "valid/sameNameDiffVar.jd";
    }

    @Test
    public void containsOneJustification() {
        Counter counter = new Counter();
        this.unit.accept(counter);
        assertEquals(4, unit.getContents().size());
    }

    @Nested
    @DisplayName("Assemble 'recruitment'")
    class AssembleRecruitmentTest {

        @Test
        public void containsTheRightModels() {
            assertTrue(unit.exists("recruitment"));
        }

        @Test
        public void modelsHaveTheRightType() {
            assertDoesNotThrow(() -> (Justification) unit.get("recruitment"));
        }

        @Test
        public void patternContainsRightElements() {
            Counter counter = new Counter();
            unit.get("recruitment").accept(counter);
            assertEquals(5, counter.getAccumulator().getOrDefault(Counter.Element.EVIDENCE,0));
            assertEquals(4, counter.getAccumulator().getOrDefault(Counter.Element.STRATEGY,0));
            assertEquals(3, counter.getAccumulator().getOrDefault(Counter.Element.SUB_CONCLUSION,0));
            assertEquals(1, counter.getAccumulator().getOrDefault(Counter.Element.CONCLUSION,0));
        }

        @Test
        public void elementsCanBeAccessed() {
            JustificationModel j = unit.get("recruitment");
            assertEquals(j.get("c"),   j.get("recruitment:c"));
            assertEquals(j.get("AND"), j.get("recruitment:AND"));
            assertEquals(j.get("c1"), j.get("recruitment:c1"));
            assertEquals(j.get("c2"), j.get("recruitment:c2"));
            assertEquals(j.get("c3"), j.get("recruitment:c3"));
            assertEquals(j.get("calling"), j.get("recruitment:calling"));
            assertEquals(j.get("script"), j.get("recruitment:script"));
            assertEquals(j.get("directory"), j.get("recruitment:directory"));
            assertEquals(j.get("display"), j.get("recruitment:display"));
            assertEquals(j.get("locations"), j.get("recruitment:locations"));
            assertEquals(j.get("poster"), j.get("recruitment:poster"));
            assertEquals(j.get("chat"), j.get("recruitment:chat"));
            assertEquals(j.get("talking_points"), j.get("recruitment:talking_points"));
        }

        @Test
        public void elementsHaveTheRightType() {
            JustificationModel j = unit.get("recruitment");
            assertInstanceOf(Conclusion.class, j.get("recruitment:c"));
            assertInstanceOf(Strategy.class, j.get("recruitment:calling"));
            assertInstanceOf(SubConclusion.class, j.get("recruitment:c1"));
            assertInstanceOf(SubConclusion.class, j.get("recruitment:c2"));
            assertInstanceOf(SubConclusion.class, j.get("recruitment:c3"));
            assertInstanceOf(Strategy.class, j.get("recruitment:calling"));
            assertInstanceOf(Strategy.class, j.get("recruitment:display"));
            assertInstanceOf(Strategy.class, j.get("recruitment:chat"));
            assertInstanceOf(Evidence.class, j.get("recruitment:script"));
            assertInstanceOf(Evidence.class, j.get("recruitment:directory"));
            assertInstanceOf(Evidence.class, j.get("recruitment:locations"));
            assertInstanceOf(Evidence.class, j.get("recruitment:poster"));
            assertInstanceOf(Evidence.class, j.get("recruitment:talking_points"));
        }

        @Test
        public void elementsAreCorrectlySupported() {
            JustificationModel j = unit.get("recruitment");
            assertEquals(Set.of("AND"),   j.get("recruitment:c").getSupportingIds());
            assertEquals(Set.of("c1", "c2", "c3"), j.get("recruitment:AND").getSupportingIds());
            assertEquals(Set.of("calling"), j.get("recruitment:c1").getSupportingIds());
            assertEquals(Set.of("display"), j.get("recruitment:c2").getSupportingIds());
            assertEquals(Set.of("chat"), j.get("recruitment:c3").getSupportingIds());
            assertEquals(Set.of("script", "directory"), j.get("recruitment:calling").getSupportingIds());
            assertEquals(Set.of(), j.get("recruitment:script").getSupportingIds());
            assertEquals(Set.of(), j.get("recruitment:directory").getSupportingIds());
            assertEquals(Set.of("locations", "poster"), j.get("recruitment:display").getSupportingIds());
            assertEquals(Set.of(), j.get("recruitment:locations").getSupportingIds());
            assertEquals(Set.of(), j.get("recruitment:poster").getSupportingIds());
            assertEquals(Set.of("talking_points", "locations"), j.get("recruitment:chat").getSupportingIds());
            assertEquals(Set.of(), j.get("recruitment:talking_points").getSupportingIds());
        }

    }




}
