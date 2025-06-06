package ca.mcscert.jpipe.operators;

import ca.mcscert.jpipe.model.elements.Conclusion;
import ca.mcscert.jpipe.model.elements.JustificationElement;
import ca.mcscert.jpipe.model.elements.JustificationModel;
import ca.mcscert.jpipe.model.elements.SubConclusion;

import java.util.ArrayList;
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
        return params.containsKey("hook") && params.get("hook").contains(":");
    }

    @Override
    protected boolean checkInputs(List<JustificationModel> inputs) {
        return inputs.size() == 2;
    }

    // inputs[0] = original JM
    // inputs[1] = refinement JM
    @Override
    protected void execute(JustificationModel output, List<JustificationModel> inputs, Map<String, String> params) {
        System.out.println("Calling REFINE on " + inputs + "(" + params + ")");

        // Find hook
        String[] hook = params.get("hook").split(":");

        JustificationModel original = hook[0].equals(inputs.getFirst().getName()) ? inputs.getFirst() : inputs.getLast();
        JustificationModel hookModel = inputs.getFirst().equals(original) ? inputs.getLast() : inputs.getFirst();
        JustificationElement hookElement = original.get(hook[1]);
        List<JustificationElement> supported = new ArrayList<>();
        for(JustificationElement JE: original.contents()){
            if(!JE.fullyQualifiedName().equals(hookElement.fullyQualifiedName())){
                output.add(JE);
            }
            if(JE.getSupports().contains(hookElement)){
                supported.add(JE);
            }
        }

        supported.getFirst().removeSupport(hookElement);
        // Replace evidence with sub-conclusion
        // Add all elements to the output

        Conclusion con = null;
        for(JustificationElement JE: hookModel.contents()){
            if(!(JE instanceof Conclusion)){
                output.add(JE);
            }
            else{
                con = (Conclusion) JE;
            }
        }

        assert con != null;
        SubConclusion newSubCon = con.intoSubConclusion(); // might need to change
        output.add(newSubCon);
        for(JustificationElement JE: supported){
            JE.removeSupport(con);
            newSubCon.supports(JE);
        }
    }
}
