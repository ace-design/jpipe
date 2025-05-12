package ca.mcscert.jpipe.operators.externals;

import ca.mcscert.jpipe.error.SemanticError;
import ca.mcscert.jpipe.model.elements.Justification;
import ca.mcscert.jpipe.model.elements.JustificationModel;
import ca.mcscert.jpipe.model.elements.Pattern;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

/**
 * Model what a composition operator is.
 * A composition operator is named, and takes two kind of input data: (1) the input models to be
 * used to run the composition, and (2) an optional set of parameters to configure how the
 * composition is done.
 */
public abstract class CompositionOperator {

    /**
     * Apply the operator, modifying the provided output (side effect). (template method)
     *
     * @param outputType type of the output (a justification model)
     * @param name the name of the justification to be created
     * @param inputs the justification models used as input
     * @param params the optional parameters provided;
     */
    public final JustificationModel run(CompositionOperator.ReturnType outputType,
                                        String name,
                                        List<JustificationModel> inputs,
                                        Map<String, String> params) {
        // Need to clone elements before composing.
        if (! checkParameters(params)) {
            throw new SemanticError("Invalid parameters for " + name());
        }
        if (! checkInputs(inputs)) {
            throw new SemanticError("Invalid inputs for " + name());
        }
        return execute(buildOutput(outputType, name), inputs, params);
    }

    /**
     * the name of the composition operator.
     *
     * @return the keyword to be used in the DSL to call this operator.
     */
    public abstract String name();

    /**
     * check if the parameters provided to the algorithms are consistent.
     *
     * @param params a set of key-value pairs to configure the operator.
     * @return true if the parameters are ok, false elsewhere.
     */
    protected abstract boolean checkParameters(Map<String, String> params);

    /**
     * check is the input models are consistent.
     *
     * @param inputs a set of justification models to be used
     * @return true if the parameters are ok, false elsewhere.
     */
    protected abstract boolean checkInputs(List<JustificationModel> inputs);

    /**
     * Execute the composition, internally. One can assume inputs and params were checked for
     * consistency beforehand.
     *
     * @param output an empty justification model to modify and return
     * @param inputs the justification models used as input
     * @param params the optional parameters provided;
     */
    protected abstract JustificationModel execute(JustificationModel output,
                                                  List<JustificationModel> inputs,
                                                  Map<String, String> params);


    /**
     * Allowed return types when calling operators.
     */
    public enum ReturnType {
        PATTERN, JUSTIFICATION
    }

    private JustificationModel buildOutput(CompositionOperator.ReturnType outputType,
                                           String name) {
        return switch (outputType) {
            case PATTERN       -> new Pattern(name);
            case JUSTIFICATION -> new Justification(name);
        };
    }

}
