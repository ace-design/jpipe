package ca.mcscert.jpipe.compiler.steps;

import ca.mcscert.jpipe.compiler.model.Transformation;
import java.io.IOException;
import java.io.InputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

public final class CharStreamProvider extends Transformation<InputStream, CharStream> {

    @Override
    protected CharStream run(InputStream input, String source) throws IOException {
        return CharStreams.fromStream(input);
    }
}
