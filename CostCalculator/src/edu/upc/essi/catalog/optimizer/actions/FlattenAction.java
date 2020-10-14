package edu.upc.essi.catalog.optimizer.actions;

import aima.core.agent.impl.DynamicAction;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import org.hypergraphdb.HyperGraph;

public class UnionAction extends DynamicAction {

    private HyperGraph G;
    private Hyperedge A;
    private Hyperedge B;

    public UnionAction(HyperGraph G, Hyperedge A, Hyperedge B) {
        super(ActionsCatalog.UNION.name());
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

    public Hyperedge getB() {
        return B;
    }

    public void setB(Hyperedge b) {
        B = b;
    }
}
