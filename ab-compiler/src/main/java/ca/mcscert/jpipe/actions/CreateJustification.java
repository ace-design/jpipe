package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.model.Justification;
import ca.mcscert.jpipe.model.Unit;


public final class CreateJustification implements Action{

    private final String fileName;
    private final String identifier;

    public CreateJustification(String fileName, String identifier) {
        this.fileName = fileName;
        this.identifier = identifier;
    }

    @Override
    public void execute(Unit context) throws Exception {
        context.add(new Justification(this.identifier));
    }

    @Override
    public String toString() {
        return "CreateJustification{" +
                "fileName='" + fileName + '\'' +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
