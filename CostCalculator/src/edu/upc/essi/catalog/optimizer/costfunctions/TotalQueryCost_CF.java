package edu.upc.essi.catalog.optimizer.costfunctions;

import edu.upc.essi.catalog.optimizer.CostCalculator;
import org.hypergraphdb.HyperGraph;
import org.json.JSONException;

public class TotalQueryCost_CF extends CostFunction {

    public double evaluate(HyperGraph G) {
        try {
            return CostCalculator.calculateCost(G).getQueryCosts().stream().reduce(Double::sum).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Double.POSITIVE_INFINITY;
    }
}
