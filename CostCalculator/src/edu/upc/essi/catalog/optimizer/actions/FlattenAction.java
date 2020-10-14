package edu.upc.essi.catalog.optimizer.actions;

import aima.core.agent.impl.DynamicAction;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import org.hypergraphdb.HyperGraph;

public class FlattenAction extends DynamicAction {

    private HyperGraph G;
    private Hyperedge A;

    public FlattenAction(HyperGraph G, Hyperedge A) {
        super(ActionsCatalog.FLATTEN.name());
        this.G = G;
        this.A = A;
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

}
