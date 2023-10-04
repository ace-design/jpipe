package ca.mcscert.jpipe.model.justification;

import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.visitors.AbstractVisitor;

/**
 * Data structure representing a Justification pattern.
 *
 * @param name the name of the pattern
 * @param conclusion it's entry point.
 */
public record JustificationPattern(String name, Conclusion conclusion)
        implements JustificationDiagram {

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }

}
