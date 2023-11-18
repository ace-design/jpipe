package ca.mcscert.jpipe.compiler.builders;

import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.model.justification.ConcreteJustification;
import ca.mcscert.jpipe.model.justification.Evidence;
import ca.mcscert.jpipe.model.justification.JustificationElement;
import ca.mcscert.jpipe.model.justification.Strategy;
import ca.mcscert.jpipe.model.justification.SubConclusion;

/**
 * Builder specialization in charge of building the AST of a concrete justification diagram.
 * We use a template method pattern here (template root is JustificationBuilder).
 */
public class ConcreteJustificationBuilder extends ScopedContextBuilder {

    /**
     * Constructor for this justification builder.
     *
     * @param name the name of the justification diagram to be built.
     */
    public ConcreteJustificationBuilder(String name) {
        super(name);
    }

    /**
     * Create the JustificationDiagram element.
     *
     * @return a JustificationDiagram
     */
    public JustificationDiagram build() {
        this.line = -1;
        this.character = -1;
        logger.trace("Finalizing build of Justification [" + this.name + "]");
        JustificationDiagram result = new ConcreteJustification(this.name, this.conclusion);
        fill(this.conclusion);
        logger.trace("Finalization complete! [" + this.name + "]");
        return result;
    }


    @Override
    public void checkConclusionPredecessor(JustificationElement e) {
        if (!(e instanceof Strategy)) {
            error("A Conclusion can only be supported by strategies, but [" + e.getIdentifier()
                    + "] is not a Strategy)");
        }
    }

    @Override
    public void checkStrategyPredecessor(JustificationElement e) {
        if (!((e instanceof SubConclusion) || (e instanceof Evidence))) {
            error("A Strategy can only be supported by a sub-conclusion or an evidence, but ["
                    + e.getIdentifier() + "] is a " + e.getClass().getCanonicalName() + " )");
        }
    }

    @Override
    public void checkSubConclusionPredecessor(JustificationElement e) {
        if (!(e instanceof Strategy)) {
            error("A Conclusion can only be supported by strategies, but ["
                    + e.getIdentifier() + "] is not a Strategy)");
        }
    }
}
