package ca.mcscert.jpipe.model;

public interface JustificationDiagram extends Visitable {

    /**
     * Get the name of the Justification Diagram
     * @return the unique name, as a string
     */
    String name();

    /**
     * Give access to the entry point (the conclusion) of the diagram
     * @return the diagram's conclusion
     */
    Conclusion conclusion();

}
