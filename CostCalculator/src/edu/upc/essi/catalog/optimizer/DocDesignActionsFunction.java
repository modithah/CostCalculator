package edu.upc.essi.catalog.optimizer;

import aima.core.agent.Action;
import aima.core.search.framework.problem.ActionsFunction;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;
import edu.upc.essi.catalog.optimizer.actions.*;
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

            //Apply the transformation to check if its already tested
            HyperGraph dummy = Graphoperations.makeGraphCopy(G);
            Transformations.union(dummy,uCandidate.getFirst(), uCandidate.getSecond());
            if (!WorkflowExecutions.usedConfigurations.contains(Graphoperations.stringDesign(dummy))) {
                WorkflowExecutions.usedConfigurations.add(Graphoperations.stringDesign(dummy));
                actions.add(new UnionAction(newG, uCandidate.getFirst(), uCandidate.getSecond()));
            }
        });

        //Flatten
        Transformations.getFlattenCandidates(G).forEach(fCandidate -> {
            //Create a copy
            HyperGraph newG = Graphoperations.makeGraphCopy(G);
            //Apply the transformation to check if its already tested
            HyperGraph dummy = Graphoperations.makeGraphCopy(G);
            Transformations.flatten(dummy,fCandidate);
            if (!WorkflowExecutions.usedConfigurations.contains(Graphoperations.stringDesign(dummy))) {
                WorkflowExecutions.usedConfigurations.add(Graphoperations.stringDesign(dummy));
                actions.add(new FlattenAction(newG,fCandidate));
            }
        });

        //Group
        Transformations.getGroupCandidates(G).forEach(gCandidate -> {
            //Create a copy
            HyperGraph newG = Graphoperations.makeGraphCopy(G);
            //Apply the transformation to check if its already tested
            HyperGraph dummy = Graphoperations.makeGraphCopy(G);
            Transformations.group(dummy,gCandidate);
            if (!WorkflowExecutions.usedConfigurations.contains(Graphoperations.stringDesign(dummy))) {
                WorkflowExecutions.usedConfigurations.add(Graphoperations.stringDesign(dummy));
                actions.add(new GroupAction(newG,gCandidate));
            }
        });

        //Embed
        Transformations.getEmbedCandidates(G).forEach(eCandidate -> {
            //Create a copy
            HyperGraph newG = Graphoperations.makeGraphCopy(G);
            //Apply the transformation to check if its already tested
            HyperGraph dummy = Graphoperations.makeGraphCopy(G);
            Transformations.embed(dummy,eCandidate);
            if (!WorkflowExecutions.usedConfigurations.contains(Graphoperations.stringDesign(dummy))) {
                WorkflowExecutions.usedConfigurations.add(Graphoperations.stringDesign(dummy));
                actions.add(new EmbedAction(newG,eCandidate));
            }
        });

        //Segregate
        Transformations.getSegregateCandidates(G).forEach(sCandidate -> {
            //Create a copy
            HyperGraph newG = Graphoperations.makeGraphCopy(G);
            //Apply the transformation to check if its already tested
            HyperGraph dummy = Graphoperations.makeGraphCopy(G);
            Transformations.segregate(dummy,sCandidate.getFirst(),sCandidate.getSecond());
            if (!WorkflowExecutions.usedConfigurations.contains(Graphoperations.stringDesign(dummy))) {
                WorkflowExecutions.usedConfigurations.add(Graphoperations.stringDesign(dummy));
                actions.add(new SegregateAction(newG,sCandidate.getFirst(),sCandidate.getSecond()));
            }
        });

        return actions;
    }

}
