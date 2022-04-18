package edu.upc.essi.catalog.optimizer.costfunctions;

import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.optimizer.CostCalculator;
import org.hypergraphdb.HyperGraph;
import org.json.JSONException;

public class TotalQueryCost_CF extends CostFunction {

    public double evaluate(HyperGraph G) {
        try {
            final Double cost = CostCalculator.calculateCost(G).getQueryCosts().stream().reduce(Double::sum).get();
            System.out.println("Cost is" + cost);
            Graphoperations.printDesign(G);
            return cost;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Double.POSITIVE_INFINITY;
    }
}
