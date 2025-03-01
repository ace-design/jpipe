package ca.mcscert.jpipe.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SymbolLocatorTest {

    private SymbolRegistry registry;

    @BeforeEach
    public void initialize() {
        this.registry = SymbolRegistry.getInstance();
        this.registry.record("aJustification", "aSymbol", "aFile.jd", 24, 42);
    }

    @Test
    public void recordingAtomicLocation() {
        SymbolRegistry.Symbol loc = this.registry.lookup("aJustification:aSymbol");
        assertEquals("aJustification:aSymbol", loc.symbolName());
        assertEquals("aFile.jd", loc.file());
        assertEquals(24, loc.line());
        assertEquals(42, loc.column());
    }

    @Test
    public void unknownSymbolThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> this.registry.lookup("foo:bar"));
    }

    @Test
    public void cannotRenameUnknownSymbol() {
        assertThrows(IllegalArgumentException.class, () -> this.registry.rename("foo:bar"));
    }

    @Test
    public void renamingsymbols() {
        String r1 = this.registry.rename("aJustification:aSymbol");
        String r2 = this.registry.rename(r1);
        String r3 = this.registry.rename(r2);

        SymbolRegistry.Symbol loc = this.registry.lookup("aJustification:aSymbol");
        assertEquals(loc, this.registry.lookup(r1));
        assertEquals(loc, this.registry.lookup(r2));
        assertEquals(loc, this.registry.lookup(r3));
    }

}
