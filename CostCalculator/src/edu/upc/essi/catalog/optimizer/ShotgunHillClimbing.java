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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class ShotgunHillClimbing {

	public ShotgunHillClimbing() {
		// TODO Auto-generated constructor stub
	}

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

	public static void main(String[] args) {
		//Shotgun hill-climbing
		Pair<String, Double> solution = null;
		Pair<String, Double> bestSoFar = null;
		int timesConverged = 0;
		int iterations = 0;

		do {
//			logger.info("ITERATIONS "+iterations);
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
//					logger.info(bestSoFar.getFirst());
					bestSoFar = solution;
					
				} else {
					++timesConverged;
				}
			}
//			logger.info(solution.getSecond()+","+ bestSoFar.getSecond());
//			logger.info("bestSoFar cost = "+bestSoFar.getSecond());
			logger.info("!!!!!!!!  "+timesConverged);
			SingletonMultiObjectiveDesignGoal.INSTANCE.destroy();
		} while (timesConverged < 20); //timesConverged

		logger.info("You are done, the solution has a cost "+bestSoFar.getSecond()+" with design \n"+bestSoFar.getFirst());

	}

}
