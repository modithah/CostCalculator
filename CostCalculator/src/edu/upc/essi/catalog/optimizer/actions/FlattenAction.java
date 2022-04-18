package edu.upc.essi.catalog.optimizer.actions;

import aima.core.agent.impl.DynamicAction;
import com.google.common.base.Objects;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import org.hypergraphdb.HyperGraph;

public class FlattenAction extends DynamicAction {

    private HyperGraph G;
    private Hyperedge A;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlattenAction that = (FlattenAction) o;
        return Objects.equal(G, that.G) &&
                Objects.equal(A, that.A);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), G, A);
    }

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
