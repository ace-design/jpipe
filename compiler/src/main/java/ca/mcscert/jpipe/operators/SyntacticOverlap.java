package ca.mcscert.jpipe.operators;

import ca.mcscert.jpipe.model.elements.JustificationElement;
import ca.mcscert.jpipe.model.elements.JustificationModel;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SyntacticOverlap
        implements BinaryOperator<JustificationModel, JustificationModel, Set<String>> {

    @Override
    public Set<String> apply(JustificationModel left, JustificationModel right) {
        Set<String> leftSymbols = left.contents().stream()
                                        .map(JustificationElement::getIdentifier)
                                        .collect(Collectors.toSet());
        Set<String> rightSymbols = right.contents().stream()
                .map(JustificationElement::getIdentifier)
                .collect(Collectors.toSet());
        Set<String> result = new HashSet<>(leftSymbols);
        result.retainAll(rightSymbols);
        return result;
    }
}
