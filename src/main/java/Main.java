import ca.mcscert.jpipe.ColorPrinter;
import ca.mcscert.jpipe.CommandLineConfiguration;
import ca.mcscert.jpipe.compiler.Compiler;
import ca.mcscert.jpipe.exporters.DiagramExporter;
import ca.mcscert.jpipe.exporters.Exportation;
import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.model.Unit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystemException;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;


/**
 * Entry point of the compiler. Boring but necessary stuff.
 */
public class Main {

    /**
     * Entry point of the system, triggering the compilation process.
     *
     * @param args arguments provided to the compiler through command line
     */
    public static void main(String[] args) {
        try {
            CommandLine cmd = getCommandLineArgs(args);
            configureLogLevel(cmd);
            CompilerConfiguration config = configure(cmd);
            logger.info(config);
            Unit unit = new Compiler().compile(config.input);
            if (config.flushAll()) {
                flushAllDiagrams(unit, config.all().get(), config.format());
            } else {
                processOne(unit, config.diagramName(), config.output(), config.format());
            }
        } catch (Exception | Error e) {
            ColorPrinter printer = new ColorPrinter(System.err);
            printer.println("Top-level error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Model a consistent configuration of the compiler.
     *
     * @param input input file to be used   (empty => stdin)
     * @param output output file to be used (empty => stdout)
     * @param diagramName diagram name to export
     * @param format file format to be used
     * @param all should we export EVERYTHING instead of just one diagram?
     */
    private record CompilerConfiguration(Optional<File> input,
                                         Optional<File> output,
                                         Optional<String> diagramName, String format,
                                         Optional<File> all)  {
        public boolean flushAll() {
            return all.isPresent();
        }

    }

    private static final Logger logger = LogManager.getLogger();


    /**
     * Extract information from command line arguments, checking for errors.
     *
     *  @param args an Apache CLI command line
     * @return a consistent CompilerConfiguration (with default values when needed)
     * @throws IOException if the file system is not OK with the configuration
     */
    private static CompilerConfiguration configure(CommandLine args) throws IOException {
        // Process input stream
        logger.debug("Configuring input stream to stdin (default)");
        Optional<File> input = Optional.empty();
        if (args.hasOption("-i")) {
            logger.debug("Overriding with user-provided file as input");
            File inputFile = new File(args.getOptionValue("-i"));
            if (! inputFile.exists()) {
                throw new FileNotFoundException("Input file not found: " + inputFile.getPath());
            }
            input = Optional.of(inputFile);
        }

        // Process format
        logger.debug("Configuring output format to PNG");
        String format = "PNG";
        if (args.hasOption("-f")) {
            logger.debug("Overriding with user-provided file format");
            format = args.getOptionValue("-f");
            if (! Set.of("PNG", "SVG").contains(format)) {
                throw new IllegalArgumentException("Unknown format: " + format);
            }
        }

        // Process the "flush all" option first:
        if (args.hasOption("--all")) {
            logger.info("Switching to 'flush all diagrams' mode");
            File outputDirectory = new File(args.getOptionValue("--all"));
            if (! outputDirectory.exists()) {
                if (! outputDirectory.mkdir()) {
                    throw new FileSystemException("Unable to create directory: "
                            + outputDirectory.getPath());
                }
            }
            if (! outputDirectory.isDirectory()) {
                throw new FileSystemException("Output must be a directory: "
                        + outputDirectory.getPath());
            }
            return new CompilerConfiguration(input, null, null,
                    format, Optional.of(outputDirectory));
        }

        logger.debug("Switching to 'export one diagram' mode");
        // We're not in flush mode, so process output stream
        Optional<File> output = Optional.empty();
        if (args.hasOption("-o")) {
            File outputFile = new File(args.getOptionValue("-o"));
            if (outputFile.exists()) {
                if (! outputFile.canWrite()) {
                    throw new FileSystemException("Output file not writable: "
                            + outputFile.getPath());
                }
            } else {
                if (!outputFile.createNewFile()) {
                    throw new FileSystemException("Cannot create output file "
                            + outputFile.getPath());
                }
            }
            output = Optional.of(outputFile);
        }

        Optional<String> diagram = Optional.empty();
        if (args.hasOption("-d")) {
           diagram = Optional.of(args.getOptionValue("-d"));
        }
        return new CompilerConfiguration(input, output, diagram, format, Optional.empty());
    }

    /**
     * Extract CommandLine elements from the args provided by the user.
     * this method ends up the compilation process of the help is invoked from the CLI.
     *
     * @param args argument provided on the command line
     * @return parsed command line.
     */
    private static CommandLine getCommandLineArgs(String[] args) throws ParseException {
        CommandLineConfiguration config = new CommandLineConfiguration(args);
        Optional<CommandLine> tmp = config.read();
        if (tmp.isEmpty()) {
            config.help();
            System.exit(0);
        }
        return tmp.get();
    }

    /**
     * Manually override the log level, mainly for debugging purpose (dev mode).
     *
     * @param cmd the commandline (Apache CLI) containing (maybe) the log level to override to
     */
    private static void configureLogLevel(CommandLine cmd) {
        if (cmd.hasOption("log-level")) {
            LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            Configuration config = ctx.getConfiguration();
            LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
            String level = cmd.getOptionValue("log-level");
            loggerConfig.setLevel(Level.getLevel(level));
            ctx.updateLoggers();
        }
    }


    private static void processOne(Unit unit, Optional<String> diagramName,
                                   Optional<File> output, String format)
            throws IOException {
        String identifier;
        if (diagramName.isEmpty()) {
            if (unit.getIdentifiers().size() == 1) {
                identifier = unit.getIdentifiers().stream().toList().get(0);
            } else {
                throw new IllegalArgumentException("Must specify diagram name!");
            }
        } else {
            identifier = diagramName.get();
        }
        OutputStream os = System.out;
        if (output.isPresent()) {
            os = new FileOutputStream(output.get());
        }
        Exportation<JustificationDiagram> exporter = new DiagramExporter();
        JustificationDiagram diag =
                unit.findByName(identifier)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Unknown diagram " + identifier));
        try {
            exporter.export(diag, os, format);
        } finally {
            os.close();
        }

    }

    private static void flushAllDiagrams(Unit unit, File outputDirectory, String format)
            throws IOException {
        Set<String> identifiers = unit.getIdentifiers();
        for (String id : identifiers) {
            String filename = outputDirectory.getPath() + "/" + id + "." + format.toLowerCase();
            File outputFile = new File(filename);
            processOne(unit, Optional.of(id), Optional.of(outputFile), format);
        }

    }

}