package edu.upc.essi.catalog.optimizer;

import aima.core.agent.Action;
import aima.core.search.framework.problem.ResultFunction;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;
import edu.upc.essi.catalog.optimizer.actions.ActionsCatalog;
import edu.upc.essi.catalog.optimizer.actions.UnionAction;
import org.hypergraphdb.HyperGraph;

import java.util.UUID;

public class DocDesignResultsFunction implements ResultFunction {
    @Override
    public Object result(Object o, Action a) {
        if (a instanceof UnionAction) {
            HyperGraph G = (HyperGraph)o;
            HyperGraph newG = Graphoperations.makeGraphCopy(UUID.randomUUID().toString());
            UnionAction uA = (UnionAction)a;

            Transformations.union(newG,uA.getA(),uA.getB());

            return newG;
        }
        return null;
    }
}
