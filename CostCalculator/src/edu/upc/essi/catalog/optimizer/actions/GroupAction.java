package edu.upc.essi.catalog.optimizer.actions;

import aima.core.agent.impl.DynamicAction;
import com.google.common.base.Objects;
import edu.upc.essi.catalog.core.constructs.opsparams.GroupParams;
import org.hypergraphdb.HyperGraph;

public class GroupAction extends DynamicAction {

    private HyperGraph G;
    private GroupParams params;

    public GroupAction(HyperGraph G, GroupParams params) {
        super(ActionsCatalog.GROUP.name());
        this.G = G;
        this.params = params;
    }

    public HyperGraph getG() {
        return G;
    }

    public void setG(HyperGraph g) {
        G = g;
    }

    public GroupParams getParams() {
        return params;
    }

    public void setParams(GroupParams params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GroupAction that = (GroupAction) o;
        return Objects.equal(G, that.G) &&
                Objects.equal(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), G, params);
    }
}
