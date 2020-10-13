package edu.upc.essi.catalog.optimizer;

import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;
import aima.core.search.local.HillClimbingSearch;
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
import org.assertj.core.util.Sets;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.collections.Lists;

import java.io.File;
import java.util.*;

public class WorkflowExecutions {

	public static Set<HyperGraph> usedConfigurations;

	public static Set<HyperGraph> optimalConfigurations;
	public static Set<HyperGraph> worstConfigurations;
	public static double optimalh;
	public static double worsth;

	public static void main(String[] args) {
		new WorkflowExecutions().run();
	}

	public HyperGraph run() {
		usedConfigurations = Sets.newHashSet();
		HyperGraph out = null;
		optimalConfigurations = Sets.newHashSet();
		worstConfigurations = Sets.newHashSet();
		optimalh = Double.POSITIVE_INFINITY;
		worsth = Double.NEGATIVE_INFINITY;

		GenerateRandomDesign generator = new GenerateRandomDesign();
		try {
			// Generate a random design
			generator.main(new String[]{});
			HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);

			Problem problem = new Problem(graph,new DocDesignActionsFunction(),new DocDesignResultsFunction(),new DocDesignGoalTest());
			HillClimbingSearch search =  new HillClimbingSearch(new DocDesignHeuristic());
			SearchAgent agent = new SearchAgent(problem,search);

			out = (HyperGraph)search.getLastSearchState();


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}




}
