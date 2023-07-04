package ca.mcscert.jpipe.exporters;

import ca.mcscert.jpipe.model.Justification;
import ca.mcscert.jpipe.visitors.ToGraph;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class DiagramExporter implements Exportation<Justification> {

    private static Logger logger = LogManager.getLogger(DiagramExporter.class);

    public void export(Justification j, String outputFile) {
        logger.trace("Exporting justification ["+j.getName()+"]");
        ToGraph visitor = new ToGraph();
        j.accept(visitor);
        MutableGraph graph = visitor.getResult();
        try {
            Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File(outputFile));
        } catch (IOException ioe) {
            throw new ExportationError(ioe.getMessage());
        }
        logger.trace("End of exportation ["+j.getName()+"]");
    }

}
