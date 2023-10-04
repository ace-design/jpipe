package ca.mcscert.jpipe.exporters;

import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.visitors.ToGraph;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Exporter to transform a JustificationDiagram instance into an image.
 */
public class DiagramExporter implements Exportation<JustificationDiagram> {

    // Logger to trace info while exporting.
    private static final Logger logger = LogManager.getLogger(DiagramExporter.class);

    /**
     * run the exportation to image.
     *
     * @param j the diagram to export.
     * @param outputFile the outputfile to use.
     */
    public void export(JustificationDiagram j, String outputFile) {
        logger.trace("Exporting justification [" + j.name() + "]");
        ToGraph visitor = new ToGraph();
        j.accept(visitor);
        MutableGraph graph = visitor.getResult();
        try {
            Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File(outputFile));
        } catch (IOException ioe) {
            throw new ExportationError(ioe.getMessage());
        }
        logger.trace("End of exportation [" + j.name() + "]");
    }

}
