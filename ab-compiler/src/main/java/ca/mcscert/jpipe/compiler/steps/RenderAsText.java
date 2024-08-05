package ca.mcscert.jpipe.compiler.steps;

import ca.mcscert.jpipe.compiler.model.Transformation;
import guru.nidi.graphviz.model.MutableGraph;

/**
 * Render a graphviz mutable graph into its textual representation.
 */
public class RenderAsText extends Transformation<MutableGraph, String> {

    @Override
    protected String run(MutableGraph input, String source) throws Exception {
        return input.toString();
    }

}
