import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.hp.hpl.jena.sparql.algebra.Transform;

import edu.upc.essi.catalog.IO.python.SolverWriter;
import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.DataIndexMetadata;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.QueryFrequencies;
import edu.upc.essi.catalog.core.constructs.Workload;
import edu.upc.essi.catalog.core.constructs.DataIndexMetadata.DataType;
import edu.upc.essi.catalog.core.constructs.opsparams.EmbedParams;
import edu.upc.essi.catalog.core.constructs.opsparams.GroupParams;
import edu.upc.essi.catalog.metadata.GenerateMetadata;
import edu.upc.essi.catalog.ops.CostOperations;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;
import edu.upc.essi.catalog.query.calculation.QueryCalculator;

public class TransformationTests {

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
//			System.out.println(set);
//		}
//		
//		
//		String a ="Hello world!";
//		System.out.println(a.substring(6,12)+a.substring(12,6));

		HyperGraph graph = new HyperGraph("C:\\hyper\\test\\1\\42a709c0-b9b0-44ae-a1f0-abedf1a06b97");
//		Transformations.getUnionCandidates(graph);
//		Graphoperations.printDesign(graph);
//		 ArrayList<Pair<Hyperedge, Hyperedge>> x = Transformations.getUnionCandidates(graph);
//		x.forEach(y->{
//			System.out.println(graph.getHandle(y.getFirst())+"   "+ graph.getHandle(y.getSecond()));
//		});

