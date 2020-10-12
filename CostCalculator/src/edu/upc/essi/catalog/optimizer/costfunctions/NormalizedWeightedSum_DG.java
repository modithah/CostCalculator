package edu.upc.essi.catalog.optimizer.costfunctions;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import edu.upc.essi.catalog.core.constructs.Workload;
import edu.upc.essi.catalog.ops.Transformations;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;

import java.util.Map;
import java.util.Set;

public class NormalizedWeightedSum_DG {

    private Set<Pair<CostFunction, Double>> costFunctions;

    //The utopian points are mapped by class name.
    private Map<String, Double> minimumPoints;
    private Map<String,  Double> maximumPoints;

    public NormalizedWeightedSum_DG(HyperGraph G, double queryCostWeight, double storageSpaceWeight) {
        Set<Pair<CostFunction, Double>> costFunctions = Sets.newHashSet();
        minimumPoints = Maps.newHashMap();

        //Call first storage cost, as there is some update going on to G
        costFunctions.add(new Pair<>(new StorageSpace_CF(), storageSpaceWeight));
        Pair<Double,Double> storageCostMinMax = Transformations.getSizeMinMax(G);
        minimumPoints.put(StorageSpace_CF.class.getName(),storageCostMinMax.getFirst());
        maximumPoints.put(StorageSpace_CF.class.getName(),storageCostMinMax.getSecond());

        costFunctions.add(new Pair<>(new TotalQueryCost_CF(), queryCostWeight));
        Pair<Double,Double> queryCostMinMax = Transformations.getCostMinMax(G,Workload.getWorkload(G));
        minimumPoints.put(TotalQueryCost_CF.class.getName(),queryCostMinMax.getFirst());
        maximumPoints.put(TotalQueryCost_CF.class.getName(),queryCostMinMax.getSecond());

        this.costFunctions=costFunctions;
    }

    public NormalizedWeightedSum_DG(Set<Pair<CostFunction, Double>> costFunctions) {
        this.costFunctions = costFunctions;
        minimumPoints = Maps.newHashMap();
        
        maximumPoints = Maps.newHashMap();
    }

    public double evaluate(HyperGraph G) {
        double V = 0;
        for (Pair<CostFunction, Double> CF : costFunctions) {
            double Fi = CF.getFirst().evaluate(G); //Current value
            double Fo = minimumPoints.get(CF.getFirst().getClass().getName()); //Utopian point
            double Fmax = maximumPoints.get(CF.getFirst().getClass().getName()); //Maximum point

            double eval = ( (Fi - Fo) / (Fmax - Fo) );
            V += (CF.getSecond() * eval);
        }
        return V;
    }

    public Map<String, Double> getMinimumPoints() {
        return minimumPoints;
    }

    public void setMinimumPoints(Map<String, Double> minimumPoints) {
        this.minimumPoints = minimumPoints;
    }

    public Map<String, Double> getMaximumPoints() {
        return maximumPoints;
    }

    public void setMaximumPoints(Map<String, Double> maximumPoints) {
        this.maximumPoints = maximumPoints;
    }

    @Override
    public String toString() {
        return "WeightedSum_DG [costFunctions=" + costFunctions + "]";
    };

}
