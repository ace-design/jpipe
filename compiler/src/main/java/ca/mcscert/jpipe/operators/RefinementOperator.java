package ca.mcscert.jpipe.operators;

import ca.mcscert.jpipe.model.elements.JustificationModel;

import java.util.List;
import java.util.Map;

public class RefinementOperator extends CompositionOperator {

    @SuppressWarnings({"checkstyle:LeftCurly", "checkstyle:RightCurly"})
    @Override
    public String name() {
        return "refine";
    }

    @Override
    protected boolean checkParameters(Map<String, String> params) {
        return false;
    }

    @Override
    protected boolean checkInputs(List<JustificationModel> inputs) {
        return false;
    }

    @Override
    protected void execute(JustificationModel output, List<JustificationModel> inputs, Map<String, String> params) {
        System.out.println("Calling REFINE on " + inputs + "(" + params + ")");
    }
}
