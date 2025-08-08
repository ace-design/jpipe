package ca.mcscert.exemplars.valid;

import ca.mcscert.exemplars.Counter;
import ca.mcscert.exemplars.SourceFileTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Source file: src/test/resources/valid/loadJM.jd")
public class loadJMIT extends SourceFileTest {
    @Override
    protected String source() {
        return "valid/loadJM.jd";
    }

    @Test
    public void containsLoadedJustifications() {
        Counter counter = new Counter();
        this.unit.accept(counter);
        System.out.println(unit);
        assertEquals(3, unit.getContents().size());
        assertTrue(unit.exists("proving"));
        assertTrue(unit.exists("j1"));
        assertTrue(unit.exists("j"));
    }
}
