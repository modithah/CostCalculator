package edu.upc.essi.catalog.optimizer;

import aima.core.agent.Action;
import aima.core.search.framework.problem.ResultFunction;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;
import edu.upc.essi.catalog.optimizer.actions.ActionsCatalog;
import edu.upc.essi.catalog.optimizer.actions.UnionAction;
import org.hypergraphdb.HyperGraph;

public class DocDesignResultsFunction implements ResultFunction {
    @Override
    public Object result(Object o, Action a) {
        if (a instanceof UnionAction) {
            HyperGraph G = (HyperGraph)o;
            Graphoperations.makeGraphCopy();
            UnionAction uA = (UnionAction)a;
            //Is this G global??
            Transformations.union(G,uA.getA(),uA.getB());

            return G;
        }
        return null;
    }
}
