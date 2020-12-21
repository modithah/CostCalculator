package edu.upc.essi.catalog.optimizer.costfunctions;

import edu.upc.essi.catalog.optimizer.CostCalculator;
import org.hypergraphdb.HyperGraph;
import org.json.JSONException;

public class StorageSpace_CF extends CostFunction {

    @Override
    public double evaluate(HyperGraph G) {
        try {
            return CostCalculator.calculateCost(G).getStorageSize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Only for errors
        return Double.POSITIVE_INFINITY;
    }

}
