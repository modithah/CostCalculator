package edu.upc.essi.catalog.metadata;

import java.util.HashMap;
import java.util.List;

import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;

import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.cost.CostGenerator2;
import edu.upc.essi.catalog.ops.CostOperations;
import edu.upc.essi.catalog.ops.Graphoperations;

public final class GenerateMetadata {

	CostGenerator2 c;

	public GenerateMetadata() {
		// TODO Auto-generated constructor stub
		c = new CostGenerator2();
	}

	public void setSizeandMultipliers(HyperGraph graph) {

//	for (Hyperedge hyp : designs) {

//		logger.info("============================"+ hyp.getName() + "==================");
		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph); // GetFirstLevelsOfDesign(hyp);

		for (Hyperedge hyperedge : firstLevels) {
//			logger.info("000000000000000000000       " + graph.getHandle(hyperedge));
//		c.CalculateSize(hyperedge, HyperedgeTypeEnum.Database_Doc);
			Pair<Double, HashMap<Atom, Double>> data = CostOperations.CalculateSize(graph, hyperedge);
			hyperedge.setSize(data.getFirst());
			hyperedge.setMultipliers(data.getSecond());
			graph.update(hyperedge);
//			hyperedge.print(0);
//			((Hyperedge) graph.get(graph.getHandle(hyperedge))).print(0);
		}

	}
}
