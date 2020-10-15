package edu.upc.essi.catalog.optimizer.actions;

import aima.core.agent.impl.DynamicAction;
import edu.upc.essi.catalog.core.constructs.opsparams.EmbedParams;
import edu.upc.essi.catalog.core.constructs.opsparams.GroupParams;
import org.hypergraphdb.HyperGraph;

public class EmbedAction extends DynamicAction {

    private HyperGraph G;
    private EmbedParams params;

    public EmbedAction(HyperGraph G, EmbedParams params) {
        super(ActionsCatalog.EMBED.name());
        this.G = G;
        this.params = params;
    }

    public HyperGraph getG() {
        return G;
    }

    public void setG(HyperGraph g) {
        G = g;
    }

    public EmbedParams getParams() {
        return params;
    }

    public void setParams(EmbedParams params) {
        this.params = params;
    }
}
