package edu.upc.essi.catalog.optimizer.actions;

import aima.core.agent.impl.DynamicAction;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
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
}
