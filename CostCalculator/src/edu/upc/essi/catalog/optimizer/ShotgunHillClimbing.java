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

import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class ShotgunHillClimbing {

	public ShotgunHillClimbing() {
		// TODO Auto-generated constructor stub
	}

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

	public static void main(String[] args) throws IOException {
		//Shotgun hill-climbing
		Pair<String, Double> solution = null;
		Pair<String, Double> bestSoFar = null;
		int timesConverged = 0;
		int iterations = 0;
		FileWriter csvWriter = null;

//		csvWriter = new FileWriter("data/schemas/rubis/difsizes/15.csv");
//
//		csvWriter.append("iter");
//		csvWriter.append(",");
//		csvWriter.append("val");
//		csvWriter.append("\n");
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

			if (bestSoFar == null) {bestSoFar = solution;
				}
			else {
				if (solution.getSecond() < bestSoFar.getSecond()) {
					timesConverged = 0;
//					logger.info(bestSoFar.getFirst());
					bestSoFar = solution;
					
				} else {
					++timesConverged;
				}
			}
//			csvWriter.append(String.valueOf(iterations));
//			csvWriter.append(",");
//			csvWriter.append(String.valueOf(bestSoFar.getSecond()));
//			csvWriter.append("\n");
//			logger.info(solution.getSecond()+","+ bestSoFar.getSecond());
//			logger.info(solution.getFirst());
//			logger.info("bestSoFar cost = "+bestSoFar.getSecond());
//			logger.info(bestSoFar.getFirst());
			logger.info("!!!!!!!!  "+timesConverged);
			SingletonMultiObjectiveDesignGoal.INSTANCE.destroy();
		} while (timesConverged < 30); //timesConverged
//		csvWriter.flush();
//		csvWriter.close();
		System.out.println( "You are done, the solution has a cost "+bestSoFar.getSecond()+" with design \n"+bestSoFar.getFirst());

	}

}
