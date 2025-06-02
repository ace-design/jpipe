package ca.mcscert.jpipe.operators;

import ca.mcscert.jpipe.model.elements.*;

import java.util.*;

public class AssembleOperator extends CompositionOperator {

    @SuppressWarnings({"checkstyle:LeftCurly", "checkstyle:RightCurly"})
    @Override
    public String name() {
        return "assemble";
    }

    @Override
    protected boolean checkParameters(Map<String, String> params) {
        return params.containsKey("conclusionLabel");
    }

    @Override
    protected boolean checkInputs(List<JustificationModel> inputs) {
        return inputs.size() == 2;
    }

    @Override
    protected void execute(JustificationModel output, List<JustificationModel> inputs, Map<String, String> params) {
        System.out.println("Calling ASSEMBLE on " + inputs + "(" + params + ")");
        String conclusionLabel = params.get("conclusionLabel");
        // Create conclusion and add it
        Conclusion newConclusion = new Conclusion("c", conclusionLabel);
        output.add(newConclusion);
        // Create AND strat
        Strategy newStrategy = new Strategy("AND", "<AND>");
        newStrategy.supports(newConclusion);
        output.add(newStrategy);
        // Go through each justification models
        HashMap<JustificationElement, List<JustificationElement>> evi2Strats = new HashMap<>();
        for (JustificationModel justificationModel : inputs) {
            for (JustificationElement justificationElement: justificationModel.contents()){
                // Transform each conclusion --> sub-conclusion
                if(justificationElement instanceof Conclusion){
                    SubConclusion subConclusion = ((Conclusion) justificationElement).intoSubConclusion(newStrategy);
                    output.add(subConclusion);
                }
                else if(justificationElement instanceof Strategy){
                    output.add(justificationElement);
                    for(JustificationElement sup: justificationElement.getSupports()){
                        if(!(sup instanceof Evidence)){ continue; }
                        if(similarLabels(evi2Strats.keySet(), sup)){
                            getJustificationElement(evi2Strats,sup).add(justificationElement);
                        }
                        else {
                            evi2Strats.put(sup, new ArrayList<>(Collections.singletonList(justificationElement)));
                        }
                        justificationElement.removeSupport(sup);
                    }


                }
                else if(! (justificationElement instanceof Evidence)){
                    output.add(justificationElement);
                }
            }
        }

        // Merge evidences together
        for(JustificationElement justificationElement: evi2Strats.keySet()){
            if(evi2Strats.get(justificationElement).size() > 1){
                for(JustificationElement strat: evi2Strats.get(justificationElement)){

                    justificationElement.supports(strat);
                }
            }
            output.add(justificationElement);
        }



    }

    private boolean similarLabels(Set<JustificationElement> evi2Strats, JustificationElement justificationElement) {
        for(JustificationElement strat: evi2Strats){
            if(strat.getLabel().equals(justificationElement.getLabel())){
                return true;
            }
        }
        return false;
    }

    private List<JustificationElement> getJustificationElement(HashMap<JustificationElement, List<JustificationElement>> justificationElements, JustificationElement justificationElement) {
        for(JustificationElement JE: justificationElements.keySet()){
            if(JE.getLabel().equals(justificationElement.getLabel())){
                return justificationElements.get(JE);
            }
        }
        throw new IllegalArgumentException("No JustificationElement found for " + justificationElement.getLabel());
    }

}
