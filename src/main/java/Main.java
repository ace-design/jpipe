import ca.mcscert.jpipe.CommandLineConfiguration;
import ca.mcscert.jpipe.compiler.CompilationError;
import ca.mcscert.jpipe.compiler.Compiler;
import ca.mcscert.jpipe.compiler.TypeError;
import ca.mcscert.jpipe.exporters.DiagramExporter;
import ca.mcscert.jpipe.exporters.Exportation;
import ca.mcscert.jpipe.exporters.ExportationError;
import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.model.Unit;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;


public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        CommandLineConfiguration config = new CommandLineConfiguration(args);
        CommandLine cmd = null;

        try {
            Optional<CommandLine> tmp = config.read();
            if (tmp.isEmpty()){
                config.help();
                System.exit(0);
            }
            cmd = tmp.get();
        } catch (ParseException e) {
            System.err.println(ANSI_RED + e.getMessage() + ANSI_RESET);
            System.exit(1);
        }

        String inputFile = cmd.getOptionValue("input");
        String outputDirectory = cmd.getOptionValue("output");
        String format = cmd.getOptionValue("format", "png");  // Default to PNG if no format is specified

        if (!"png".equals(format) && !"svg".equals(format)) {
            System.err.println(ANSI_RED + "Invalid format specified. Choose 'png' or 'svg'." + ANSI_RESET);
            System.exit(1);
        }

        if (outputDirectory == null) {
            outputDirectory = System.getProperty("user.dir");
        }

        Optional<String> diagramName = Optional.ofNullable(cmd.getOptionValue("diagram"));

        try {
            process(inputFile, outputDirectory, diagramName, format);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void process(String inputFile, String outputDirectory, Optional<String> diagramName, String format) {
        Unit unit;
        try {
            unit = (new Compiler()).compile(inputFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(ANSI_RED + "File not found: " + e.getMessage() + ANSI_RESET, e);
        } catch (CompilationError | TypeError | ExportationError err) {
            throw new RuntimeException(ANSI_RED + err.getMessage() + ANSI_RESET, err);
        }

        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            throw new IllegalArgumentException(ANSI_RED + "Output directory does not exist: " + outputDir.getPath() + ANSI_RESET);
        }

        DiagramExporter exporter = new DiagramExporter();

        if (diagramName.isPresent()) {
            Optional<JustificationDiagram> tmp = unit.findByName(diagramName.get());
            if (tmp.isEmpty()) {
                throw new IllegalArgumentException(ANSI_RED + "Diagram not found: " + diagramName.get() + ANSI_RESET);
            }
            JustificationDiagram justification = tmp.get();
            String outputFilePath = outputDir.getAbsolutePath() + "/" + removeFileExtension(inputFile) + "_" + justification.name() + "." + format;
            exporter.export(justification, outputFilePath, format);
        } else {
            for (JustificationDiagram justification : unit.getJustificationSet()) {
                String outputFilePath = outputDir.getAbsolutePath() + "/" + removeFileExtension(inputFile) + "_" + justification.name() + "." + format;
                exporter.export(justification, outputFilePath, format);
            }
        }
    }

    private static String removeFileExtension(String filename) {
        File f = new File(filename);
        return f.getName().replaceAll("(?<!^)[.][^.]*$", "");
    }
}