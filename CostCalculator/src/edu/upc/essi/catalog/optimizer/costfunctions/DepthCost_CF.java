package edu.upc.essi.catalog.optimizer.costfunctions;

import edu.upc.essi.catalog.cost.DepthandHeterogeniety;
import edu.upc.essi.catalog.optimizer.CostCalculator;
import org.hypergraphdb.HyperGraph;
import org.json.JSONException;

public class DepthCost_CF extends CostFunction {

    public double evaluate(HyperGraph G) {
        return DepthandHeterogeniety.CalculateDepth(G);
    }
}
