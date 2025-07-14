package ca.mcscert.jpipe.operators;

import ca.mcscert.jpipe.model.RepTable;
import ca.mcscert.jpipe.model.elements.Conclusion;
import ca.mcscert.jpipe.model.elements.Evidence;
import ca.mcscert.jpipe.model.elements.JustificationElement;
import ca.mcscert.jpipe.model.elements.JustificationModel;
import ca.mcscert.jpipe.model.elements.Strategy;
import ca.mcscert.jpipe.model.elements.SubConclusion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Assembles different justification models together under a single conclusion.
 */
public class AssembleOperator extends CompositionOperator {

    private static final Logger logger = LogManager.getLogger();

    @SuppressWarnings({"checkstyle:LeftCurly", "checkstyle:RightCurly"})
    @Override
    public String name() {
        return "assemble";
    }

    @Override
    protected boolean checkParameters(Map<String, String> params) {
        return params.containsKey("conclusionLabel") && params.containsKey("strategyLabel");
    }

    @Override
    protected boolean checkInputs(List<JustificationModel> inputs) {
        return inputs.size() > 1;
    }

    @Override
    protected void execute(JustificationModel output, List<JustificationModel> inputs,
                           Map<String, String> params) {
        logger.info("Calling ASSEMBLE on {}({})", inputs, params);
        String conclusionLabel = params.get("conclusionLabel");
        String strategyLabel = params.get("strategyLabel");
        // Create conclusion and add it
        Conclusion newConclusion = new Conclusion("c", conclusionLabel);
        output.add(newConclusion);
        // Create AND strat
        Strategy newStrategy = new Strategy("AND", strategyLabel);
        newStrategy.supports(newConclusion);
        output.add(newStrategy);
        RepTable<JustificationElement> representations = new RepTable<>();
        // Go through each justification models
        HashMap<JustificationElement, List<JustificationElement>> evi2Strats = new HashMap<>();
        for (JustificationModel justificationModel : inputs) {
            representations.recordAll(justificationModel.representations());
            for (JustificationElement justificationElement : justificationModel.contents()) {
                // Transform each conclusion --> sub-conclusion
                if (justificationElement instanceof Conclusion) {
                    SubConclusion subConclusion = ((Conclusion) justificationElement)
                            .intoSubConclusion(newStrategy);
                    output.add(subConclusion, justificationModel.representations().getAllParents(justificationElement));
                } else if (justificationElement instanceof Strategy) {
                    output.add(justificationElement,
                            justificationModel.representations().getAllParents(justificationElement));
                    for (JustificationElement sup : justificationElement.getSupports()) {
                        if (!(sup instanceof Evidence)) {
                            continue;
                        }
                        if (similarLabels(evi2Strats.keySet(), sup)) {
                            getJustificationElement(evi2Strats, sup).add(justificationElement);
                        } else {
                            evi2Strats.put(sup, new ArrayList<>(
                                    Collections.singletonList(justificationElement)));
                        }
                        justificationElement.removeSupport(sup);
                    }
                } else if (justificationElement instanceof SubConclusion) {
                    output.add(justificationElement,  justificationModel.representations().getAllParents(justificationElement));
                }
            }
        }
        // Merge evidences together
        for (JustificationElement justificationElement : evi2Strats.keySet()) {
            if (evi2Strats.get(justificationElement).size() > 1) {
                for (JustificationElement strat : evi2Strats.get(justificationElement)) {
                    justificationElement.supports(strat);
                }
            }
            output.add(justificationElement,  representations.getAllParents(justificationElement));
            justificationElement.supports(evi2Strats.get(justificationElement).getFirst());
        }
    }

    private boolean similarLabels(Set<JustificationElement> evi2Strats,
                                  JustificationElement justificationElement) {
        for (JustificationElement strat : evi2Strats) {
            if (strat.getLabel().equals(justificationElement.getLabel())) {
                return true;
            }
        }
        return false;
    }

    private List<JustificationElement> getJustificationElement(
            HashMap<JustificationElement, List<JustificationElement>> justificationElements,
            JustificationElement justificationElement) {
        for (JustificationElement e : justificationElements.keySet()) {
            if (e.getLabel().equals(justificationElement.getLabel())) {
                return justificationElements.get(e);
            }
        }
        throw new IllegalArgumentException("No JustificationElement found for "
                + justificationElement.getLabel());
    }

}
