package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.error.DuplicateSymbol;
import ca.mcscert.jpipe.error.UnknownSymbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SymbolTableTest {

    private SymbolTable<Integer> t123;
    private SymbolTable<Integer> t456;
    private SymbolTable<Integer> t34;
    private SymbolTable<Integer> p456789;
    private SymbolTable<Integer> p123456789;

    @BeforeEach
    public void initialize() {
        t123 = new SymbolTable<>();
        t123.record("one", 1);
        t123.record("two", 2);
        t123.record("three", 3);

        t456 = new SymbolTable<>();
        t456.record("four", 4);
        t456.record("five", 5);
        t456.record("six", 6);

        t34 = new SymbolTable<>();
        t34.record("three", 3);
        t34.record("four", 4);

        p456789 = new SymbolTable<>(t456);
        p456789.record("seven", 7);
        p456789.record("eight", 8);
        p456789.record("nine", 9);

        p123456789 = new SymbolTable<>(p456789);
        p123456789.record("one", 1);
        p123456789.record("two", 2);
        p123456789.record("three", 3);

    }

    @Test
    public void scalarTable() {
        assertEquals(t123.get("one"), 1);
        assertThrows(UnknownSymbol.class, () -> t123.get("four"));
    }

    @Test
    public void inheritingTable() {
        assertEquals(p456789.get("seven"), 7);
        assertEquals(p456789.get("four"), 4);
        assertThrows(UnknownSymbol.class, () -> p456789.get("one"));
    }

    @Test
    public void cannotRecordDuplicates() {
        assertThrows(DuplicateSymbol.class, () -> t123.record("one", 42));
        assertThrows(DuplicateSymbol.class, () -> p456789.record("four", 42));
    }


}