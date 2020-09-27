package edu.upc.essi.catalog.optimizer;

import edu.upc.essi.catalog.IO.python.SolverCaller;
import edu.upc.essi.catalog.IO.python.SolverWriter;
import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.DataIndexMetadata;
import edu.upc.essi.catalog.core.constructs.DataIndexMetadata.DataType;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.QueryFrequencies;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowExecutions {

	public static Pair<HyperGraph,Double> run(Double ) {
		GenerateRandomDesign generator = new GenerateRandomDesign();
		GenerateMetadata metadataGen = new GenerateMetadata();
		try {
			// Generate a random design
			generator.main(args);
			HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);

			// add the size and the multipliers
			metadataGen.setSizeandMultipliers(graph);

			ArrayList<Pair<Double, ArrayList<Atom>>> workload = getWorkload(graph);

			// get query frequencies
			QueryFrequencies freq = QueryCalculator.CalculateFrequency(workload, graph);
			ArrayList<Pair<ArrayList<Atom>, HashMap<Hyperedge, Map<Atom, Double>>>> queryFrequencies = freq
					.getQueryFrequencies();
			HashMap<Hyperedge, Map<Atom, Double>> global = freq.getGlobalFrequencies();
			
			
			// write the dynamic solver
			List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph);
			HashMap<String, String> elmToString = new HashMap<>();
			MutableDouble collectionSize = new MutableDouble(0.0);
			ArrayList<ArrayList<DataIndexMetadata>> schema = generateSolverData(graph, global, firstLevels, elmToString,
					collectionSize);
			SolverWriter solver = new SolverWriter();
			solver.generateCode(schema);

			// call the solver and get the miss rates
			JSONObject missRates = SolverCaller.getMissRates();

		
			// calculate the individual query costs and assemble the result
			CostResult result = new CostResult();
			result.setWorkload(workload);
			result.setStorageSize(collectionSize.getValue());
			calculateQueryCosts(queryFrequencies, firstLevels, elmToString, missRates, result);
			
			System.out.println("-----Final Result------");
			System.out.println(result);

			return null;
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static void calculateQueryCosts(
			ArrayList<Pair<ArrayList<Atom>, HashMap<Hyperedge, Map<Atom, Double>>>> queryFrequencies,
			List<Hyperedge> firstLevels, HashMap<String, String> elmToString, JSONObject missRates, CostResult result)
			throws JSONException {
		// The order of the workload is the same as of the query frequencies
		for (Pair<ArrayList<Atom>, HashMap<Hyperedge, Map<Atom, Double>>> pairs : queryFrequencies) {

			HashMap<Hyperedge, Map<Atom, Double>> costmap = pairs.getSecond();
			double cost = 0.0;
			for (Hyperedge hyp : firstLevels) {
//					System.out.println(hyp.getName());
				Map<Atom, Double> collectionMap = costmap.get(hyp);
				cost += collectionMap.get(new Atom("~dummy")) * missRates.getDouble(elmToString.get(hyp.getName()));
//					System.out.println("---" + cost);
				for (Atom at : collectionMap.keySet()) {
					if (!at.getName().equals("~dummy")
							&& elmToString.containsKey(hyp.getName() + "~" + at.getName())) {
//							System.out.println(at);
//							System.out.println(elmToString.get(hyp.getName() + "~" + at.getName()));
						cost += collectionMap.get(at)
								* missRates.getDouble(elmToString.get(hyp.getName() + "~" + at.getName()));
//							System.out.println("+++"+cost);
					}

				}
			}

			result.AddtoQueryCost(cost);

		}
	}

	private static ArrayList<ArrayList<DataIndexMetadata>> generateSolverData(HyperGraph graph,
			HashMap<Hyperedge, Map<Atom, Double>> global, List<Hyperedge> firstLevels,
			HashMap<String, String> elmToString, MutableDouble collectionSize) {
		ArrayList<ArrayList<DataIndexMetadata>> schema = new ArrayList<>();
		ArrayList<Hyperedge> notUsed = new ArrayList<>();

		int collectionCounter = 0;
		for (Hyperedge hyperedge : firstLevels) {

			collectionSize.add(hyperedge.getSize());
//			System.out.println(collectionSize);
			if (global.get(hyperedge).get(new Atom("~dummy")) != 0) {
				collectionCounter++;
				String collectionPrefix = "collection_" + collectionCounter;
				elmToString.put(hyperedge.getName(), collectionPrefix);
				ArrayList<DataIndexMetadata> col = new ArrayList<>();

//				System.out.println(global.get(hyperedge));

				double hyperedgecount = 0.0;
				List<HGHandle> secondLevels = hyperedge.findAll();

				if (secondLevels.size() == 1) {
					int indexCount = 1;
					Hyperedge secondlevelHyperedge = (Hyperedge) graph.get(secondLevels.get(0));
					Atom secondLevelRoot = graph.get(secondlevelHyperedge.getRoot());
					hyperedgecount = hyperedge.getMultipliers().get(secondLevelRoot);
					if (global.get(hyperedge).get(secondLevelRoot) != 0) {
//						System.out.println(global.get(hyperedge).get(secondLevelRoot));
						col.add(new DataIndexMetadata(false, 1, hyperedgecount,
								global.get(hyperedge).get(secondLevelRoot), DataType.UUID));

						elmToString.put(hyperedge.getName() + "~" + secondLevelRoot.getName(),
								collectionPrefix + "_index_" + indexCount);
						indexCount++;
					}

					for (Atom atom : global.get(hyperedge).keySet()) {
						if (!atom.getName().equals("~dummy") && !atom.getName().equals(secondLevelRoot.getName())
								&& global.get(hyperedge).get(atom) != 0) {
//						System.out.println(hyperedge.getMultipliers().get(atom));
							col.add(new DataIndexMetadata(false, hyperedge.getMultipliers().get(atom) / hyperedgecount,
									hyperedge.getMultipliers().get(atom), global.get(hyperedge).get(atom),
									DataType.INT));
							elmToString.put(hyperedge.getName() + "~" + atom.getName(),
									collectionPrefix + "_index_" + indexCount);
//							System.out.println(collectionPrefix+"_index_"+indexCount);
							indexCount++;
						}
					}
				} else {
					for (HGHandle secondLevel : secondLevels) {
						// TODO : heterogeneous collections
					}
				}

				col.add(new DataIndexMetadata(true, hyperedge.getSize() / hyperedgecount, hyperedgecount,
						global.get(hyperedge).get(new Atom("~dummy")), DataType.DATA));
//				System.out.println(col);
				schema.add(col);
			} else {
				notUsed.add(hyperedge);
			}
		}
		firstLevels.removeAll(notUsed);
		return schema;
	}

	private static ArrayList<Pair<Double, ArrayList<Atom>>> getWorkload(HyperGraph graph) {
		ArrayList<Atom> query = new ArrayList<>();
		query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
		query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
		query.add(graph.get(Graphoperations.getAtomByName(graph, "P_ID")));
		Pair<Double, ArrayList<Atom>> p = new Pair<Double, ArrayList<Atom>>(0.5, query);

		ArrayList<Atom> query2 = new ArrayList<>();
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
		query2.add(graph.get(Graphoperations.getAtomByName(graph, "P_ID")));
		Pair<Double, ArrayList<Atom>> p1 = new Pair<Double, ArrayList<Atom>>(0.5, query2);

		ArrayList<Pair<Double, ArrayList<Atom>>> allQ = new ArrayList<>();
		allQ.add(p);
		allQ.add(p1);
		return allQ;
	}

}
