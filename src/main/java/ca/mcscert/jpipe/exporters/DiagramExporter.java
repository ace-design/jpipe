package ca.mcscert.jpipe.exporters;

import ca.mcscert.jpipe.model.ConcreteJustification;
import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.visitors.ToGraph;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class DiagramExporter implements Exportation<JustificationDiagram> {

    private static final Logger logger = LogManager.getLogger(DiagramExporter.class);

    public void export(JustificationDiagram j, String outputFile) {
        logger.trace("Exporting justification ["+j.name()+"]");
        ToGraph visitor = new ToGraph();
        j.accept(visitor);
        MutableGraph graph = visitor.getResult();
        try {
            Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File(outputFile));
        } catch (IOException ioe) {
            throw new ExportationError(ioe.getMessage());
        }
        logger.trace("End of exportation ["+j.name()+"]");
    }

}
