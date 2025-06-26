package ca.mcscert.jpipe.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {

    private Options buildOptions() {
        Options options = new Options();
        for (Option opt : Option.values()) {
            opt.registerInto(options);
        }
        return options;
    }

    private CommandLine parse(String[] args) throws Exception {
        return new DefaultParser().parse(buildOptions(), args);
    }

    @Test
    void testDefaults() throws Exception {
        CommandLine cmd = parse(new String[]{});
        Configuration config = new Configuration(cmd);

        assertEquals(Configuration.STDIN_PATH, config.getInputFilePath());
        assertEquals(Configuration.STDOUT_PATH, config.getOutputFilePath());
        assertNull(config.getDiagramName());
        assertEquals(Format.PNG, config.getFormat());
        assertEquals(Mode.PRINT, config.getMode());
    }

    @Test
    void testCustomInputOutput() throws Exception {
        String[] args = {
            "-i", "input.jp",
            "-o", "output.png"
        };
        CommandLine cmd = parse(args);
        Configuration config = new Configuration(cmd);

        assertEquals("input.jp", config.getInputFilePath());
        assertEquals("output.png", config.getOutputFilePath());
    }

    @Test
    void testDiagramName() throws Exception {
        String[] args = {"-d", "MyDiagram"};
        CommandLine cmd = parse(args);
        Configuration config = new Configuration(cmd);

        assertEquals("MyDiagram", config.getDiagramName());
    }

    @Test
    void testFormatAndMode() throws Exception {
        String[] args = {"-f", "svg", "--mode", "print"};
        CommandLine cmd = parse(args);
        Configuration config = new Configuration(cmd);

        assertEquals(Format.SVG, config.getFormat());
        assertEquals(Mode.PRINT, config.getMode());
    }

    @Test
    void testInvalidFormatThrowsException() {
        String[] args = {"-f", "invalidformat"};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Configuration config = new Configuration(parse(args));
        });
        assertTrue(exception.getMessage().toLowerCase().contains("invalidformat"));
    }

    @Test
    void testInvalidModeThrowsException() {
        String[] args = {"--mode", "invalidmode"};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Configuration config = new Configuration(parse(args));
        });
        assertTrue(exception.getMessage().toLowerCase().contains("invalidmode"));
    }
}
