import ca.mcscert.jpipe.CommandLineConfiguration;
import ca.mcscert.jpipe.compiler.CompilationError;
import ca.mcscert.jpipe.compiler.Compiler;
import ca.mcscert.jpipe.compiler.TypeError;
import ca.mcscert.jpipe.exporters.DiagramExporter;
import ca.mcscert.jpipe.exporters.Exportation;
import ca.mcscert.jpipe.exporters.ExportationError;
import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.model.Unit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;


/**
 * Entry point of the compiler.
 */
public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    /**
     * Entry point of the system, triggering the compilation process.
     *
     * @param args arguments provided to the compiler through command line
     */
    public static void main(String[] args) {
        try {
            CommandLine cmd = getCommandLineArgs(args);

            String inputFile = cmd.getOptionValue("input");
            String outputDirectory = cmd.getOptionValue("output",
                                                        System.getProperty("user.dir"));
            String format = cmd.getOptionValue("format",
                                    "png");
            String[] diagramNames = cmd.getOptionValues("diagram");

            process(inputFile, outputDirectory, diagramNames, format);
        } catch (Exception | Error e) {
            System.err.println(ANSI_RED + e.getMessage() + ANSI_RESET);
            System.exit(1);
        }
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

    private static void process(String inputFile, String outputDirectory,
                                String[] diagramNames, String format) {
        Unit unit = buildUnitFromFile(inputFile);
        File outputDir = getOutputDirectory(outputDirectory);
        Set<String> relevantDiagrams = extractDiagrams(unit, diagramNames);
        Exportation<JustificationDiagram> exporter = new DiagramExporter();

        for (String diagramName : relevantDiagrams) {
            JustificationDiagram diag = unit.findByName(diagramName).orElseThrow();
            String outfile = getOutputFilePath(inputFile, outputDir, diagramName, format);
            exporter.export(diag, outfile, format);
        }
    }

    private static String getOutputFilePath(String inputFile, File outputDir,
                                            String name, String format) {
        return outputDir.getAbsolutePath() + "/"
                + removeFileExtension(inputFile) + "_" + name + "." + format;
    }

    private static Set<String> extractDiagrams(Unit unit, String[] diagramNames) {
        Set<String> all = unit.getJustificationSet().stream()
                              .map(JustificationDiagram::name).collect(Collectors.toSet());
        if (diagramNames == null || diagramNames.length == 0) {
            return all;
        }
        Set<String> asked = new HashSet<>();
        for (String name : diagramNames) {
            if (all.contains(name)) {
             asked.add(name);
            } else {
             throw new ExportationError("Unknown diagram identifier: [" + name + "]");
            }
        }
        return asked;
    }

    private static File getOutputDirectory(String outputDirectory) {
        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            throw new IllegalArgumentException("Output directory does not exist: "
                    + outputDir.getPath());
        }
        return outputDir;
    }

    private static Unit buildUnitFromFile(String inputFile) {
        try {
            return (new Compiler()).compile(inputFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + e.getMessage(), e);
        } catch (CompilationError | TypeError | ExportationError err) {
            throw new RuntimeException(err.getMessage(), err);
        }
    }

    private static String removeFileExtension(String filename) {
        File f = new File(filename);
        return f.getName().replaceAll("(?<!^)[.][^.]*$", "");
    }
}