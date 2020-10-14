package edu.upc.essi.catalog.optimizer;

import aima.core.agent.Action;
import aima.core.search.framework.problem.ActionsFunction;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;
import edu.upc.essi.catalog.optimizer.actions.ActionsCatalog;
import edu.upc.essi.catalog.optimizer.actions.FlattenAction;
import edu.upc.essi.catalog.optimizer.actions.UnionAction;
import org.assertj.core.util.Sets;
import org.hypergraphdb.HyperGraph;

import java.util.Set;

public class DocDesignActionsFunction implements ActionsFunction {

    @Override
    public Set<Action> actions(Object o) {
        HyperGraph G = (HyperGraph) o;
        Set<Action> actions = Sets.newHashSet();

        // Union
        Transformations.getUnionCandidates(G).forEach(uCandidate -> {
            //Create a copy
            HyperGraph newG = Graphoperations.makeGraphCopy(G);
            actions.add(new UnionAction(newG,uCandidate.getFirst(), uCandidate.getSecond()));
        });

        //Flatten
        Transformations.getFlattenCandidates(G).forEach(fCandidate -> {
            //Create a copy
            HyperGraph newG = Graphoperations.makeGraphCopy(G);
            actions.add(new FlattenAction(newG,fCandidate));
        });


        return actions;
    }

}
