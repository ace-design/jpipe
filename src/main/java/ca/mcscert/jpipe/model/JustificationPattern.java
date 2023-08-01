package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractVisitor;

public record JustificationPattern(String name, Conclusion conclusion) implements JustificationDiagram {

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }

}
