package ca.mcscert.jpipe.compiler.steps.io;

import ca.mcscert.jpipe.compiler.model.Sink;
import java.io.IOException;

public class StdoutPrinter implements Sink<String> {

    @Override
    public void pourInto(String input, String source) throws IOException {
        System.out.println(input);
    }

}
