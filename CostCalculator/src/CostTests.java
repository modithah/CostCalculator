import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.DataIndexMetadata;
import edu.upc.essi.catalog.core.constructs.DataIndexMetadata.DataType;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.cost.CostResult;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;
import edu.upc.essi.catalog.optimizer.CostCalculator;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGQuery;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CostTests {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		ArrayList<String> heroes = new ArrayList<String>();
//		heroes.add("a");
//		heroes.add("b");
//		heroes.add("c");
//		heroes.add("d");
//		heroes.add("e");
//		heroes.add("f");
//		heroes.add("g");
//		
//		int num=3;
//		
//		Set<Set<String>> combos = Sets.combinations(ImmutableSet.copyOf(heroes),num);
//
//		Iterator<Set<String>> comboIterator = combos.iterator();
//		while (comboIterator.hasNext()) {
//			Set<java.lang.String> set = (Set<java.lang.String>) comboIterator.next();
//			logger.info(set);
//		}
//		
//		
//		String a ="Hello world!";
//		logger.info(a.substring(6,12)+a.substring(12,6));

//		HyperGraph graph = new HyperGraph("C:\\hyper\\test\\1\\79da4b19-0efa-4850-964f-58ac358a79a4\\6b7c3bc2-0772-48a5-9331-031f2ebf78e8\\80d92f06-f301-4cf2-8e29-9adcaba502e3");
//		HyperGraph graph = new HyperGraph("C:\\hyper\\test\\2\\e8a14f86-25f9-47e9-835f-3cb90afe30e0");
//		HyperGraph graph = new HyperGraph("C:\\hyper\\test\\2\\8e75f39f-248f-49e9-86ac-ca918f42f355");
//		Graphoperations.printDesign(graph);
//		HyperGraph graph2 = new HyperGraph("C:\\hyper\\test\\2\\98df0f0c-8fb1-47da-9f07-df044b4926a6");
		HyperGraph graph2 = new HyperGraph("C:\\hyper\\test\\2\\7d31a356-4e8d-4b4c-9d9b-cf291a232ba3\\fbd66f4f-9c2a-4997-8702-9d5ba57ee868");
		System.out.println(Graphoperations.JSONDesign(graph2));
//		logger.info(String.valueOf(Transformations.getEmbedCandidates(graph).size()));
//		logger.info(Transformations.getFlattenCandidates(graph).size());
//		logger.info(Transformations.getGroupCandidates(graph).size());
//		logger.info(Transformations.getSegregateCandidates(graph).size());
//		logger.info(Transformations.getUnionCandidates(graph).size());

//		Graphoperations.printDesign(graph);
//		CostCalculator.calculateCost(graph);
//		CostResult c1 = CostCalculator.calculateCost(graph);
//		CostResult c2 = CostCalculator.calculateCost(graph2);
//		System.out.println(c1.getStorageSize()+"  "+c1.getQueryCosts().stream().reduce(Double::sum).get());
//		System.out.println(c2.getStorageSize()+"  "+c2.getQueryCosts().stream().reduce(Double::sum).get());
////		Graphoperations.printDesign(graph);
//		Transformations.getUnionCandidates(graph);
//		Graphoperations.printDesign(graph);
//		 ArrayList<Pair<Hyperedge, Hyperedge>> x = Transformations.getUnionCandidates(graph);
//		x.forEach(y->{
//			logger.info(graph.getHandle(y.getFirst())+"   "+ graph.getHandle(y.getSecond()));
//		});

//		GenerateMetadata metadataGen =  new GenerateMetadata();
//		metadataGen .setSizeandMultipliers(graph);
////		Graphoperations.printDesign(graph);
//
//		ArrayList<Pair<Double, ArrayList<Atom>>> workload = Workload.getWorkload(graph);
//		QueryFrequencies freq = QueryCalculator.CalculateFrequency(workload, graph);
//		ArrayList<Pair<ArrayList<Atom>, HashMap<Hyperedge, Map<Atom, Double>>>> queryFrequencies = freq
//				.getQueryFrequencies();
//		HashMap<Hyperedge, Map<Atom, Double>> global = freq.getGlobalFrequencies();
//
//
//		// write the dynamic solver
//		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph);
//		HashMap<String, String> elmToString = new HashMap<>();
//		MutableDouble collectionSize = new MutableDouble(0.0);
//		ArrayList<ArrayList<DataIndexMetadata>> schema = generateSolverData(graph, global, firstLevels, elmToString,
//				collectionSize);
//		SolverWriter solver = new SolverWriter();
//		solver.generateCode(schema);
//		GenerateMetadata metadataGen = new GenerateMetadata();
//		metadataGen.setSizeandMultipliers(graph);
//		
//		Hyperedge flattenCand = Transformations.getFlattenCandidates(graph).get(0);
//		logger.info("flatten " + flattenCand);
//		Transformations.flatten(graph, flattenCand);

//		metadataGen.setSizeandMultipliers(graph);
//		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph); // GetFirstLevelsOfDesign(hyp);
//		Hyperedge hyperedge = firstLevels.get(0);
//		
//		hyperedge.findAll().forEach(x->{
//			Hyperedge y = graph.get(x);
//			List<HGHandle> n = Graphoperations.getParentHyperedges(graph, x);
//			for (HGHandle hgHandle : n) {
//				logger.info(graph.get(hgHandle).toString());
//			}
//			logger.info(.getIncidenceSet());
//			graph.getIncidenceSet(x).forEach(l->{
//				logger.info(graph.get(l).toString());
//			});
//		});
//		logger.info("FFFFFFFFFFFFFFFFFFFFFFF");
//		hyperedge.print(0);
//		GroupParams y = Transformations.getGroupCandidates(graph).get(0);
//		 logger.info(y);
//		Transformations.group(graph, y);
//		Graphoperations.printDesign();

//		metadataGen.setSizeandMultipliers(graph);
//		
//		firstLevels = Graphoperations.getAllFirstLevels(graph); // GetFirstLevelsOfDesign(hyp);
//		hyperedge = firstLevels.get(0);
//		logger.info("FFFFFFFFFFFFFFFFFFFFFFF");
//		hyperedge.print(0);

//			logger.info("000000000000000000000       " + graph.getHandle(hyperedge));
//		c.CalculateSize(hyperedge, HyperedgeTypeEnum.Database_Doc);
//			Pair<Double, HashMap<Atom, Double>> data = CostOperations.CalculateSize(graph, hyperedge);
//			hyperedge.setSize(data.getFirst());
//			hyperedge.setMultipliers(data.getSecond());
//			graph.update(hyperedge);

//			((Hyperedge) graph.get(graph.getHandle(hyperedge))).print(0);

//		Graphoperations.printDesign();
//		firstLevels.forEach(y ->{
//			y.print(0);
//			logger.info("###");
//		});
//		ArrayList<Pair<Hyperedge, Hyperedge>> unions = Transformations.getUnionCandidates(graph);
//		logger.info(unions.size());
//		Transformations.union(graph, unions.get(0).getFirst(), unions.get(0).getSecond());
//		unions = Transformations.getUnionCandidates(graph);
//		logger.info(unions.size());
//		ArrayList<Pair<Hyperedge, Hyperedge>> segregates = Transformations.getSegregateCandidates(graph);
//		logger.info(segregates.size());
//		logger.info(segregates.get(0));
//		logger.info(segregates.get(1));
//		metadataGen.setSizeandMultipliers(graph);
//		firstLevels.get(0).print(0);
//		Transformations.segregate(graph, segregates.get(1).getFirst(), segregates.get(1).getSecond());
//		firstLevels = Graphoperations.getAllFirstLevels(graph);
//		logger.info("------------");
//		firstLevels.forEach(y ->{
//			y.print(0);
//			logger.info("###");
//		});
		
//		Graphoperations.printDesign(graph);
//		Pair<Double, HashMap<Atom, Double>> data = CostOperations.CalculateSize(graph, firstLevels.get(0));
//		firstLevels.get(0).print(0);
		
//		ArrayList<Hyperedge> embedCandidates = Transformations.getFlattenCandidates(graph);
//		logger.info(embedCandidates);
//		logger.info("PARENT");
//		embedCandidate.getParent().print(0);
//		logger.info("CHILD");
//		embedCandidate.getChild().print(0);
//		logger.info("GRAND PARENT");
//		embedCandidate.getGrandParent().print(0);
//		Transformations.embed(graph, embedCandidate);
//		Graphoperations.printDesign(graph);
		
	}

	public static boolean flatten(HyperGraph graph, Hyperedge hyp) {
		hyp = graph.get(graph.findOne(HGQuery.hg.eq(hyp)));
		HGHandle hypHandle = graph.getHandle(hyp);
		HGHandle parentHandle = hyp.getParents().get(0); //graph.findAll(hg.contains(hypHandle)).get(0);
		Hyperedge parent = graph.get(parentHandle);

		if (!(parent.getType() == HyperedgeTypeEnum.Struct || parent.getType() == HyperedgeTypeEnum.SecondLevel)) {
			logger.info("parent must be a struct to flatten");
			return false;
		}

		// Add children to the first hyperedge
		List<HGHandle> children = hyp.findAll();
		List<HGHandle> remove = new ArrayList<>();

		for(HGHandle child : children){
			parent.add(child);
			remove.add(child);
		}

		for (HGHandle child : remove) {
			hyp.remove(child);
		}

		parent.remove(hypHandle);
		graph.remove(hypHandle);
		graph.update(parent);
		return true;
	}


	private static ArrayList<ArrayList<DataIndexMetadata>> generateSolverData(HyperGraph graph,
																			  HashMap<Hyperedge, Map<Atom, Double>> global, List<Hyperedge> firstLevels,
																			  HashMap<String, String> elmToString, MutableDouble collectionSize) {
	ArrayList<ArrayList<DataIndexMetadata>> schema = new ArrayList<>();
	ArrayList<Hyperedge> notUsed = new ArrayList<>();

	int collectionCounter = 0;
	for (Hyperedge hyperedge : firstLevels) {

		collectionSize.add(hyperedge.getSize());
		logger.info("FFFFFFFFFFFFFFFFFFFFFFFFFFFF" + collectionSize);
		if (global.get(hyperedge).get(new Atom("~dummy")) != 0) {
			collectionCounter++;
			String collectionPrefix = "collection_" + collectionCounter;
			elmToString.put(hyperedge.getName(), collectionPrefix);
			ArrayList<DataIndexMetadata> col = new ArrayList<>();

//			logger.info(global.get(hyperedge));

			double hyperedgecount = 0.0;
			List<HGHandle> secondLevels = hyperedge.findAll();

			if (secondLevels.size() == 1) {
				int indexCount = 1;
				Hyperedge secondlevelHyperedge = (Hyperedge) graph.get(secondLevels.get(0));
				Atom secondLevelRoot = graph.get(secondlevelHyperedge.getRoot());
				hyperedgecount = hyperedge.getMultipliers().get(secondLevelRoot);
				if (global.get(hyperedge).get(secondLevelRoot) != 0) {
//					logger.info(global.get(hyperedge).get(secondLevelRoot));
					col.add(new DataIndexMetadata(false, 1, hyperedgecount,
							global.get(hyperedge).get(secondLevelRoot), DataType.UUID));

					elmToString.put(hyperedge.getName() + "~" + secondLevelRoot.getName(),
							collectionPrefix + "_index_" + indexCount);
					indexCount++;
				}

				for (Atom atom : global.get(hyperedge).keySet()) {
					if (!atom.getName().equals("~dummy") && !atom.getName().equals(secondLevelRoot.getName())
							&& global.get(hyperedge).get(atom) != 0) {
					logger.info(atom.getName()+"--->"+hyperedge.getMultipliers());
						col.add(new DataIndexMetadata(false, hyperedge.getMultipliers().get(atom) / hyperedgecount,
								hyperedge.getMultipliers().get(atom), global.get(hyperedge).get(atom),
								DataType.INT));
						elmToString.put(hyperedge.getName() + "~" + atom.getName(),
								collectionPrefix + "_index_" + indexCount);
						logger.info(hyperedge.getName() + "~" + atom.getName());
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
//			logger.info(col);
			schema.add(col);
		} else {
			notUsed.add(hyperedge);
		}
	}
	firstLevels.removeAll(notUsed);
//	logger.info(elmToString);
	return schema;

	}

	private static ArrayList<Pair<Double, ArrayList<Atom>>> getWorkload(HyperGraph graph) {
		// TODO Auto-generated method stub
		return null;
	}

}
