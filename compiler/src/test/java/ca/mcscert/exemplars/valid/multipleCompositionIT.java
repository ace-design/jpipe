package ca.mcscert.exemplars.valid;

import ca.mcscert.exemplars.Counter;
import ca.mcscert.exemplars.SourceFileTest;
import ca.mcscert.jpipe.model.elements.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DisplayName("Source file: src/test/resources/valid/multipleComposition.jd")
public class multipleCompositionIT extends SourceFileTest {
    @Override
    protected String source() {
        return "valid/multipleComposition.jd";
    }

    @Test
    public void containsOneJustification() {
        Counter counter = new Counter();
        this.unit.accept(counter);
        assertEquals(8, unit.getContents().size());
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
            assertEquals(j.get("c1"), j.get("recruitment:phone:c1"));
            assertEquals(j.get("c2"), j.get("recruitment:posters:c2"));
            assertEquals(j.get("c3"), j.get("recruitment:trusted:c3"));
            assertEquals(j.get("calling"), j.get("recruitment:phone:calling"));
            assertEquals(j.get("script"), j.get("recruitment:phone:script"));
            assertEquals(j.get("directory"), j.get("recruitment:phone:directory"));
            assertEquals(j.get("display"), j.get("recruitment:posters:display"));
            assertEquals(j.get("posters:locations"), j.get("recruitment:posters:locations"));
            assertEquals(j.get("poster"), j.get("recruitment:posters:poster"));
            assertEquals(j.get("chat"), j.get("recruitment:trusted:chat"));
            assertEquals(j.get("talking_points"), j.get("recruitment:trusted:talking_points"));
        }

        @Test
        public void elementsHaveTheRightType() {
            JustificationModel j = unit.get("recruitment");
            assertInstanceOf(Conclusion.class, j.get("c"));
            assertInstanceOf(Strategy.class, j.get("calling"));
            assertInstanceOf(SubConclusion.class, j.get("c1"));
            assertInstanceOf(SubConclusion.class, j.get("c2"));
            assertInstanceOf(SubConclusion.class, j.get("c3"));
            assertInstanceOf(Strategy.class, j.get("calling"));
            assertInstanceOf(Strategy.class, j.get("display"));
            assertInstanceOf(Strategy.class, j.get("chat"));
            assertInstanceOf(Evidence.class, j.get("script"));
            assertInstanceOf(Evidence.class, j.get("directory"));
            assertInstanceOf(Evidence.class, j.get("locations"));
            assertInstanceOf(Evidence.class, j.get("poster"));
            assertInstanceOf(Evidence.class, j.get("talking_points"));
        }

