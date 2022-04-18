package edu.upc.essi.catalog.cost;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashSet;

import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;

import edu.upc.essi.catalog.core.constructs.AdjacencyList;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.GenericTriple;
import edu.upc.essi.catalog.core.constructs.Triple;
import edu.upc.essi.catalog.cost.calculation.DocumentCost;
import edu.upc.essi.catalog.cost.calculation.ICost;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.query.prefixsuffix.DocumentPrefixSuffix;
import edu.upc.essi.catalog.query.prefixsuffix.IPrefixSuffix;
import edu.upc.essi.catalog.query.prefixsuffix.RelationalPrefixSuffix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CostGenerator {

	IPrefixSuffix PrefixSuffix;
	ICost Cost;
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

	public CostGenerator() {
		Cost = new DocumentCost();
		// TODO Auto-generated constructor stub
	}

	public Triple GetPrefixSuffix(Element node, String path) {
		return PrefixSuffix.GetprefixSuffix(node, path);
	}

	public GenericTriple<Double, Double, Double> GetSize(HyperGraph graph, Element node, String path) {
		return Cost.GetSize(graph, node, path);
	}
	
	

	public void CreateCostFromMap(HyperGraph graph,Element node, String path, AdjacencyList l, HyperedgeTypeEnum type) {

		switch (type) {
		case Database_Col:

			break;
		case Database_Rel:
			PrefixSuffix = new RelationalPrefixSuffix();
			break;
		case Database_Doc:
			PrefixSuffix = new DocumentPrefixSuffix();
			Cost = new DocumentCost();
			break;
		default:
			break;
		}

		for (Element element : l.getAjadacencyList(node)) {
			logger.info(CreateCostFromMap(graph,element, path, l).toString());
		}
		// return CreateQueryFromMap(node, path, l) + "\n";
	}

	public Pair<Double, Double> CreateCostFromMap(HyperGraph graph,Element node, String path, AdjacencyList l) {
//		logger.info("node -> " + node );
		Pair<Double, Double> p;
		double Q = 0;
		double mult = 1;

		GenericTriple<Double, Double, Double> pair = GetSize(graph,node, path);

		mult = pair.getVal2();
		LinkedHashSet<Element> children = l.getAjadacencyList(node);

		if (children != null) {
			for (Element element : children) {
				Pair<Double, Double> x = CreateCostFromMap(graph,element, "", l);
				Q = Q + x.getFirst();

			}
		}

//		logger.info("mult Ss  " + mult);
		Q = Q * mult;
		Q = Q + pair.getVal1();
//		logger.info("WWWWWWWWWWWWW    "+Q);
		p = new Pair<Double, Double>(Q, mult);
//		logger.info(node.getName() + "--out$$$--" + p);
		return p;
	}
}
