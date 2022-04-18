package edu.upc.essi.catalog.tests;

import com.opencsv.CSVWriter;
import edu.upc.essi.catalog.optimizer.WorkflowExecutions;
import edu.upc.essi.catalog.optimizer.costfunctions.SingletonMultiObjectiveDesignGoal;
import org.hypergraphdb.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;

public class TestShotgunHillClimbing {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    public TestShotgunHillClimbing() {
        // TODO Auto-generated constructor stub
    }
    public static void main(String[] args) {

        int rounds = 5;
        try {
            CSVWriter pw = new CSVWriter(new FileWriter("2col-8-1-union.csv"));
pw.writeNext(new String[] { "current","best","runs","iter","time"});
            for (int i = 0; i < rounds; i++) {


                //Shotgun hill-climbing
                Pair<String, Double> solution = null;
                Pair<String, Double> bestSoFar = null;
                int timesConverged = 0;
                int iterations = 0;
                long start = System.nanoTime();
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
                            bestSoFar = solution;
                        } else {
                            ++timesConverged;
                        }
                    }
                    pw.writeNext(new String[] { String.valueOf(solution.getSecond()) ,  String.valueOf(bestSoFar.getSecond()),String.valueOf(iterations),String.valueOf(i)});
                    pw.flush();
//			logger.info("bestSoFar cost = "+bestSoFar.getSecond());
                    logger.info(i+"  !!!!!!!!  "+timesConverged);
                    SingletonMultiObjectiveDesignGoal.INSTANCE.destroy();
                } while (timesConverged < 20);
                pw.writeNext(new String[] { "","",String.valueOf(iterations),String.valueOf(i), String.valueOf(System.nanoTime()-start)});
//		logger.info("You are done, the solution has a cost "+bestSoFar.getSecond()+" with design \n"+bestSoFar.getFirst());

            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
