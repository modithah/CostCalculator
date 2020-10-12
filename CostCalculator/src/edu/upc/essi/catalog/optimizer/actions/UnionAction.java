package edu.upc.essi.catalog.optimizer.actions;

import aima.core.agent.impl.DynamicAction;
import edu.upc.essi.catalog.core.constructs.Hyperedge;

public class UnionAction extends DynamicAction {

    private Hyperedge A;
    private Hyperedge B;

    public UnionAction(Hyperedge A, Hyperedge B) {
        super(ActionsCatalog.UNION.name());
        this.A = A;
        this.B = B;
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
