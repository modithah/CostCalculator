package edu.upc.essi.catalog.optimizer;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.cost.CostResult;
import edu.upc.essi.catalog.generators.GenerateRandomDesign;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;
import org.json.JSONException;

public class ShotgunHillClimbing {

	public ShotgunHillClimbing() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		//Shotgun hill-climbing
		Pair<HyperGraph, Double> solution = null;
		Pair<HyperGraph, Double> bestSoFar = null;
		int timesConverged = 0;
		int iterations = 0;
		do {
			System.out.println("ITERATIONS "+iterations);
			++iterations;

			WorkflowExecutions worker = new WorkflowExecutions();
			HyperGraph G = worker.run();
			/**try {
				CostResult result = CostCalculator.calculateCost(G);
			} catch (JSONException e) {
				e.printStackTrace();
			}**/

			if (bestSoFar == null) bestSoFar = solution;
			else {
				if (bestSoFar.getSecond() < solution.getSecond()) {
					timesConverged = 0;
					bestSoFar = solution;
				} else {
					++timesConverged;
				}
			}
		} while (/*timesConverged*/iterations < 2);

	}

}
