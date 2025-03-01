package ca.mcscert.jpipe.operators.internals;

import ca.mcscert.jpipe.model.elements.JustificationElement;
import ca.mcscert.jpipe.model.elements.JustificationModel;
import ca.mcscert.jpipe.model.elements.Pattern;
import java.util.Collection;

/**
 * Create a justification model with Pattern x Model -> Model.
 */
public class PatternImplementation
        implements BinaryOperator<JustificationModel, Pattern, JustificationModel> {

    private final SyntacticOverlap syntaxOverlap = new SyntacticOverlap();

    @SuppressWarnings("checkstyle:LeftCurly")
    @Override
    public JustificationModel apply(JustificationModel left, Pattern right) {

        JustificationModel result = left.replicate();
        Pattern pattern =  right.replicate();

        // Rewriting overlapping elements
        Collection<String> overlapping = syntaxOverlap.apply(result, pattern);
        for (String overlap : overlapping) {
            pattern.replace(pattern.get(overlap), result.get(overlap));
        }

        // Loading all elements into the result
        for (JustificationElement patternElement : pattern.contents()) {
            if (overlapping.contains(patternElement.getIdentifier())) {
                continue;
           }
            result.add(patternElement);
        }
        return result;
    }

}
