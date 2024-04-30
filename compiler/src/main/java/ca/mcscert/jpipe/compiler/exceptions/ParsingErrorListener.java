package ca.mcscert.jpipe.compiler.exceptions;

import ca.mcscert.jpipe.compiler.exceptions.ParsingError;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * Extending the default error listener provided by ANTLR to crash the parsing step in case
 * of errors.
 */
public class ParsingErrorListener extends BaseErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg,
                            RecognitionException e) {
        throw new ParsingError(line, charPositionInLine, msg);
    }

}
