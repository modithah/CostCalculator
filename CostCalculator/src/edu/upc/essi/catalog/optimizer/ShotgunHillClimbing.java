package edu.upc.essi.catalog.optimizer;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.cost.CostResult;
import edu.upc.essi.catalog.generators.GenerateRandomDesign;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.optimizer.costfunctions.NormalizedWeightedSum_DG;
import edu.upc.essi.catalog.optimizer.costfunctions.SingletonMultiObjectiveDesignGoal;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;
import org.json.JSONException;

public class ShotgunHillClimbing {

	public ShotgunHillClimbing() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		//Shotgun hill-climbing
		Pair<String, Double> solution = null;
		Pair<String, Double> bestSoFar = null;
		int timesConverged = 0;
		int iterations = 0;

		do {
			System.out.println("ITERATIONS "+iterations);
			++iterations;

			WorkflowExecutions worker = new WorkflowExecutions();
			solution = worker.run();

			/**try {
				CostResult result = CostCalculator.calculateCost(G);
			} catch (JSONException e) {
				e.printStackTrace();
			}**/

			if (bestSoFar == null) bestSoFar = solution;
			else {
				if (solution.getSecond() < bestSoFar.getSecond()) {
					timesConverged = 0;
					bestSoFar = solution;
				} else {
					++timesConverged;
				}
			}
			System.out.println("bestSoFar cost = "+bestSoFar.getSecond());

			SingletonMultiObjectiveDesignGoal.INSTANCE.destroy();
		} while (/*timesConverged*/iterations < 3);

		System.out.println("You are done, the solution has a cost "+bestSoFar.getSecond()+" with design "+bestSoFar.getFirst());

	}

}
