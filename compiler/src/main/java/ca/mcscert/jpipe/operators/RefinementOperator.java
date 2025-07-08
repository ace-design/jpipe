package ca.mcscert.jpipe.operators;

import ca.mcscert.jpipe.model.elements.Conclusion;
import ca.mcscert.jpipe.model.elements.JustificationElement;
import ca.mcscert.jpipe.model.elements.JustificationModel;
import ca.mcscert.jpipe.model.elements.SubConclusion;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Refines an evidence with a justification model.
 */
public class RefinementOperator extends CompositionOperator {

    @SuppressWarnings({"checkstyle:LeftCurly", "checkstyle:RightCurly"})
    @Override
    public String name() {
        return "refine";
    }

    @Override
    protected boolean checkParameters(Map<String, String> params) {
        return params.containsKey("hook");
    }

    @Override
    protected boolean checkInputs(List<JustificationModel> inputs) {
        return inputs.size() == 2;
    }

    // inputs[0] = JM with the evidence to refine
    // inputs[1] = JM which attaches to an evidence
    // hook: Label of the hook
    @Override
    protected void execute(JustificationModel output, List<JustificationModel> inputs,
                           Map<String, String> params) {
        System.out.println("Calling REFINE on " + inputs + "(" + params + ")");

        // Find hook
        String hook = params.get("hook");

        JustificationModel original = inputs.getFirst();
        JustificationModel hookModel = inputs.getLast();
        JustificationElement hookElement = findJustificationModelbyLabel(original, hook);
        List<JustificationElement> supported = new ArrayList<>();
        for (JustificationElement je : original.contents()) {
            if (!je.getLabel().equals(hook)) {
                output.add(je, original.representations().get(je));
            }
            if (je.getSupports().contains(hookElement)) {
                supported.add(je);
            }
        }
        if (!supported.isEmpty()) {
            for (JustificationElement je : supported) {
                je.removeSupport(hookElement);
            }
        }
        // Replace evidence with sub-conclusion
        // Add all elements to the output

        Conclusion con = null;
        for (JustificationElement je : hookModel.contents()) {
            if (!(je instanceof Conclusion)) {
                output.add(je, hookModel.representations().get(je));
            } else {
                con = (Conclusion) je;
            }
        }

        assert con != null;
        SubConclusion newSubCon = con.intoSubConclusion(null); // might need to change
        output.add(newSubCon, original.representations().get(hookElement));
        for (JustificationElement je : supported) {
            je.removeSupport(con);
            newSubCon.supports(je);
        }
    }

    private JustificationElement findJustificationModelbyLabel(JustificationModel jm,
                                                               String label) {
        for (JustificationElement je : jm.contents()) {
            if (je.getLabel().equals(label)) {
                return je;
            }
        }
        return null;
    }
}
