package ca.mcscert.jpipe.model.justification;

import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.visitors.AbstractVisitor;

public record ConcreteJustification(String name, Conclusion conclusion) implements JustificationDiagram {

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }

}
