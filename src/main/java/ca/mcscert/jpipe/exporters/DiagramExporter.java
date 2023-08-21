package ca.mcscert.jpipe.exporters;

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

    @Override
    public void export(JustificationDiagram j, String outputFile) {
        throw new UnsupportedOperationException("Use the export method with the format parameter.");
    }

    public void export(JustificationDiagram j, String outputFile, String format) {
        Format fileFormat = getFormatFromString(format);

        logger.trace("Exporting justification [" + j.name() + "]");

        ToGraph visitor = new ToGraph();
        j.accept(visitor);

        MutableGraph graph = visitor.getResult();

        try {
            Graphviz.fromGraph(graph).render(fileFormat).toFile(new File(outputFile));
        } catch (IOException ioe) {
            throw new ExportationError(ioe.getMessage());
        }

        logger.trace("End of exportation [" + j.name() + "]");
    }

    private Format getFormatFromString(String format) {
        return switch (format.toLowerCase()) {
            case "png" -> Format.PNG;
            case "svg" -> Format.SVG;
            default -> throw new ExportationError("Unsupported file format: " + format);
        };
    }
}