        @Test
        public void elementsAreCorrectlySupported() {
            JustificationModel j = unit.get("recruitment");
            assertEquals(Set.of("AND"),   j.get("c").getSupportingIds());
            assertEquals(Set.of("c1", "c2", "c3"), j.get("AND").getSupportingIds());
            assertEquals(Set.of("calling"), j.get("c1").getSupportingIds());
            assertEquals(Set.of("display"), j.get("c2").getSupportingIds());
            assertEquals(Set.of("chat"), j.get("c3").getSupportingIds());
            assertEquals(Set.of("script", "directory"), j.get("calling").getSupportingIds());
            assertEquals(Set.of(), j.get("script").getSupportingIds());
            assertEquals(Set.of(), j.get("directory").getSupportingIds());
            assertEquals(Set.of("locations", "poster"), j.get("display").getSupportingIds());
            assertEquals(Set.of(), j.get("locations").getSupportingIds());
            assertEquals(Set.of(), j.get("poster").getSupportingIds());
            assertEquals(Set.of("talking_points", "locations"), j.get("chat").getSupportingIds());
            assertEquals(Set.of(), j.get("talking_points").getSupportingIds());
        }

    }

    @Nested
    @DisplayName("Refine 'temp'")
    class RefineTempTest {
        @Test
        public void containsTheRightModels() {
            assertTrue(unit.exists("temp"));
        }

        @Test
        public void modelsHaveTheRightType() {
            assertDoesNotThrow(() -> (Justification) unit.get("temp"));
        }

        @Test
        public void patternContainsRightElements() {
            Counter counter = new Counter();
            unit.get("temp").accept(counter);
            assertEquals(6, counter.getAccumulator().getOrDefault(Counter.Element.EVIDENCE,0));
            assertEquals(6, counter.getAccumulator().getOrDefault(Counter.Element.STRATEGY,0));
            assertEquals(5, counter.getAccumulator().getOrDefault(Counter.Element.SUB_CONCLUSION,0));
            assertEquals(1, counter.getAccumulator().getOrDefault(Counter.Element.CONCLUSION,0));
        }

        @Test
        public void elementsCanBeAccessed() {
            JustificationModel j = unit.get("temp");
            assertEquals(j.get("c"),   j.get("temp:recruitment:c"));
            assertEquals(j.get("AND"), j.get("temp:recruitment:AND"));
            assertEquals(j.get("c1"), j.get("temp:recruitment:phone:c1"));
            assertEquals(j.get("c2"), j.get("temp:recruitment:posters:c2"));
            assertEquals(j.get("c3"), j.get("temp:recruitment:trusted:c3"));
            assertEquals(j.get("calling"), j.get("temp:recruitment:phone:calling"));
            assertEquals(j.get("script"), j.get("temp:recruitment:phone:script"));
            assertEquals(j.get("directory"), j.get("temp:recruitment:phone:directory"));
            assertEquals(j.get("display"), j.get("temp:recruitment:posters:display"));
            assertEquals(j.get("poster"), j.get("temp:recruitment:posters:poster"));
            assertEquals(j.get("chat"), j.get("temp:recruitment:trusted:chat"));
            assertEquals(j.get("talking_points"), j.get("temp:recruitment:trusted:talking_points"));
            assertEquals(j.get("loc_available"), j.get("temp:locations:loc_available"));
            assertEquals(j.get("combining"), j.get("temp:locations:combining"));
            assertEquals(j.get("public"), j.get("temp:locations:public"));
            assertEquals(j.get("authorized"), j.get("temp:locations:authorized"));
            assertEquals(j.get("authorization"), j.get("temp:locations:authorization"));
            assertEquals(j.get("places"), j.get("temp:locations:places"));
        }

        @Test
        public void elementsHaveTheRightType() {
            JustificationModel j = unit.get("temp");
            assertInstanceOf(Conclusion.class, j.get("c"));
            assertInstanceOf(Strategy.class, j.get("calling"));
            assertInstanceOf(SubConclusion.class, j.get("c1"));
            assertInstanceOf(SubConclusion.class, j.get("c2"));
            assertInstanceOf(SubConclusion.class, j.get("c3"));
            assertInstanceOf(SubConclusion.class, j.get("loc_available"));
            assertInstanceOf(SubConclusion.class, j.get("authorized"));
            assertInstanceOf(Strategy.class, j.get("calling"));
            assertInstanceOf(Strategy.class, j.get("display"));
            assertInstanceOf(Strategy.class, j.get("chat"));
            assertInstanceOf(Strategy.class, j.get("combining"));
            assertInstanceOf(Strategy.class, j.get("authorization"));
            assertInstanceOf(Evidence.class, j.get("script"));
            assertInstanceOf(Evidence.class, j.get("directory"));
            assertInstanceOf(Evidence.class, j.get("poster"));
            assertInstanceOf(Evidence.class, j.get("talking_points"));
            assertInstanceOf(Evidence.class, j.get("public"));
            assertInstanceOf(Evidence.class, j.get("places"));
        }

        @Test
        public void elementsAreCorrectlySupported() {
            JustificationModel j = unit.get("temp");
            assertEquals(Set.of("AND"),   j.get("c").getSupportingIds());
            assertEquals(Set.of("c1", "c2", "c3"), j.get("AND").getSupportingIds());
            assertEquals(Set.of("calling"), j.get("c1").getSupportingIds());
            assertEquals(Set.of("display"), j.get("c2").getSupportingIds());
            assertEquals(Set.of("chat"), j.get("c3").getSupportingIds());
            assertEquals(Set.of("script", "directory"), j.get("calling").getSupportingIds());
            assertEquals(Set.of(), j.get("script").getSupportingIds());
            assertEquals(Set.of(), j.get("directory").getSupportingIds());
            assertEquals(Set.of("loc_available", "poster"), j.get("display").getSupportingIds());
            assertEquals(Set.of(), j.get("poster").getSupportingIds());
            assertEquals(Set.of("talking_points", "loc_available"), j.get("chat").getSupportingIds());
            assertEquals(Set.of(), j.get("talking_points").getSupportingIds());
            assertEquals(Set.of("combining"), j.get("loc_available").getSupportingIds());
            assertEquals(Set.of("public", "authorized"), j.get("combining").getSupportingIds());
            assertEquals(Set.of(), j.get("public").getSupportingIds());
            assertEquals(Set.of("authorization"), j.get("authorized").getSupportingIds());
            assertEquals(Set.of("places"), j.get("authorization").getSupportingIds());
            assertEquals(Set.of(), j.get("places").getSupportingIds());
        }
    }

    @Nested
    @DisplayName("Refine 'final'")
    class RefineFinalTest {
        @Test
        public void containsTheRightModels() {
            assertTrue(unit.exists("final"));
        }

        @Test
        public void modelsHaveTheRightType() {
            assertDoesNotThrow(() -> (Justification) unit.get("final"));
        }

        @Test
        public void patternContainsRightElements() {
            Counter counter = new Counter();
            unit.get("final").accept(counter);
            assertEquals(7, counter.getAccumulator().getOrDefault(Counter.Element.EVIDENCE,0));
            assertEquals(7, counter.getAccumulator().getOrDefault(Counter.Element.STRATEGY,0));
            assertEquals(6, counter.getAccumulator().getOrDefault(Counter.Element.SUB_CONCLUSION,0));
            assertEquals(1, counter.getAccumulator().getOrDefault(Counter.Element.CONCLUSION,0));
        }

        @Test
        public void elementsCanBeAccessed() {
            JustificationModel j = unit.get("final");
            assertEquals(j.get("c"),   j.get("final:temp:recruitment:c"));
            assertEquals(j.get("AND"), j.get("final:temp:recruitment:AND"));
            assertEquals(j.get("c1"), j.get("final:temp:recruitment:phone:c1"));
            assertEquals(j.get("c2"), j.get("final:temp:recruitment:posters:c2"));
            assertEquals(j.get("c3"), j.get("final:temp:recruitment:trusted:c3"));
            assertEquals(j.get("calling"), j.get("final:temp:recruitment:phone:calling"));
            assertEquals(j.get("script"), j.get("final:temp:recruitment:phone:script"));
            assertEquals(j.get("display"), j.get("final:temp:recruitment:posters:display"));
            assertEquals(j.get("poster"), j.get("final:temp:recruitment:posters:poster"));
            assertEquals(j.get("chat"), j.get("final:temp:recruitment:trusted:chat"));
            assertEquals(j.get("talking_points"), j.get("final:temp:recruitment:trusted:talking_points"));
            assertEquals(j.get("loc_available"), j.get("final:temp:locations:loc_available"));
            assertEquals(j.get("combining"), j.get("final:temp:locations:combining"));
            assertEquals(j.get("public"), j.get("final:temp:locations:public"));
            assertEquals(j.get("authorized"), j.get("final:temp:locations:authorized"));
            assertEquals(j.get("authorization"), j.get("final:temp:locations:authorization"));
            assertEquals(j.get("places"), j.get("final:temp:locations:places"));
            assertEquals(j.get("reused"), j.get("final:phone_reuse:reused"));
            assertEquals(j.get("check"), j.get("final:phone_reuse:check"));
            assertEquals(j.get("directory"), j.get("final:phone_reuse:directory"));
            assertEquals(j.get("tou"), j.get("final:phone_reuse:tou"));
            // With partial scope
            assertEquals(j.get("c"),   j.get("temp:recruitment:c"));
            assertEquals(j.get("AND"), j.get("temp:recruitment:AND"));
            assertEquals(j.get("c1"), j.get("temp:recruitment:phone:c1"));
            assertEquals(j.get("c2"), j.get("temp:recruitment:posters:c2"));
            assertEquals(j.get("c3"), j.get("temp:recruitment:trusted:c3"));
            assertEquals(j.get("calling"), j.get("temp:recruitment:phone:calling"));
            assertEquals(j.get("script"), j.get("temp:recruitment:phone:script"));
            assertEquals(j.get("display"), j.get("temp:recruitment:posters:display"));
            assertEquals(j.get("poster"), j.get("temp:recruitment:posters:poster"));
            assertEquals(j.get("chat"), j.get("temp:recruitment:trusted:chat"));
            assertEquals(j.get("talking_points"), j.get("temp:recruitment:trusted:talking_points"));
            assertEquals(j.get("loc_available"), j.get("temp:locations:loc_available"));
            assertEquals(j.get("combining"), j.get("temp:locations:combining"));
            assertEquals(j.get("public"), j.get("temp:locations:public"));
            assertEquals(j.get("authorized"), j.get("temp:locations:authorized"));
            assertEquals(j.get("authorization"), j.get("temp:locations:authorization"));
            assertEquals(j.get("places"), j.get("temp:locations:places"));
            assertEquals(j.get("reused"), j.get("phone_reuse:reused"));
            assertEquals(j.get("check"), j.get("phone_reuse:check"));
            assertEquals(j.get("directory"), j.get("phone_reuse:directory"));
            assertEquals(j.get("tou"), j.get("phone_reuse:tou"));
        }

        @Test
        public void elementsHaveTheRightType() {
            JustificationModel j = unit.get("final");

            assertInstanceOf(Conclusion.class, j.get("c"));
            assertInstanceOf(Strategy.class, j.get("calling"));
            assertInstanceOf(SubConclusion.class, j.get("c1"));
            assertInstanceOf(SubConclusion.class, j.get("c2"));
            assertInstanceOf(SubConclusion.class, j.get("c3"));
            assertInstanceOf(SubConclusion.class, j.get("loc_available"));
            assertInstanceOf(SubConclusion.class, j.get("authorized"));
            assertInstanceOf(SubConclusion.class, j.get("reused"));
            assertInstanceOf(Strategy.class, j.get("calling"));
            assertInstanceOf(Strategy.class, j.get("display"));
            assertInstanceOf(Strategy.class, j.get("chat"));
            assertInstanceOf(Strategy.class, j.get("combining"));
            assertInstanceOf(Strategy.class, j.get("authorization"));
            assertInstanceOf(Strategy.class, j.get("check"));
            assertInstanceOf(Evidence.class, j.get("script"));
            assertInstanceOf(Evidence.class, j.get("directory"));
            assertInstanceOf(Evidence.class, j.get("poster"));
            assertInstanceOf(Evidence.class, j.get("talking_points"));
            assertInstanceOf(Evidence.class, j.get("public"));
            assertInstanceOf(Evidence.class, j.get("places"));
            assertInstanceOf(Evidence.class, j.get("tou"));
        }

        @Test
        public void elementsAreCorrectlySupported() {
            JustificationModel j = unit.get("final");
            assertEquals(Set.of("AND"),   j.get("c").getSupportingIds());
            assertEquals(Set.of("c1", "c2", "c3"), j.get("AND").getSupportingIds());
            assertEquals(Set.of("calling"), j.get("c1").getSupportingIds());
            assertEquals(Set.of("display"), j.get("c2").getSupportingIds());
            assertEquals(Set.of("chat"), j.get("c3").getSupportingIds());
            assertEquals(Set.of("script", "reused"), j.get("calling").getSupportingIds());
            assertEquals(Set.of(), j.get("script").getSupportingIds());
            assertEquals(Set.of("loc_available", "poster"), j.get("display").getSupportingIds());
            assertEquals(Set.of(), j.get("poster").getSupportingIds());
            assertEquals(Set.of("talking_points", "loc_available"), j.get("chat").getSupportingIds());
            assertEquals(Set.of(), j.get("talking_points").getSupportingIds());
            assertEquals(Set.of("combining"), j.get("loc_available").getSupportingIds());
            assertEquals(Set.of("public", "authorized"), j.get("combining").getSupportingIds());
            assertEquals(Set.of(), j.get("public").getSupportingIds());
            assertEquals(Set.of("authorization"), j.get("authorized").getSupportingIds());
            assertEquals(Set.of("places"), j.get("authorization").getSupportingIds());
            assertEquals(Set.of(), j.get("places").getSupportingIds());
            assertEquals(Set.of("check"), j.get("reused").getSupportingIds());
            assertEquals(Set.of("directory", "tou"), j.get("check").getSupportingIds());
            assertEquals(Set.of(), j.get("directory").getSupportingIds());
            assertEquals(Set.of(), j.get("tou").getSupportingIds());

        }
    }



}
