package ca.mcscert.jpipe.visitors;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

import ca.mcscert.jpipe.model.justification.AbstractSupport;
import ca.mcscert.jpipe.model.justification.Conclusion;
import ca.mcscert.jpipe.model.justification.ConcreteJustification;
import ca.mcscert.jpipe.model.justification.Evidence;
import ca.mcscert.jpipe.model.justification.JustificationPattern;
import ca.mcscert.jpipe.model.justification.Strategy;
import ca.mcscert.jpipe.model.justification.SubConclusion;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementation of a default visitor to export diagrams to GraphViz nodes (MutableGraph).
 */
public class ToGraph extends DefaultVisitor<MutableGraph> {

    private static final Logger logger = LogManager.getLogger(ToGraph.class);

    private Optional<String> parent;

    /**
     * Constructor to instantiate the graph transformation visitor.
     */
    public ToGraph() {
        this.result = mutGraph().setDirected(true)
                .graphAttrs().add(Rank.dir(Rank.RankDir.BOTTOM_TO_TOP));
        this.parent = Optional.empty();
    }

    @Override
    public MutableGraph getResult() {
        return super.getResult();
    }

    @Override
    public void visit(ConcreteJustification j) {
        logger.trace("  Visiting justification [" + j.name() + "]");
        this.result.graphAttrs().add(Label.markdown(j.name()));
        j.conclusion().accept(this);
    }

    @Override
    public void visit(JustificationPattern p) {
        logger.trace("  Visiting pattern [" + p.name() + "]");
        this.result.graphAttrs()
                .add(Label.markdown("&lt;&lt;pattern&gt;&gt;&nbsp;" + p.name()));
        p.conclusion().accept(this);
    }

    @Override
    public void visit(Conclusion c) {
        logger.trace("  Visiting conclusion [" + c.getIdentifier() + "]");
        mutNode(c.getIdentifier())
                .add(Label.markdown(c.getLabel()))
                .add(Shape.RECT)
                .add(Style.FILLED)
                .add(Color.LIGHTGREY.fill())
                .add(Style.lineWidth(1.01))
                .addTo(this.result);
        this.parent = Optional.of(c.getIdentifier());
        super.visit(c);
    }

    @Override
    public void visit(Strategy s) {
        logger.trace("  Visiting strategy [" + s.getIdentifier() + "]");
        MutableNode n = mutNode(s.getIdentifier())
                .add(Label.markdown(s.getLabel()))
                .add(Shape.PARALLELOGRAM)
                .add(Style.FILLED)
                .add(Color.PALEGREEN.fill())
                .add(Style.lineWidth(1.01));
        n.addTo(this.result);
        this.parent.ifPresent(n::addLink);
        Optional<String> old = this.parent;
        this.parent = Optional.of(s.getIdentifier());
        super.visit(s);
        this.parent = old;
    }

    @Override
    public void visit(SubConclusion sc) {
        logger.trace("  Visiting sub-conclusion [" + sc.getIdentifier() + "]");
        MutableNode n = mutNode(sc.getIdentifier())
                .add(Label.markdown(sc.getLabel()))
                .add(Shape.RECT)
                .add(Color.DODGERBLUE)
                .add(Style.lineWidth(1.01));
        n.addTo(this.result);
        this.parent.ifPresent(n::addLink);
        Optional<String> old = this.parent;
        this.parent = Optional.of(sc.getIdentifier());
        super.visit(sc);
        this.parent = old;
    }

    @Override
    public void visit(Evidence e) {
        logger.trace("  Visiting evidence [" + e.getIdentifier() + "]");
        MutableNode n = mutNode(e.getIdentifier())
                .add(Label.markdown(e.getLabel()))
                .add(Shape.RECT)
                .add(Color.LIGHTSKYBLUE2.fill())
                .add(Style.FILLED)
                .add(Style.lineWidth(1.01));
        n.addTo(this.result);
        this.parent.ifPresent(n::addLink);
    }

    @Override
    public void visit(AbstractSupport a) {
        logger.trace("  Visiting abstract support [" + a.getIdentifier() + "]");
        MutableNode n = mutNode(a.getIdentifier())
                .add(Label.markdown(a.getLabel()))
                .add(Shape.RECT)
                .add(Color.LIGHTCORAL.fill())
                .add(Style.FILLED)
                .add(Style.lineWidth(1.01));
        n.addTo(this.result);
        this.parent.ifPresent(n::addLink);
    }

}
