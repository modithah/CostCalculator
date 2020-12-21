package edu.upc.essi.catalog.optimizer;

import aima.core.agent.Action;
import aima.core.search.framework.problem.ResultFunction;
import com.google.common.collect.Sets;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;
import edu.upc.essi.catalog.optimizer.actions.*;
import org.hypergraphdb.HyperGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

public class DocDesignResultsFunction implements ResultFunction {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

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
        else if(a instanceof GroupAction){
            GroupAction gA = (GroupAction)a;
            newG = gA.getG();
            Transformations.group(newG,gA.getParams());
        }
        else if(a instanceof EmbedAction){
            EmbedAction eA = (EmbedAction)a;
            newG = eA.getG();
            Transformations.embed(newG, eA.getParams());
        }
        else if(a instanceof SegregateAction){
            SegregateAction sA = (SegregateAction)a;
            newG = sA.getG();
            Transformations.segregate(newG, sA.getA(), sA.getB());
        }
        else {
            logger.error("Unknown action " +a.getClass());
        }

        DocDesignHeuristic fh = new DocDesignHeuristic();
        double h = fh.h(newG);
        //logger.debug("Old optimal = "+ForgeProcess.optimalh+", current h = "+h);
        if (h < WorkflowExecutions.optimalh) {
            WorkflowExecutions.optimalh = h;
            WorkflowExecutions.optimalConfigurations = Sets.newHashSet();
            WorkflowExecutions.optimalConfigurations.add(Graphoperations.stringDesign(newG));
        }
        else if (h == WorkflowExecutions.optimalh) {
            WorkflowExecutions.optimalConfigurations.add(Graphoperations.stringDesign(newG));
        }

        if (h != Double.POSITIVE_INFINITY && h > WorkflowExecutions.worsth) {
            WorkflowExecutions.worsth = h;
            WorkflowExecutions.worstConfigurations = Sets.newHashSet();
            WorkflowExecutions.worstConfigurations.add(Graphoperations.stringDesign(newG));
        }
        else if (h != Double.POSITIVE_INFINITY && h == WorkflowExecutions.worsth) {
            WorkflowExecutions.worstConfigurations.add(Graphoperations.stringDesign(newG));
        }
        return newG;
    }
}
