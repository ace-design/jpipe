package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.error.DuplicateSymbol;
import ca.mcscert.jpipe.model.elements.Conclusion;
import ca.mcscert.jpipe.model.elements.Evidence;
import ca.mcscert.jpipe.model.elements.Strategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SymbolTableTest {

    private SymbolTable t1;

    @BeforeEach
    public void setUp() {
        this.t1 = new SymbolTable();
        Evidence e1 = new Evidence("e", "Test evidence");
        Strategy s1 = new Strategy("s", "Test strategy");
        Conclusion c1 = new Conclusion("c", "Test conclusion");
        t1.record(e1.getIdentifier(), e1);
        t1.record(s1.getIdentifier(), s1);
        t1.record(c1.getIdentifier(), c1);
    }

    @Test
    public void containsCorrectSymbols() {
        assertTrue(t1.exists("e"));
        assertTrue(t1.exists("s"));
        assertTrue(t1.exists("c"));
    }


    @Test
    public void cannotRecordExactDuplicates() {
        Evidence e1 = (Evidence) t1.get("e");
        assertThrows(DuplicateSymbol.class, () -> t1.record(e1.getIdentifier(), e1));
    }

    @Test
    public void canRecordDuplicatesIdentifiers() {
        Evidence e2 = new Evidence("e", "Another evidence");
        e2.setScope("test scope");
        t1.record(e2.getIdentifier(), e2);
        assertEquals(t1.get("e", "test scope"), e2);
    }
}
