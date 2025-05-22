package ca.mcscert.jpipe.operators.internals;

import ca.mcscert.jpipe.model.elements.JustificationElement;
import ca.mcscert.jpipe.model.elements.JustificationModel;

import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.commons.lang3.tuple.Pair;

// No implementation since it does not allow for threshold / integer argument
public class HeuristicSimilarity{

    // Using this library: https://github.com/tdebatty/java-string-similarity
    /**
     *
     * smaller graph element key --> Bigger graph element key
     */
    public Map<String, String> apply(JustificationModel left, JustificationModel right, int threshold) {
        Map<String, String> leftMap = left.contents().stream()
                .collect(Collectors.toMap(
                        JustificationElement::getIdentifier,
                        JustificationElement::getLabel
                ));

        Map<String, String> rightMap = right.contents().stream()
                .collect(Collectors.toMap(
                        JustificationElement::getIdentifier,
                        JustificationElement::getLabel
                ));
        System.out.println(leftMap);
        System.out.println(rightMap);
        return leftMap.size() > rightMap.size() ? retainAllFuzzy(leftMap, rightMap, threshold) :
                retainAllFuzzy(rightMap, leftMap, threshold);

    }

    public boolean checkValidMethod(String string){
        return string.equalsIgnoreCase("levenshtein");
    }

    // Could use this library: https://github.com/tdebatty/java-string-similarity
    private static Map<String, String> retainAllFuzzy(Map<String, String> mapA, Map<String, String> mapB, int thresholdPercentage) {
        Map<String, String> retained = new HashMap<>();
        LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();
        for (Map.Entry<String, String> entryA : mapA.entrySet()) {
            String idA = entryA.getKey();
            String labelA = entryA.getValue();

            for (Map.Entry<String, String> entryB : mapB.entrySet()) {
                String idB = entryB.getKey();
                String labelB = entryB.getValue();

                int distance = levenshteinDistance.apply(labelA, labelB);
                double distancePercentage = labelA.length() > labelB.length() ?
                        (double) distance / (double) labelA.length() : (double) distance / (double) labelB.length() ;


                if (distancePercentage <= ((double) thresholdPercentage / 100)) {
                    retained.put(idA, idB);
                    break;
                }
            }
        }

        return retained;
    }



    // If needed
    private void levenshteinSimilarity(String left, String right, int k) {
        return;
    }
}
