import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSet;
import org.hypergraphdb.util.Pair;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPlainLink;
import org.hypergraphdb.HGQuery.hg;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.AdjacencyList;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.cost.CostGenerator;
import edu.upc.essi.catalog.cost.CostGenerator2;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.estocada.CreateGraph;
import edu.upc.essi.catalog.estocada.CreateGraph2;
import edu.upc.essi.catalog.loaders.LoadGraph;
import edu.upc.essi.catalog.ops.CostOperations;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.SchemaOperations;
import edu.upc.essi.catalog.query.QueryGenerator;
import edu.upc.essi.catalog.query.calculation.QueryCalculator;
import edu.upc.essi.catalog.query.calculation.SizeComparator;

public class run3 {

	public run3() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IllegalStateException, FileNotFoundException {
		// TODO Auto-generated method stub
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//		ReadTest.main(args);

//		List<Hyperedge> designs = Graphoperations.getAllDesigns();

//		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels();
//		System.out.println(firstLevels);
		CostGenerator2 c = new CostGenerator2();

//		for (Hyperedge hyp : designs) {

//			System.out.println("============================"+ hyp.getName() + "==================");
		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph); // GetFirstLevelsOfDesign(hyp);

		for (Hyperedge hyperedge : firstLevels) {

			System.out.println(hyperedge.getSize());
		}
		Collections.sort(firstLevels, new SizeComparator());

		for (Hyperedge hyperedge : firstLevels) {
			System.out.println(hyperedge.getSize());
		}
//
//		for (Hyperedge hyperedge : firstLevels) {
//			System.out.println("000000000000000000000       " + graph.getHandle(hyperedge));
////				c.CalculateSize(hyperedge, HyperedgeTypeEnum.Database_Doc);
//			Pair<Double, HashMap<Atom, Double>> data = CostOperations.CalculateSize(graph, hyperedge);
//			hyperedge.setSize(data.getFirst());
//			hyperedge.setMultipliers(data.getSecond());
//			graph.update(hyperedge);
//			hyperedge.print(0);
//			((Hyperedge) graph.get(graph.getHandle(hyperedge))).print(0);
//		}
//

		////// Calculating frequency

		ArrayList<Atom> query = new ArrayList<>();
		query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
		query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
		query.add(graph.get(Graphoperations.getAtomByName(graph, "P_ID")));
		Pair<Double, ArrayList<Atom>> p= new Pair<Double, ArrayList<Atom>>(1.0, query);
		
		
		ArrayList<Atom> query2 = new ArrayList<>();
//		query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
//		query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
		query2.add(graph.get(Graphoperations.getAtomByName(graph, "P_ID")));
		Pair<Double, ArrayList<Atom>> p1= new Pair<Double, ArrayList<Atom>>(1.0, query2);
		
		
		
		ArrayList<Pair<Double, ArrayList<Atom>>> allQ= new ArrayList<>();
		allQ.add(p);
		allQ.add(p1); 
		
		HashMap<Hyperedge, Map<Atom, Double>> x = QueryCalculator.CalculateFrequency(allQ, graph);

		for (Hyperedge pair : x.keySet()) {
			System.out.println(pair);
			System.out.println(x.get(pair));
		}
	}
}
