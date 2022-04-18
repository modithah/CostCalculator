package edu.upc.essi.catalog.optimizer.actions;

import aima.core.agent.impl.DynamicAction;
import com.google.common.base.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UnionAction that = (UnionAction) o;
        return Objects.equal(G, that.G) &&
                Objects.equal(A, that.A) &&
                Objects.equal(B, that.B);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), G, A, B);
    }
}
