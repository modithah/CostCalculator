package edu.upc.essi.catalog.optimizer;

import aima.core.search.framework.evalfunc.HeuristicFunction;
import edu.upc.essi.catalog.optimizer.costfunctions.SingletonMultiObjectiveDesignGoal;
import org.hypergraphdb.HyperGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class DocDesignHeuristic implements HeuristicFunction {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    @Override
    public double h(Object o) {
        HyperGraph G = (HyperGraph)o;
        logger.info("evaluating "+ ((HyperGraph) o).getLocation());
        double h = SingletonMultiObjectiveDesignGoal.INSTANCE.getDG().evaluate(G);
        return h;
//        return 0;
    }
}
