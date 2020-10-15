package edu.upc.essi.catalog.optimizer.actions;

import aima.core.agent.impl.DynamicAction;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import org.hypergraphdb.HyperGraph;

public class SegregateAction extends DynamicAction {

    private HyperGraph G;
    private Hyperedge A;
    private Element B;

    public SegregateAction(HyperGraph G, Hyperedge A, Element B) {
        super(ActionsCatalog.SEGREGATE.name());
        this.G = G;
        this.A = A;
        this.B = B;
    }

    public HyperGraph getG() {
        return G;
    }

    public void setG(HyperGraph g) {
        G = g;
    }

    public Hyperedge getA() {
        return A;
    }

    public void setA(Hyperedge a) {
        A = a;
    }

    public Element getB() {
        return B;
    }

    public void setB(Element b) {
        B = b;
    }
}
