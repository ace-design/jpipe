package ca.mcscert.jpipe.operators;

import ca.mcscert.jpipe.compiler.MergeDiagram;
import ca.mcscert.jpipe.compiler.ModelCreationListener;
import ca.mcscert.jpipe.model.JustificationDiagram;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implement a "merge union" semantic on top of justification diagrams.
 */
public final class MergeOperator implements NaryOperator {

    private static final Logger logger = LogManager.getLogger(ModelCreationListener.class);

    @Override
    public JustificationDiagram apply(String name, List<JustificationDiagram> input) {
        logger.trace("OPERATOR -- MERGE");
        logger.trace("  inputs: "
                + input.stream().map(JustificationDiagram::name).collect(Collectors.toSet()));
        MergeDiagram merger = new MergeDiagram(name, input);
        return merger.getDiagram().build();

    }
}
