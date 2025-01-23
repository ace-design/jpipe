package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.model.elements.Justification;
import ca.mcscert.jpipe.model.elements.Pattern;
import ca.mcscert.jpipe.model.Unit;

/**
 * Create a justification inside a unit.
 */
public final class CreateJustification implements Action {

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
        if (this.parent == null) {
            context.add(new Justification(this.identifier));
        } else {
            throw new UnsupportedOperationException("Pattern implementation not suppoerted yet");
        }
    }

    @Override
    public String toString() {
        return "CreateJustification{" + "identifier='" + identifier + '\''
                + ", parent='" + parent + '\'' + '}';
    }
}
