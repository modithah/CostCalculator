package edu.upc.essi.catalog.optimizer;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.cost.CostResult;
import edu.upc.essi.catalog.generators.GenerateRandomDesign;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;

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
			++iterations;
			solution = WorkflowExecutions.run();
			if (bestSoFar == null) bestSoFar = solution;
			else {
				if (bestSoFar.getSecond() < solution.getSecond()) {
					timesConverged = 0;
					bestSoFar = solution;
				} else {
					++timesConverged;
				}
			}
		} while (timesConverged < 30);

	}

}
