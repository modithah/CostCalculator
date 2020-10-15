package edu.upc.essi.catalog.optimizer.costfunctions;

import edu.upc.essi.catalog.cost.DepthandHeterogeniety;
import org.hypergraphdb.HyperGraph;

public class Heterogenity_CF extends CostFunction {

    public double evaluate(HyperGraph G) {
        return DepthandHeterogeniety.CalculateHeterogeniety(G);
    }
}
