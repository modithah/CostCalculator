package edu.upc.essi.catalog.optimizer;

import edu.upc.essi.catalog.IO.python.SolverCaller;
import edu.upc.essi.catalog.IO.python.SolverWriter;
import edu.upc.essi.catalog.core.constructs.*;
import edu.upc.essi.catalog.cost.CostResult;
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

public class CostCalculator {

	public static CostResult calculateCost(HyperGraph graph) throws JSONException {
		GenerateMetadata metadataGen = new GenerateMetadata();
		metadataGen.setSizeandMultipliers(graph);

		ArrayList<Pair<Double, ArrayList<Atom>>> workload = Workload.getWorkload(graph);

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

		return result;
	}

	private static void calculateQueryCosts(
			ArrayList<Pair<ArrayList<Atom>, HashMap<Hyperedge, Map<Atom, Double>>>> queryFrequencies,
			List<Hyperedge> firstLevels, HashMap<String, String> elmToString, JSONObject missRates, CostResult result) {
		try {
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
//			System.out.println("FFFFFFFFFFFFFFFFFFFF" + hyperedge.getSize() + "    "+ hyperedge.getName());
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
								global.get(hyperedge).get(secondLevelRoot), DataIndexMetadata.DataType.UUID));

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
									DataIndexMetadata.DataType.INT));
							elmToString.put(hyperedge.getName() + "~" + atom.getName(),
									collectionPrefix + "_index_" + indexCount);
//							System.out.println(collectionPrefix+"_index_"+indexCount);
							indexCount++;
						}
					}
				} else {
					System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX");
//                	System.out.println(global);
					int indexCount = 1;
					for (HGHandle secondLevel : secondLevels) {
						Hyperedge secondlevelHyperedge = (Hyperedge) graph.get(secondLevel);
						Atom secondLevelRoot = graph.get(secondlevelHyperedge.getRoot());
						hyperedgecount += hyperedge.getMultipliers().get(secondLevelRoot);
					}

					System.out.println("GGGGGGGGGGGGGGGG  " + hyperedgecount);
					for (HGHandle secondLevel : secondLevels) {

						Hyperedge secondlevelHyperedge = (Hyperedge) graph.get(secondLevel);
						Atom secondLevelRoot = graph.get(secondlevelHyperedge.getRoot());
//                        hyperedgecount += hyperedge.getMultipliers().get(secondLevelRoot);
						if (global.get(hyperedge).get(secondLevelRoot) != 0) {
//    						System.out.println(global.get(hyperedge).get(secondLevelRoot));
//                            col.add(new DataIndexMetadata(false, 1, hyperedgecount,
//                                    global.get(hyperedge).get(secondLevelRoot), DataIndexMetadata.DataType.UUID));

							elmToString.put(hyperedge.getName() + "~" + secondLevelRoot.getName(),
									collectionPrefix + "_index_" + indexCount);
							indexCount++;
						}

						for (Atom atom : global.get(hyperedge).keySet()) {
							if (!atom.getName().equals("~dummy") && !atom.getName().equals(secondLevelRoot.getName())
									&& global.get(hyperedge).get(atom) != 0) {
//    						System.out.println(hyperedge.getMultipliers().get(atom));
								col.add(new DataIndexMetadata(false,
										hyperedge.getMultipliers().get(atom) / hyperedgecount,
										hyperedge.getMultipliers().get(atom), global.get(hyperedge).get(atom),
										DataIndexMetadata.DataType.INT));
								elmToString.put(hyperedge.getName() + "~" + atom.getName(),
										collectionPrefix + "_index_" + indexCount);
								System.out.println(collectionPrefix + "_index_" + indexCount);
								indexCount++;
							}
						}

					}
				}

				col.add(new DataIndexMetadata(true, hyperedge.getSize() / hyperedgecount, hyperedgecount,
						global.get(hyperedge).get(new Atom("~dummy")), DataIndexMetadata.DataType.DATA));
//				System.out.println(col);
				schema.add(col);
			} else {
				notUsed.add(hyperedge);
			}
		}
		firstLevels.removeAll(notUsed);
		return schema;
	}

}
