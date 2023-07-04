import ca.mcscert.jpipe.compiler.CompilationError;
import ca.mcscert.jpipe.compiler.Compiler;
import ca.mcscert.jpipe.compiler.TypeError;
import ca.mcscert.jpipe.exporters.DiagramExporter;
import ca.mcscert.jpipe.exporters.Exportation;
import ca.mcscert.jpipe.exporters.ExportationError;
import ca.mcscert.jpipe.model.Justification;
import ca.mcscert.jpipe.model.Unit;

import java.io.FileNotFoundException;



public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {

        String file = args[0];
        Compiler compiler = new Compiler();
        Unit unit = null;
        try {
            unit = compiler.compile(file);
            Exportation<Justification> exporter = new DiagramExporter();
            for(Justification j: unit.getJustificationSet()) {
                String outputFile = removeFileExtension(file) + "_" + j.getName() + ".png";
                exporter.export(j, outputFile);
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println(ANSI_RED + "File not found: " + fnfe.getMessage() + ANSI_RESET);
            System.exit(1);
            return;
        } catch (CompilationError | TypeError | ExportationError err) {
            System.err.println(ANSI_RED + err.getMessage() + ANSI_RESET);
            System.exit(1);
            return;
        }
        System.exit(0);
    }


    private static String removeFileExtension(String filename) {
        // https://www.baeldung.com/java-filename-without-extension
        return filename.replaceAll("(?<!^)[.][^.]*$", "");
    }

}
