package ca.mcscert.jpipe.compiler.steps.io;

import ca.mcscert.jpipe.cli.Configuration;
import ca.mcscert.jpipe.compiler.model.Sink;
import ca.mcscert.jpipe.error.ErrorManager;
import ca.mcscert.jpipe.cli.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GraphVizRenderer implements Sink<MutableGraph> {

    private final Format format;
    private final String path;
    private static final Logger logger = LogManager.getLogger();

    public GraphVizRenderer(Configuration config) {
        this.format = config.getFormat();
        this.path = config.getOutputFilePath();
    }

    @Override
    public void pourInto(MutableGraph input, String source) throws IOException {

        guru.nidi.graphviz.engine.Format out = switch (this.format) {
            case PNG -> guru.nidi.graphviz.engine.Format.PNG;
            case SVG -> guru.nidi.graphviz.engine.Format.SVG;
            default -> throw new IllegalArgumentException("Cannot process format [" +
                                    format + "] with a Graphviz renderer");
        };

        if (this.path.equals(Configuration.STDOUT_PATH)) {
            runGraphviz(System.out, out, input);
        } else {
            try (FileOutputStream fos = new FileOutputStream(this.path)) {
                runGraphviz(fos, out, input);
            } catch (IOException ioe) {
                ErrorManager.getInstance().fatal(ioe);
            }
        }
    }


    private void runGraphviz(OutputStream outStream, guru.nidi.graphviz.engine.Format outFormat,
                             MutableGraph graph) throws IOException {
        Graphviz.fromGraph(graph).render(outFormat).toOutputStream(outStream);
    }

}
