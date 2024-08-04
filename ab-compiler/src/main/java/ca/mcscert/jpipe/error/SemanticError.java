package ca.mcscert.jpipe.error;

@JPipeError
public class SemanticError extends RuntimeException {


    public SemanticError(String message) {
        super(message);
    }

}
