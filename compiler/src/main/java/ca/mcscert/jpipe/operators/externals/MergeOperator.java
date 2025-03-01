package ca.mcscert.jpipe.operators.externals;

import ca.mcscert.jpipe.model.elements.JustificationModel;
import java.util.List;
import java.util.Map;

/**
 * Merge operator.
 */
public class MergeOperator extends CompositionOperator {

    @SuppressWarnings({"checkstyle:LeftCurly", "checkstyle:RightCurly"})
    @Override public String name() { return "merge"; }

    @Override
    protected boolean checkParameters(Map<String, String> params) {
        return true;
    }

    @Override
    protected boolean checkInputs(List<JustificationModel> inputs) {
        return true;
    }

    @Override
    protected JustificationModel execute(JustificationModel output, List<JustificationModel> inputs,
                           Map<String, String> params) {
        System.out.println("Calling MERGE on " + inputs + "(" + params + ")");
        return output;
    }

}
