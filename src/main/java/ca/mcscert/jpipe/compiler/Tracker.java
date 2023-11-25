package ca.mcscert.jpipe.compiler;

import ca.mcscert.jpipe.model.justification.JustificationElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tracker {

  private static Logger logger = LogManager.getLogger(ModelCreationListener.class);
  Map<String, Map<String, List<String>>> allDependencies = new HashMap<>();

  Map<JustificationElement, Map<String, String>> equalElements = new HashMap<>();

  public void addDiagram(String diagramName, Map<String, JustificationElement> elements) {
    if (!allDependencies.containsKey(diagramName)) {
      logger.trace("Tracking diagram [" + diagramName + "]");

      for (JustificationElement element : elements.values()) {
        if (equalElements.containsKey(element)) {
          equalElements.get(element).put(diagramName, element.getIdentifier());
        } else {
          Map<String, String> identifier = new HashMap<>();
          identifier.put(diagramName, element.getIdentifier());
          equalElements.put(element, identifier);
        }
      }

    } else {
      throw new RuntimeException("Two diagrams have the same name.");
    }
  }



  public String getIdentifier(JustificationElement element, String diagramName) {
    return equalElements.get(element).get(diagramName);
  }

  public Set<JustificationElement> getUniqueElements() {
    return equalElements.keySet();
  }
}
