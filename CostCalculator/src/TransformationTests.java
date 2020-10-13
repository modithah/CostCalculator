import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.hp.hpl.jena.sparql.algebra.Transform;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.opsparams.EmbedParams;
import edu.upc.essi.catalog.core.constructs.opsparams.GroupParams;
import edu.upc.essi.catalog.metadata.GenerateMetadata;
import edu.upc.essi.catalog.ops.CostOperations;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;

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

		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//		Transformations.getUnionCandidates(graph);
//		Graphoperations.printDesign();
//		 ArrayList<Pair<Hyperedge, Hyperedge>> x = Transformations.getUnionCandidates(graph);
//		x.forEach(y->{
//			System.out.println(graph.getHandle(y.getFirst())+"   "+ graph.getHandle(y.getSecond()));
//		});

		GenerateMetadata metadataGen = new GenerateMetadata();
//		Hyperedge flattenCand = Transformations.getFlattenCandidates(graph).get(0);
//		System.out.println("flatten " + flattenCand);
//		Transformations.flatten(graph, flattenCand);

		metadataGen.setSizeandMultipliers(graph);
		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph); // GetFirstLevelsOfDesign(hyp);
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

}
