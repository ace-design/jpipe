package ca.mcscert.jpipe.compiler;

public class TypeError extends RuntimeException {

    public TypeError(String message) {
        super("[TypeSystem] " + message);
    }
}
