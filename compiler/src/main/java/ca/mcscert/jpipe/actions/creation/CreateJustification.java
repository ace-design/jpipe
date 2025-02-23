package ca.mcscert.jpipe.actions.creation;

import ca.mcscert.jpipe.actions.RegularAction;
import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.model.elements.Justification;

/**
 * Create a justification inside a unit.
 */
public final class CreateJustification extends RegularAction {

    private final String fileName;
    private final String identifier;
    private final String parent;

    /**
     * Provides information necessary to create a justification.
     *
     * @param fileName the file name containing the justification.
     * @param identifier the identified to be used to access it.
     */
    public CreateJustification(String fileName, String identifier) {
        this.fileName = fileName;
        this.identifier = identifier;
        this.parent = null;
    }

    /**
     * Provides information necessary to create a justification.
     *
     * @param fileName the file name containing the justification.
     * @param identifier the identified to be used to access it.
     */
    public CreateJustification(String fileName, String identifier, String parentId) {
        this.fileName = fileName;
        this.identifier = identifier;
        this.parent = parentId;
    }


    @Override
    public void execute(Unit context) throws Exception {
        context.add(new Justification(this.identifier));
    }

    @Override
    public String toString() {
        return "CreateJustification{" + "identifier='" + identifier + '\''
                + ", parent='" + parent + '\'' + '}';
    }
}
