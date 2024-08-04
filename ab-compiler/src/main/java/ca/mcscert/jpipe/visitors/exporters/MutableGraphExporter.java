package ca.mcscert.jpipe.visitors.exporters;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;
import static guru.nidi.graphviz.model.Factory.node;

import ca.mcscert.jpipe.model.Justification;
import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.model.elements.Conclusion;
import ca.mcscert.jpipe.model.elements.Evidence;
import ca.mcscert.jpipe.model.elements.JustificationElement;
import ca.mcscert.jpipe.model.elements.Strategy;
import ca.mcscert.jpipe.model.elements.SubConclusion;
import ca.mcscert.jpipe.model.elements.Support;
import ca.mcscert.jpipe.visitors.ModelVisitor;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;


public class MutableGraphExporter extends ModelVisitor<MutableGraph> {

    public MutableGraphExporter() {
        // Mutable graph (Graphviz data structure) as output
        super(mutGraph()
                .setDirected(true)
                .graphAttrs()
                .add(Rank.dir(Rank.RankDir.BOTTOM_TO_TOP)));
    }

    @Override
    public void visit(Unit u) {
        throw new RuntimeException("Cannot export to graphviz an entire compilation unit!");
    }

    @Override
    public void visit(Justification j) {
        // Setting justification name
        this.result.graphAttrs().add(Label.markdown(j.getName()));
        for (JustificationElement je: j.contents()) {
            je.accept(this);
        }
    }

    @Override
    public void visit(Conclusion c) {
        MutableNode n = mutNode(c.getIdentifier())
                .add(Label.markdown(c.getLabel()))
                .add(Shape.RECT)
                .add(Style.combine(Style.FILLED, Style.ROUNDED))
                .add(Color.LIGHTGREY.fill())
                .add(Style.lineWidth(1.01))
                ;
        n.addTo(this.result);
        if (c.getStrategy() != null) {
            this.result.add(node(c.getStrategy().getIdentifier()).link(n));
        }
    }

    @Override
    public void visit(Evidence e) {
        MutableNode n = mutNode(e.getIdentifier())
                .add(Label.markdown(e.getLabel()))
                .add(Shape.NOTE)
                .add(Color.LIGHTSKYBLUE2.fill())
                .add(Style.FILLED)
                .add(Style.lineWidth(1.01));
        n.addTo(this.result);
    }

    @Override
    public void visit(Strategy s) {
        MutableNode n = mutNode(s.getIdentifier())
                .add(Label.markdown(s.getLabel()))
                .add(Shape.PARALLELOGRAM)
                .add(Style.FILLED)
                .add(Color.PALEGREEN.fill())
                .add(Style.lineWidth(1.01));
        n.addTo(this.result);
        for(Support su: s.getSupports()) {
            this.result.add(node(su.getIdentifier()).link(n));
        }
    }

    @Override
    public void visit(SubConclusion sc) {
        MutableNode n = mutNode(sc.getIdentifier())
                .add(Label.markdown(sc.getLabel()))
                .add(Shape.RECT)
                .add(Color.DODGERBLUE)
                .add(Style.lineWidth(1.01));
        n.addTo(this.result);
        if (sc.getStrategy() != null) {
            this.result.add(node(sc.getStrategy().getIdentifier()).link(n));
        }
    }

}
