package edu.upc.essi.catalog.optimizer;

import aima.core.search.framework.evalfunc.HeuristicFunction;
import edu.upc.essi.catalog.optimizer.costfunctions.SingletonMultiObjectiveDesignGoal;
import org.hypergraphdb.HyperGraph;

public class DocDesignHeuristic implements HeuristicFunction {
    @Override
    public double h(Object o) {
        HyperGraph G = (HyperGraph)o;
        double h = SingletonMultiObjectiveDesignGoal.INSTANCE.getDG().evaluate(G);
        return h;
//        return 0;
    }
}
