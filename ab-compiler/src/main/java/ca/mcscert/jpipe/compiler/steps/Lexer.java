package ca.mcscert.jpipe.compiler.steps;

import ca.mcscert.jpipe.compiler.model.Transformation;
import ca.mcscert.jpipe.error.LexingError;
import ca.mcscert.jpipe.syntax.JPipeLexer;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public final class Lexer extends Transformation<CharStream, CommonTokenStream> {

    private final List<Throwable> errors;

    public Lexer(List<Throwable> errors) {
        this.errors = errors;
    }

    @Override
    protected CommonTokenStream run(CharStream input, String source) throws Exception {
        JPipeLexer lexer = new JPipeLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new LexerErrorListener(errors, source));
        return new CommonTokenStream(lexer);
    }

    private static class LexerErrorListener extends BaseErrorListener {

        private final List<Throwable> errors;
        private final String fileName;

        public LexerErrorListener(List<Throwable> errors, String fileName) {
            this.errors = errors;
            this.fileName = fileName;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                int line, int charPositionInLine, String msg,
                                RecognitionException e) {
            errors.add(new LexingError(fileName, line, charPositionInLine, msg));
        }

    }

}
