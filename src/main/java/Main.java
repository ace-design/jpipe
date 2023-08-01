import ca.mcscert.jpipe.CommandLineConfiguration;
import ca.mcscert.jpipe.compiler.CompilationError;
import ca.mcscert.jpipe.compiler.Compiler;
import ca.mcscert.jpipe.compiler.TypeError;
import ca.mcscert.jpipe.exporters.DiagramExporter;
import ca.mcscert.jpipe.exporters.Exportation;
import ca.mcscert.jpipe.exporters.ExportationError;
import ca.mcscert.jpipe.model.ConcreteJustification;
import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.model.Unit;

import org.apache.commons.cli.*;

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
            System.err.println(ANSI_RED +e.getMessage() + ANSI_RESET);
            System.exit(1);
        }

        String inputFile = cmd.getOptionValue("input");
        String outputDirectory = cmd.getOptionValue("output");
        String diagramName = cmd.getOptionValue("diagram");

        try {
            process(inputFile, outputDirectory, diagramName);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void process(String inputFile, String outputDirectoryName, String diagramName) {
        Unit unit;
        try {
            unit = (new Compiler()).compile(inputFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(ANSI_RED + "File not found: " + e.getMessage() + ANSI_RESET, e);
        } catch (CompilationError | TypeError | ExportationError err) {
            throw new RuntimeException(ANSI_RED + err.getMessage() + ANSI_RESET, err);
        }

        File outputDirectory = new File(outputDirectoryName);
        if (!outputDirectory.exists()) {
            throw new IllegalArgumentException(ANSI_RED + "Output directory does not exist: " + outputDirectory.getPath() + ANSI_RESET);
        }

        Optional<JustificationDiagram> tmp = unit.findByName(diagramName);
        if (tmp.isEmpty()) {
            throw new IllegalArgumentException(ANSI_RED + "Diagram not found: " + diagramName + ANSI_RESET);
        }
        JustificationDiagram justification = tmp.get();
        Exportation<ConcreteJustification> exporter = new DiagramExporter();
        String outputFilePath = outputDirectory.getAbsolutePath() + "/" + removeFileExtension(inputFile) +
                "_" + justification.name() + ".png";
        System.out.println(outputFilePath);
        exporter.export((ConcreteJustification) justification, outputFilePath);
    }

    private static String removeFileExtension(String filename) {
        // https://www.baeldung.com/java-filename-without-extension
        System.out.println(filename);
        File f = new File(filename);
        return f.getName().replaceAll("(?<!^)[.][^.]*$", "");
    }
}

