package ca.mcscert.jpipe.exporters;

/**
 * Specialized error to represent error happening when exporting a diagram.
 */
public class ExportationError extends RuntimeException {

    public ExportationError(String message) {
        super(message);
    }
}
