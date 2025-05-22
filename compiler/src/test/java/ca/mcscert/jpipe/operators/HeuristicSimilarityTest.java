package ca.mcscert.jpipe.operators;

import ca.mcscert.jpipe.model.elements.*;
import ca.mcscert.jpipe.operators.internals.EquivalenceClass;
import ca.mcscert.jpipe.operators.internals.HeuristicSimilarity;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeuristicSimilarityTest {
    private JustificationModel j1;
    private JustificationModel j2;

    @BeforeEach
    public void setUp() {
        // Initializing j1
        j1 = new Justification("j1");
        Evidence e1 = new Evidence("e1", "A unique Evidence");
        j1.add(e1);
        Strategy s1 = new Strategy("s1", "An qeuivalent strategy");
        e1.supports(s1);
        j1.add(s1);
        Conclusion c1 = new Conclusion("c1", "A shared conclusion");
        j1.add(c1);
        s1.supports(c1);

        // Initializing j2
        j2 = new Justification("j2");
        Evidence e2 = new Evidence("e2", "frdafreafer");
        j2.add(e2);
        Strategy s2 = new Strategy("s2", "An equivalent strategy");
        e2.supports(s2);
        j2.add(s2);
        Conclusion c2 = new Conclusion("c2", "A shared conclusion");
        j2.add(c2);
        s2.supports(c2);
    }

    @Test
    public void equivalenceReflexivity() {
        HeuristicSimilarity hs = new HeuristicSimilarity();
        Map<String, String> mergedSet = hs.apply(j1, j1, 10);
        assertEquals(3, mergedSet.size());
    }

    @Test
    public void identifyEquivalentElements() {
        HeuristicSimilarity hs = new HeuristicSimilarity();
        Map<String, String> mergedSet = hs.apply(j1, j2, 50);
        System.out.println(mergedSet);
        assertEquals(2, mergedSet.size());

    }




}
