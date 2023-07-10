import ca.mcscert.jpipe.compiler.CompilationError;
import ca.mcscert.jpipe.compiler.Compiler;
import ca.mcscert.jpipe.compiler.TypeError;
import ca.mcscert.jpipe.exporters.DiagramExporter;
import ca.mcscert.jpipe.exporters.Exportation;
import ca.mcscert.jpipe.exporters.ExportationError;
import ca.mcscert.jpipe.model.Justification;
import ca.mcscert.jpipe.model.Unit;

import java.io.FileNotFoundException;
import org.apache.commons.cli.*;
import java.io.File;

public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    private static CommandLine setupCLI(String[] args) {
        Options options = new Options();
        Option input = new Option("i", "input", true, "input file path");
        input.setRequired(true);
        options.addOption(input);
    
        Option output = new Option("o", "output", true, "output file path");
        output.setRequired(true);
        options.addOption(output);
    
        Option diagram = new Option("d", "diagram", true, "diagram name");
        diagram.setRequired(true);
        options.addOption(diagram);
    
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
    
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("jpipe", options);
            System.exit(1);
        }
    
        return cmd;
    }
    
    public static void main(String[] args) {

        CommandLine cmd = setupCLI(args);
        String inputFile = cmd.getOptionValue("input");
        String outputFile = cmd.getOptionValue("output");
        String diagramName = cmd.getOptionValue("diagram");
    
        Compiler compiler = new Compiler();
        Unit unit = null;
        try {
            unit = compiler.compile(inputFile);
            
            boolean diagramExists = false;
            for (Justification j : unit.getJustificationSet()) {
                if (j.getName().equals(diagramName)) {
                    diagramExists = true;
                    break;
                }
            }
        
            if (!diagramExists) {
                System.err.println(ANSI_RED + "Diagram not found: " + diagramName + ANSI_RESET);
                System.exit(1);
            }
        
            File outputDirectory = new File(outputFile).getParentFile();
            if (!outputDirectory.exists()) {
                System.err.println(ANSI_RED + "Output directory does not exist: " + outputDirectory.getPath() + ANSI_RESET);
                System.exit(1);
            }
        
            Exportation<Justification> exporter = new DiagramExporter();
            for(Justification j: unit.getJustificationSet()) {
                if (j.getName().equals(diagramName)) {
                    String outputFilePath = removeFileExtension(outputFile) + "_" + j.getName() + ".png";
                    exporter.export(j, outputFilePath);
                }    
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println(ANSI_RED + "File not found: " + fnfe.getMessage() + ANSI_RESET);
            System.exit(1);
        } catch (CompilationError | TypeError | ExportationError err) {
            System.err.println(ANSI_RED + err.getMessage() + ANSI_RESET);
            System.exit(1);
        }
        System.exit(0);
    }
    
    private static String removeFileExtension(String filename) {
        // https://www.baeldung.com/java-filename-without-extension
        return filename.replaceAll("(?<!^)[.][^.]*$", "");
    }
}
