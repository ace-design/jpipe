package ca.mcscert.exemplars.valid;

import ca.mcscert.exemplars.Counter;
import ca.mcscert.exemplars.SourceFileTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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



}
