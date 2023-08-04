package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.model.justification.JustificationElement;
import ca.mcscert.jpipe.visitors.AbstractVisitor;

public class Load implements Visitable{

    private final String fileName;

    public Load(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }
}
