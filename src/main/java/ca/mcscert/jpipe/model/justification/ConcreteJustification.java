package ca.mcscert.jpipe.model.justification;

import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.visitors.AbstractVisitor;

/**
 * Data class to represent a concrete justification.
 * A concrete justification is a named entity pointing to its final conclusion.
 *
 * @param name the name of the concrete justification element
 * @param conclusion it's entry point (final conclusion)
 */
public record ConcreteJustification(String name, Conclusion conclusion)
        implements JustificationDiagram {


    public static Conclusion template_conclusion;

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }

}
