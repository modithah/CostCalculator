package edu.upc.essi.catalog.optimizer.costfunctions;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;

import java.util.Map;
import java.util.Set;

public class NormalizedWeightedSumDG {

    private Set<Pair<CostFunction, Double>> costFunctions;

    //The utopian points are mapped by class name.
    private Map<String, Pair<HyperGraph,Double>> compromisePoints;

    public NormalizedWeightedSum_DG(double queryCostWeight, double storageSpaceWeight) {
        Set<Pair<CostFunction, Double>> costFunctions = Sets.newHashSet();
        compromisePoints = Maps.newHashMap();

        costFunctions.add(new Pair<>(new TotalQueryCost_CF(), queryCostWeight));
        compromisePoints.put(TotalQueryCost_CF.class.getName(),1);

        costFunctions.add(new Pair<>(new StorageSpace_CF(), storageSpaceWeight));

        this.costFunctions=costFunctions;
    }


}