		GenerateMetadata metadataGen =  new GenerateMetadata();
		metadataGen .setSizeandMultipliers(graph);
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
//		System.out.println("flatten " + flattenCand);
//		Transformations.flatten(graph, flattenCand);

//		metadataGen.setSizeandMultipliers(graph);
//		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph); // GetFirstLevelsOfDesign(hyp);
//		Hyperedge hyperedge = firstLevels.get(0);
//		
//		hyperedge.findAll().forEach(x->{
//			Hyperedge y = graph.get(x);
//			List<HGHandle> n = Graphoperations.getParentHyperedges(graph, x);
//			for (HGHandle hgHandle : n) {
//				System.out.println(graph.get(hgHandle).toString());
//			}
//			System.out.println(.getIncidenceSet());
//			graph.getIncidenceSet(x).forEach(l->{
//				System.out.println(graph.get(l).toString());
//			});
//		});
//		System.out.println("FFFFFFFFFFFFFFFFFFFFFFF");
//		hyperedge.print(0);
//		GroupParams y = Transformations.getGroupCandidates(graph).get(0);
//		 System.out.println(y);
//		Transformations.group(graph, y);
//		Graphoperations.printDesign();

//		metadataGen.setSizeandMultipliers(graph);
//		
//		firstLevels = Graphoperations.getAllFirstLevels(graph); // GetFirstLevelsOfDesign(hyp);
//		hyperedge = firstLevels.get(0);
//		System.out.println("FFFFFFFFFFFFFFFFFFFFFFF");
//		hyperedge.print(0);

//			System.out.println("000000000000000000000       " + graph.getHandle(hyperedge));
//		c.CalculateSize(hyperedge, HyperedgeTypeEnum.Database_Doc);
//			Pair<Double, HashMap<Atom, Double>> data = CostOperations.CalculateSize(graph, hyperedge);
//			hyperedge.setSize(data.getFirst());
//			hyperedge.setMultipliers(data.getSecond());
//			graph.update(hyperedge);

//			((Hyperedge) graph.get(graph.getHandle(hyperedge))).print(0);

//		Graphoperations.printDesign();
//		firstLevels.forEach(y ->{
//			y.print(0);
//			System.out.println("###");
//		});
//		ArrayList<Pair<Hyperedge, Hyperedge>> unions = Transformations.getUnionCandidates(graph);
//		System.out.println(unions.size());
//		Transformations.union(graph, unions.get(0).getFirst(), unions.get(0).getSecond());
//		unions = Transformations.getUnionCandidates(graph);
//		System.out.println(unions.size());
//		ArrayList<Pair<Hyperedge, Hyperedge>> segregates = Transformations.getSegregateCandidates(graph);
//		System.out.println(segregates.size());
//		System.out.println(segregates.get(0));
//		System.out.println(segregates.get(1));
//		metadataGen.setSizeandMultipliers(graph);
//		firstLevels.get(0).print(0);
//		Transformations.segregate(graph, segregates.get(1).getFirst(), segregates.get(1).getSecond());
//		firstLevels = Graphoperations.getAllFirstLevels(graph);
//		System.out.println("------------");
//		firstLevels.forEach(y ->{
//			y.print(0);
//			System.out.println("###");
//		});
		
//		Graphoperations.printDesign(graph);
//		Pair<Double, HashMap<Atom, Double>> data = CostOperations.CalculateSize(graph, firstLevels.get(0));
//		firstLevels.get(0).print(0);
		
//		ArrayList<Hyperedge> embedCandidates = Transformations.getFlattenCandidates(graph);
//		System.out.println(embedCandidates);
//		System.out.println("PARENT");
//		embedCandidate.getParent().print(0);
//		System.out.println("CHILD");
//		embedCandidate.getChild().print(0);
//		System.out.println("GRAND PARENT");
//		embedCandidate.getGrandParent().print(0);
//		Transformations.embed(graph, embedCandidate);
//		Graphoperations.printDesign(graph);
		
	}

	private static ArrayList<ArrayList<DataIndexMetadata>> generateSolverData(HyperGraph graph,
			HashMap<Hyperedge, Map<Atom, Double>> global, List<Hyperedge> firstLevels,
			HashMap<String, String> elmToString, MutableDouble collectionSize) {
	ArrayList<ArrayList<DataIndexMetadata>> schema = new ArrayList<>();
	ArrayList<Hyperedge> notUsed = new ArrayList<>();

	int collectionCounter = 0;
	for (Hyperedge hyperedge : firstLevels) {

		collectionSize.add(hyperedge.getSize());
		System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFF" + collectionSize);
		if (global.get(hyperedge).get(new Atom("~dummy")) != 0) {
			collectionCounter++;
			String collectionPrefix = "collection_" + collectionCounter;
			elmToString.put(hyperedge.getName(), collectionPrefix);
			ArrayList<DataIndexMetadata> col = new ArrayList<>();

//			System.out.println(global.get(hyperedge));

			double hyperedgecount = 0.0;
			List<HGHandle> secondLevels = hyperedge.findAll();

			if (secondLevels.size() == 1) {
				int indexCount = 1;
				Hyperedge secondlevelHyperedge = (Hyperedge) graph.get(secondLevels.get(0));
				Atom secondLevelRoot = graph.get(secondlevelHyperedge.getRoot());
				hyperedgecount = hyperedge.getMultipliers().get(secondLevelRoot);
				if (global.get(hyperedge).get(secondLevelRoot) != 0) {
//					System.out.println(global.get(hyperedge).get(secondLevelRoot));
					col.add(new DataIndexMetadata(false, 1, hyperedgecount,
							global.get(hyperedge).get(secondLevelRoot), DataType.UUID));

					elmToString.put(hyperedge.getName() + "~" + secondLevelRoot.getName(),
							collectionPrefix + "_index_" + indexCount);
					indexCount++;
				}

				for (Atom atom : global.get(hyperedge).keySet()) {
					if (!atom.getName().equals("~dummy") && !atom.getName().equals(secondLevelRoot.getName())
							&& global.get(hyperedge).get(atom) != 0) {
					System.out.println(atom.getName()+"--->"+hyperedge.getMultipliers());
						col.add(new DataIndexMetadata(false, hyperedge.getMultipliers().get(atom) / hyperedgecount,
								hyperedge.getMultipliers().get(atom), global.get(hyperedge).get(atom),
								DataType.INT));
						elmToString.put(hyperedge.getName() + "~" + atom.getName(),
								collectionPrefix + "_index_" + indexCount);
						System.out.println(hyperedge.getName() + "~" + atom.getName());
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
//			System.out.println(col);
			schema.add(col);
		} else {
			notUsed.add(hyperedge);
		}
	}
	firstLevels.removeAll(notUsed);
//	System.out.println(elmToString);
	return schema;

	}

	private static ArrayList<Pair<Double, ArrayList<Atom>>> getWorkload(HyperGraph graph) {
		// TODO Auto-generated method stub
		return null;
	}

}
