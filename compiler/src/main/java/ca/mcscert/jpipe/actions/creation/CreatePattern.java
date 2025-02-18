package ca.mcscert.jpipe.actions.creation;

import ca.mcscert.jpipe.actions.RegularAction;
import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.model.elements.Pattern;

/**
 * Create a Pattern inside a unit.
 */
public final class CreatePattern extends RegularAction {

    private final String fileName;
    private final String identifier;
    private final String parent;

    /**
     * Provides information necessary to create a pattern.
     *
     * @param fileName the file name containing the justification.
     * @param identifier the identified to be used to access it.
     */
    public CreatePattern(String fileName, String identifier) {
        this.fileName = fileName;
        this.identifier = identifier;
        this.parent = null;
    }

    /**
     * Provides information necessary to create a pattern.
     *
     * @param fileName the file name containing the justification.
     * @param identifier the identified to be used to access it.
     */
    public CreatePattern(String fileName, String identifier, String parentId) {
        this.fileName = fileName;
        this.identifier = identifier;
        this.parent = parentId;
    }

    @Override
    public void execute(Unit context) throws Exception {
        context.add(new Pattern(this.identifier));
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CreatePattern{");
        sb.append("fileName='").append(fileName).append('\'');
        sb.append(", identifier='").append(identifier).append('\'');
        sb.append(", parent='").append(parent).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
