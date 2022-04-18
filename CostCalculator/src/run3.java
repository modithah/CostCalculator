import java.io.FileNotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.QueryFrequencies;
import edu.upc.essi.catalog.cost.CostGenerator2;
import edu.upc.essi.catalog.ops.CostOperations;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.query.calculation.QueryCalculator;
import edu.upc.essi.catalog.query.calculation.SizeComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class run3 {

	public run3() {
		// TODO Auto-generated constructor stub
	}
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

	public static void main(String[] args) throws IllegalStateException, FileNotFoundException {
		// TODO Auto-generated method stub
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//		ReadTest.main(args);

//		List<Hyperedge> designs = Graphoperations.getAllDesigns();

//		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels();
//		logger.info(firstLevels);
		CostGenerator2 c = new CostGenerator2();

//		for (Hyperedge hyp : designs) {

//			logger.info("============================"+ hyp.getName() + "==================");
		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph); // GetFirstLevelsOfDesign(hyp);
		
		for (Hyperedge hyperedge : firstLevels) {
		logger.info("000000000000000000000       " + graph.getHandle(hyperedge));
//			c.CalculateSize(hyperedge, HyperedgeTypeEnum.Database_Doc);
		Pair<Double, HashMap<Atom, Double>> data = CostOperations.CalculateSize(graph, hyperedge);
		hyperedge.setSize(data.getFirst());
		hyperedge.setMultipliers(data.getSecond());
		graph.update(hyperedge);
		hyperedge.print(0);
		((Hyperedge) graph.get(graph.getHandle(hyperedge))).print(0);
	}

		for (Hyperedge hyperedge : firstLevels) {

			logger.info(String.valueOf(hyperedge.getSize()));
		}
		Collections.sort(firstLevels, new SizeComparator());

		for (Hyperedge hyperedge : firstLevels) {
			logger.info(String.valueOf(hyperedge.getSize()));
		}
//

//

		////// Calculating frequency

		ArrayList<Atom> query = new ArrayList<>();
		query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
		query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
		query.add(graph.get(Graphoperations.getAtomByName(graph, "P_ID")));
		Pair<Double, ArrayList<Atom>> p = new Pair<Double, ArrayList<Atom>>(1.0, query);

		ArrayList<Atom> query2 = new ArrayList<>();
//		query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
//		query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
		query2.add(graph.get(Graphoperations.getAtomByName(graph, "P_ID")));
		Pair<Double, ArrayList<Atom>> p1 = new Pair<Double, ArrayList<Atom>>(1.0, query2);

		ArrayList<Pair<Double, ArrayList<Atom>>> allQ = new ArrayList<>();
		allQ.add(p);
		allQ.add(p1);

		QueryFrequencies x = QueryCalculator.CalculateFrequency(allQ, graph);
		logger.info("-----------");
		
		logger.info(String.valueOf(x.getGlobalFrequencies()));
		
//		x.getQueryFrequencies().forEach(y -> {
//			y.getSecond().keySet().forEach(z -> {
//				logger.info(z+"  "+y.getSecond().get(z));
//			});
//			logger.info("******");
//		});
//		for (Hyperedge pair : x.keySet()) {
//			logger.info(pair);
//			logger.info(x.get(pair));
//		}
	}
}
