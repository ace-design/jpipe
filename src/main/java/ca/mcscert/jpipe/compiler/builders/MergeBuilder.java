package ca.mcscert.jpipe.compiler.builders;

import ca.mcscert.jpipe.compiler.CompositionError;
import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.operators.MergeOperator;
import ca.mcscert.jpipe.operators.NaryOperator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Builder element to accumulate elements to merge and then trigger the operator.
 */
public class MergeBuilder {

    private final Set<String> contents;
    private final Map<String, JustificationDiagram> known;

    private final String name;

    /**
     * Instantiate a merge builder, considering the justification diagram we already know.
     *
     * @param encountered the known diagrams
     */
    public MergeBuilder(String diagramName, List<JustificationDiagram> encountered) {
        this.name = diagramName;
        this.contents = new HashSet<>();
        this.known = encountered.stream()
                        .collect(Collectors.toMap(JustificationDiagram::name, Function.identity()));
    }

    /**
     * Register a new justification diagram id to be part of this composition.
     *
     * @param id the id to register (must be known)
     */
    public void register(String id) {
        if (! known.containsKey(id)) {
            error("Cannot compose unknown justification [" + id + "]");
        }
        this.contents.add(id);
    }

    /**
     * Error mechanism if merge is not consistent at parsing level.
     *
     * @param message the error message to send to the user
     */
    protected final void error(String message) {
        throw new CompositionError(message);
    }

    /**
     * Finalize the build by merging the diagrams and storing the result.
     *
     * @return the merged diagram
     */
    public JustificationDiagram build() {
        List<JustificationDiagram> toMerge =
                contents.stream().map(known::get).collect(Collectors.toList());
        NaryOperator merge = new MergeOperator();
        return merge.apply(this.name, toMerge);
    }

}
