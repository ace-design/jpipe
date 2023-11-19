package ca.mcscert.jpipe.operators;

import ca.mcscert.jpipe.model.JustificationDiagram;
import java.util.Set;

/**
 * Model an n-ary composition operator.
 */
public interface NaryOperator {

    /**
     * Apply the operator on Justification Diagrams, taking a set of inputs.
     *
     * @param input the set of justification diagrams to compose
     * @return the result of the composition operator
     */
    JustificationDiagram apply(Set<JustificationDiagram> input);

}
