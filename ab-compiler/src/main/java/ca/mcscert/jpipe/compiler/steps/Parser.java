package ca.mcscert.jpipe.compiler.steps;

import ca.mcscert.jpipe.compiler.model.Transformation;
import ca.mcscert.jpipe.error.ParsingError;
import ca.mcscert.jpipe.syntax.JPipeParser;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;

public final class Parser extends Transformation<CommonTokenStream, ParseTree> {

    private final List<Throwable> errors;

    public Parser(List<Throwable> errors) {
        this.errors = errors;
    }

    @Override
    protected ParseTree run(CommonTokenStream tokens, String source) throws Exception {
        JPipeParser parser = new JPipeParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ParsingErrorListener(errors, source));
        return parser.unit();
    }

    private static class ParsingErrorListener extends BaseErrorListener {

        private final List<Throwable> errors;
        private final String fileName;


        public ParsingErrorListener(List<Throwable> errors, String fileName) {
            this.fileName = fileName;
            this.errors = errors;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                int line, int charPositionInLine, String msg,
                                RecognitionException e) {
            errors.add(new ParsingError(fileName, line, charPositionInLine, msg));
        }


    }


}
