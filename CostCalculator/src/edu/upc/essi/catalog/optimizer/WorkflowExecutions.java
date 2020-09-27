package edu.upc.essi.catalog.optimizer;

import edu.upc.essi.catalog.IO.python.SolverCaller;
import edu.upc.essi.catalog.IO.python.SolverWriter;
import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.*;
import edu.upc.essi.catalog.core.constructs.DataIndexMetadata.DataType;
import edu.upc.essi.catalog.cost.CostResult;
import edu.upc.essi.catalog.generators.GenerateRandomDesign;
import edu.upc.essi.catalog.metadata.GenerateMetadata;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.query.calculation.QueryCalculator;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.collections.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowExecutions {

	public static void main(String[] args) {
		run();
	}

	public static Pair<HyperGraph,Double> run() {
		GenerateRandomDesign generator = new GenerateRandomDesign();
		try {
			// Generate a random design
			generator.main(new String[]{});
			HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
			CostResult result = CostCalculator.calculateCost(graph);
			System.out.println("-----Final Result------");
			System.out.println(result);

			return null;
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}




}
