package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.model.Justification;
import ca.mcscert.jpipe.model.Pattern;
import ca.mcscert.jpipe.model.Unit;

/**
 * Create a Pattern inside a unit.
 */
public final class CreatePattern implements Action {

    private final String fileName;
    private final String identifier;

    /**
     * Provides information necessary to create a pattern.
     *
     * @param fileName the file name containing the justification.
     * @param identifier the identified to be used to access it.
     */
    public CreatePattern(String fileName, String identifier) {
        this.fileName = fileName;
        this.identifier = identifier;
    }

    @Override
    public void execute(Unit context) throws Exception {
        context.add(new Pattern(this.identifier));
    }

    @Override
    public String toString() {
        return "CreatePattern{"
                + "fileName='" + fileName + '\''
                + ", identifier='" + identifier + '\''
                + '}';
    }
}
