package edu.upc.essi.server;

import edu.upc.essi.catalog.optimizer.costfunctions.SingletonMultiObjectiveDesignGoal;
import org.hypergraphdb.util.Pair;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;

public class ShotgunHillClimbing {

	public ShotgunHillClimbing() {
		// TODO Auto-generated constructor stub
	}

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

	public static JSONObject run(double query, double size, double depth, double hetro, String jsonSchema, int iters) throws IOException {
		//Shotgun hill-climbing
		Instant start = Instant.now();
		Pair<String, Double> solution = null;
		Pair<String, Double> bestSoFar = null;
		int timesConverged = 0;
		int iterations = 0;






		do {
//			logger.info("ITERATIONS "+iterations);
			++iterations;

			WorkflowExecutions worker = new WorkflowExecutions();
			solution = worker.run(query,size,depth,hetro,jsonSchema);

			/**try {
				CostResult result = CostCalculator.calculateCost(G);
			} catch (JSONException e) {
				e.printStackTrace();
			}**/
//			System.out.println(solution.getSecond()+"  "+solution.getFirst());
			if (bestSoFar == null){ bestSoFar = solution;
			}
			else {

				if (solution.getSecond() < bestSoFar.getSecond()) {
//					System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXX");
//					System.out.println(bestSoFar.getSecond());
//					System.out.println(bestSoFar.getFirst());
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
		} while (timesConverged < iters); //timesConverged


		Instant end = Instant.now();
		logger.info("Runtime "+ String.valueOf(Duration.between(start, end).getSeconds()));
		JSONObject obj = new JSONObject();
		try {
			obj.put("runtime",Duration.between(start, end).getSeconds());
			obj.put("utility",bestSoFar.getSecond());
			obj.put("location",bestSoFar.getFirst().split("\n")[0]);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return  obj;
	}

}
