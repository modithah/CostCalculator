package edu.upc.essi.catalog.optimizer;

import edu.upc.essi.catalog.optimizer.costfunctions.SingletonMultiObjectiveDesignGoal;
import org.hypergraphdb.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class ShotgunHillClimbingLoop {

	public ShotgunHillClimbingLoop() {
		// TODO Auto-generated constructor stub
	}

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

	public static void main(String[] args) throws IOException {

		int total=0;
		FileWriter csvWriter = new FileWriter("data/schemas/rubis/difsizes/final.csv");

		csvWriter.append("loop");
		csvWriter.append(",");
		csvWriter.append("iter");
		csvWriter.append(",");
		csvWriter.append("val");
		csvWriter.append("\n");
		for (int i = 0; i < 5; i++) {

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
			csvWriter.append(String.valueOf(i));
			csvWriter.append(",");
			csvWriter.append(String.valueOf(iterations));
			csvWriter.append(",");
			csvWriter.append(String.valueOf(bestSoFar.getSecond()));
			csvWriter.append("\n");
//			logger.info(solution.getSecond()+","+ bestSoFar.getSecond());
//			logger.info("bestSoFar cost = "+bestSoFar.getSecond());
			logger.info("!!!!!!!!  "+timesConverged);
			SingletonMultiObjectiveDesignGoal.INSTANCE.destroy();
		} while (timesConverged < 100); //timesConverged

			total = total+iterations-100;
//		System.out.println( "You are done, the solution has a cost "+bestSoFar.getSecond()+" with design \n"+bestSoFar.getFirst());

		}
		csvWriter.flush();
		csvWriter.close();
		System.out.println("Average Iterations " + total/5);
	}

}
