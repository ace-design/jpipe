package ca.mcscert.jpipe.operators;

import ca.mcscert.jpipe.error.SemanticError;
import ca.mcscert.jpipe.model.elements.AbstractSupport;
import ca.mcscert.jpipe.model.elements.Conclusion;
import ca.mcscert.jpipe.model.elements.JustificationElement;
import ca.mcscert.jpipe.model.elements.JustificationModel;
import ca.mcscert.jpipe.model.elements.Pattern;
import java.util.Collection;
import java.util.Set;

/**
 * Operationalize pattern implementation as a binary operator
 */
public class PatternImplementation
        implements BinaryOperator<JustificationModel, Pattern, JustificationModel> {

    private final SyntacticOverlap syntaxOverlap = new SyntacticOverlap();

    @SuppressWarnings("checkstyle:LeftCurly")
    @Override
    public JustificationModel apply(JustificationModel left, Pattern right) {

        JustificationModel result = left.replicate();
        Pattern pattern =  right.replicate();

        // Rewriting overlappings
        Collection<String> overlapping = syntaxOverlap.apply(result, pattern);
        for (String overlap : overlapping) {
            System.err.println("Replacing "  + overlap);
            pattern.replace(pattern.get(overlap), result.get(overlap));
        }
        result.setConclusion(pattern.getConclusion());
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
