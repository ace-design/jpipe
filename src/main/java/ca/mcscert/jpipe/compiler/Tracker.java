package ca.mcscert.jpipe.compiler;

import ca.mcscert.jpipe.model.justification.JustificationElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Keeps track of identifiers and JusitificationElements from merging diagrams.
 */
public class Tracker {

  private static final Logger logger = LogManager.getLogger(Tracker.class);

  // Keeps track of equal elements across all given diagrams.
  Map<JustificationElement, Map<String, String>> equalElements = new HashMap<>();


  /**
   * Keeps track of current diagram, and associates all elements to given diagram.
   *
   * @param diagramName name of given diagram.
   * @param elements    elements of given diagram.
   */
  public void addDiagram(String diagramName, Map<String, JustificationElement> elements) {
    logger.trace("Tracking diagram [" + diagramName + "]");

    for (JustificationElement element : elements.values()) {
      if (equalElements.containsKey(element)) {
        //Log which diagram elements the new element is the same as.
        logTracker(diagramName, element);
        equalElements.get(element).put(diagramName, element.getIdentifier());
      } else {
        Map<String, String> identifier = new HashMap<>();
        identifier.put(diagramName, element.getIdentifier());
        equalElements.put(element, identifier);
      }
    }
  }


  private void logTracker(String newDiagram, JustificationElement newElement) {

    for (String trackedDiagram : equalElements.get(newElement).keySet()) {
      String message =
          "Element " + newElement.getIdentifier() + " of diagram " + newDiagram
              + " is the same as ";
      String elementName = equalElements.get(newElement).get(trackedDiagram);
      logger.trace(message + elementName + " of diagram " + trackedDiagram);
    }
  }


  // Retrieve the identifier of a justification element for a specific diagram.
  // JustificationElement has defined equivalence relation that will allow this.
  public String getIdentifier(JustificationElement element, String diagramName) {
    return equalElements.get(element).get(diagramName);
  }


  // The key set represents all unique elements. Merged diagram requires all unique keys.
  public Set<JustificationElement> getUniqueElements() {
    return equalElements.keySet();
  }
}
