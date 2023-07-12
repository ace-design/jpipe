import ca.mcscert.jpipe.compiler.CompilationError;
import ca.mcscert.jpipe.compiler.Compiler;
import ca.mcscert.jpipe.compiler.TypeError;
import ca.mcscert.jpipe.exporters.DiagramExporter;
import ca.mcscert.jpipe.exporters.Exportation;
import ca.mcscert.jpipe.exporters.ExportationError;
import ca.mcscert.jpipe.model.Justification;
import ca.mcscert.jpipe.model.Unit;

import org.apache.commons.cli.*;
import java.io.File;
import java.io.FileNotFoundException;

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
        CommandLine cmd;
    
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("jpipe", options);
            throw new RuntimeException("Error parsing command line arguments", e);
        }
    
        return cmd;
    }
    
    public static void main(String[] args) {
        CommandLine cmd = setupCLI(args);
        String inputFile = cmd.getOptionValue("input");
        String outputFile = cmd.getOptionValue("output");
        String diagramName = cmd.getOptionValue("diagram");
        
        Compiler compiler = new Compiler();
        Unit unit;
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
                throw new IllegalArgumentException(ANSI_RED + "Diagram not found: " + diagramName + ANSI_RESET);
            }
        
            File outputDirectory = new File(outputFile).getParentFile();
            if (!outputDirectory.exists()) {
                throw new IllegalArgumentException(ANSI_RED + "Output directory does not exist: " + outputDirectory.getPath() + ANSI_RESET);
            }
        
            Exportation<Justification> exporter = new DiagramExporter();
            for(Justification j: unit.getJustificationSet()) {
                if (j.getName().equals(diagramName)) {
                    String outputFilePath = removeFileExtension(outputFile) + "_" + j.getName() + ".png";
                    exporter.export(j, outputFilePath);
                }    
            }
        } catch (FileNotFoundException e) {
            System.err.println(ANSI_RED + "File not found: " + e.getMessage() + ANSI_RESET);
        } catch (CompilationError | TypeError | ExportationError e) {
            System.err.println(ANSI_RED + e.getMessage() + ANSI_RESET);
        }
    }
    
    private static String removeFileExtension(String filename) {
        return filename.replaceAll("(?<!^)[.][^.]*$", "");
    }
}

