package ca.mcscert.jpipe.compiler.steps.io;

import ca.mcscert.jpipe.cli.Configuration;
import ca.mcscert.jpipe.compiler.model.Sink;
import ca.mcscert.jpipe.error.ErrorManager;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Flush the content of a string builder into a file (or stdout).
 */
public class FileWriter implements Sink<StringBuilder> {

    @Override
    public void pourInto(StringBuilder output, String fileName) throws IOException {

        if (fileName.equals(Configuration.STDOUT_PATH)) {
            System.out.println(output.toString());
            System.out.flush();
        } else {
            try (PrintStream fos = new PrintStream(fileName)) {
                fos.println(output.toString());
                fos.flush();
            } catch (IOException ioe) {
                ErrorManager.getInstance().fatal(ioe);
            }
        }
    }

}
