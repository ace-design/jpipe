package ca.mcscert.jpipe.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionTest {

    @Test
    void testRegisterIntoAddsOption() {
        Options options = new Options();
        Option.INPUT_PATH.registerInto(options);
        assertNotNull(options.getOption("i"));
        assertNotNull(options.getOption("input"));
    }

    @Test
    void testIsInReturnsTrueWhenPresent() throws Exception {
        Options options = new Options();
        Option.INPUT_PATH.registerInto(options);
        CommandLine cmd = new DefaultParser().parse(options, new String[]{"-i", "file.jp"});
        assertTrue(Option.INPUT_PATH.isIn(cmd));
    }

    @Test
    void testIsInReturnsFalseWhenAbsent() throws Exception {
        Options options = new Options();
        Option.INPUT_PATH.registerInto(options);
        CommandLine cmd = new DefaultParser().parse(options, new String[]{});
        assertFalse(Option.INPUT_PATH.isIn(cmd));
    }

    @Test
    void testReadFromReturnsCorrectValue() throws Exception {
        Options options = new Options();
        Option.INPUT_PATH.registerInto(options);
        CommandLine cmd = new DefaultParser().parse(options, new String[]{"-i", "file.jp"});
        assertEquals("file.jp", Option.INPUT_PATH.readFrom(cmd));
    }
}
