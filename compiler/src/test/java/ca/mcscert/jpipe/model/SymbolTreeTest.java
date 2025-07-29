package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.model.elements.Evidence;
import ca.mcscert.jpipe.model.elements.JustificationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SymbolTreeTest {

    private SymbolTree t1;

    @BeforeEach
    public void setUp() {
        Evidence e1 = new Evidence("e", "Test evidence");


    }

    @Test
    public void cannotRecordDuplicates() {
    }

    @Test
    public void canRecordDuplicates() {}
}
