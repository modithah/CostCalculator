package edu.upc.essi.catalog.optimizer;

import aima.core.agent.Action;
import aima.core.search.framework.problem.ResultFunction;
import com.google.common.collect.Sets;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;
import edu.upc.essi.catalog.optimizer.actions.ActionsCatalog;
import edu.upc.essi.catalog.optimizer.actions.FlattenAction;
import edu.upc.essi.catalog.optimizer.actions.UnionAction;
import org.hypergraphdb.HyperGraph;

import java.util.UUID;

public class DocDesignResultsFunction implements ResultFunction {
    @Override
    public Object result(Object o, Action a) {
        HyperGraph newG = null;
        if (a instanceof UnionAction) {
            UnionAction uA = (UnionAction)a;
            newG = uA.getG();
            Transformations.union(newG,uA.getA(),uA.getB());
        }
        else if (a instanceof FlattenAction) {
            FlattenAction fA = (FlattenAction)a;
            newG = fA.getG();
            Transformations.flatten(newG,fA.getA());
        }

        DocDesignHeuristic fh = new DocDesignHeuristic();
        double h = fh.h(newG);
        //logger.debug("Old optimal = "+ForgeProcess.optimalh+", current h = "+h);
        if (h < WorkflowExecutions.optimalh) {
            WorkflowExecutions.optimalh = h;
            WorkflowExecutions.optimalConfigurations = Sets.newHashSet();
            WorkflowExecutions.optimalConfigurations.add(newG);
        }
        else if (h == WorkflowExecutions.optimalh) {
            WorkflowExecutions.optimalConfigurations.add(newG);
        }

        if (h != Double.POSITIVE_INFINITY && h > WorkflowExecutions.worsth) {
            WorkflowExecutions.worsth = h;
            WorkflowExecutions.worstConfigurations = Sets.newHashSet();
            WorkflowExecutions.worstConfigurations.add(newG);
        }
        else if (h != Double.POSITIVE_INFINITY && h == WorkflowExecutions.worsth) {
            WorkflowExecutions.worstConfigurations.add(newG);
        }
        return newG;
    }
}
