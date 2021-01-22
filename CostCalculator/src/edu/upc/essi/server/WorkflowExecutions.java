package edu.upc.essi.server;

import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.problem.Problem;
import aima.core.search.local.HillClimbingSearch;
import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.generators.GenerateRandomDesign;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.optimizer.DocDesignActionsFunction;
import edu.upc.essi.catalog.optimizer.DocDesignGoalTest;
import edu.upc.essi.catalog.optimizer.DocDesignHeuristic;
import edu.upc.essi.catalog.optimizer.DocDesignResultsFunction;
import edu.upc.essi.catalog.optimizer.costfunctions.NormalizedWeightedSum_DG;
import edu.upc.essi.catalog.optimizer.costfunctions.SingletonMultiObjectiveDesignGoal;
import org.assertj.core.util.Sets;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;

import java.io.File;
import java.util.Set;
import java.util.UUID;

public class WorkflowExecutions {


	public static Set<String> usedConfigurations;

	public static Set<String> optimalConfigurations;
	public static Set<String> worstConfigurations;
	public static double optimalh;
	public static double worsth;


	public Pair<String, Double> run(double query, double size, double depth, double hetro, String jsonSchema) {

		usedConfigurations = Sets.newHashSet();
		HyperGraph out = null;
		optimalConfigurations = Sets.newHashSet();
		worstConfigurations = Sets.newHashSet();
		optimalh = Double.POSITIVE_INFINITY;
		worsth = Double.NEGATIVE_INFINITY;
		Pair<String, Double> val = null;
		//GenerateRandomDesign generator = new GenerateRandomDesign();
		try {
			// Generate a random design
			//generator.main(new String[]{});
			//HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK+File.separator+UUID.randomUUID().toString());
			HyperGraph G = GenerateRandomDesign.get(Const.HG_LOCATION_BOOK+ File.separator+UUID.randomUUID(),jsonSchema);
			SingletonMultiObjectiveDesignGoal.INSTANCE.init(new NormalizedWeightedSum_DG(G,query,size,depth,hetro));

			Problem problem = new Problem(G,new DocDesignActionsFunction(),new DocDesignResultsFunction(),new DocDesignGoalTest());
			HillClimbingSearch search =  new HillClimbingSearch(new DocDesignHeuristic());
			SearchAgent agent = new SearchAgent(problem,search);
			out = (HyperGraph)search.getLastSearchState();
			 val = new Pair<>(Graphoperations.stringDesign(out), SingletonMultiObjectiveDesignGoal.INSTANCE.getDG().evaluate(out));
			G.close();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return val ;
	}




}
