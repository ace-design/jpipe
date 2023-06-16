import ca.mcscert.jpipe.compiler.Compiler;
import ca.mcscert.jpipe.model.CompilationUnit;
import ca.mcscert.jpipe.model.JustifiedPipeline;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        System.out.println("jPipe compiler");
        String file = args[0];
        Compiler compiler = new Compiler();
        CompilationUnit unit = null;
        try {
            unit = compiler.compile(file);
        } catch (FileNotFoundException fnfe) {
            System.err.println("File not found: " + fnfe.getMessage());
            System.exit(1);
            return;
        }
        System.out.println(unit);
        System.exit(0);
    }

}
