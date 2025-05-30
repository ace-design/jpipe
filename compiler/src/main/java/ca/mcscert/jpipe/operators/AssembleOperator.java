package ca.mcscert.jpipe.operators;

import ca.mcscert.jpipe.model.elements.*;

import java.util.List;
import java.util.Map;

public class AssembleOperator extends CompositionOperator {

    @SuppressWarnings({"checkstyle:LeftCurly", "checkstyle:RightCurly"})
    @Override
    public String name() {
        return "assemble";
    }

    @Override
    protected boolean checkParameters(Map<String, String> params) {
        return params.containsKey("conclusion");
    }

    @Override
    protected boolean checkInputs(List<JustificationModel> inputs) {
        return inputs.size() == 2;
    }

    @Override
    protected void execute(JustificationModel output, List<JustificationModel> inputs, Map<String, String> params) {
        System.out.println("Calling ASSEMBLE on " + inputs + "(" + params + ")");
        String conclusionLabel = params.get("conclusion");
        // Create conclusion and add it
        Conclusion newConclusion = new Conclusion("c", conclusionLabel);
        output.add(newConclusion);
        // Create AND strat
        Strategy newStrategy = new Strategy("and", "<AND>");
        newStrategy.supports(newConclusion);
        output.add(newStrategy);
        // Go through each justification models
        for (JustificationModel justificationModel : inputs) {
            for (JustificationElement justificationElement: justificationModel.contents()){
                // Transform each conclusion --> sub-conclusion
                JustificationElement e = justificationElement;
                if(e instanceof Conclusion){
                    SubConclusion subConclusion = ((Conclusion) e).intoSubConclusion();
                    output.add(subConclusion);
                }
                else{
                    output.add(e);
                }
                // add each element to the output
                // if element is an evidence, merge with others
                // could keep a set of all evidences in the output

            }
        }



    }
}
