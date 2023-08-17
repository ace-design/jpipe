package ca.mcscert.jpipe.exporters;

import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.visitors.ToGraph;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.rough.Roughifyer;
import guru.nidi.graphviz.rough.FillStyle;
import guru.nidi.graphviz.model.MutableGraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class DiagramExporter implements Exportation<JustificationDiagram> {

    private static final Logger logger = LogManager.getLogger(DiagramExporter.class);
    private boolean roughStyle = false;

    private double bowing = 2;
    private int curveStepCount = 6;
    private double roughness = 1;
    private FillStyle fillStyle = FillStyle.hachure().width(2).gap(5).angle(0);
    private String[] fonts = {"*serif", "Comic Sans MS"};


    public void setRoughStyle(boolean roughStyle) {
        this.roughStyle = roughStyle;
    }

    public void export(JustificationDiagram j, String outputFile) {
        logger.trace("Exporting justification ["+j.name()+"]");
        ToGraph visitor = new ToGraph();
        j.accept(visitor);
        MutableGraph graph = visitor.getResult();

        if (roughStyle) {
            try {
                Graphviz.fromGraph(graph)
                        .processor(new Roughifyer()
                                .bowing(bowing)
                                .curveStepCount(curveStepCount)
                                .roughness(roughness)
                                .fillStyle(fillStyle)
                                .font(fonts[0], fonts[1]))
                        .render(Format.PNG)
                        .toFile(new File(outputFile));
            } catch (IOException ioe) {
                throw new ExportationError(ioe.getMessage());
            }
        } else {
            try {
                Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File(outputFile));
            } catch (IOException ioe) {
                throw new ExportationError(ioe.getMessage());
            }
        }

        logger.trace("End of exportation ["+j.name()+"]");
    }
}

