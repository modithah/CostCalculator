package edu.upc.essi.catalog.cost;

import java.util.Iterator;
import java.util.LinkedHashSet;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.util.Pair;

import edu.upc.essi.catalog.core.constructs.AdjacencyList;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.GenericTriple;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Triple;
import edu.upc.essi.catalog.cost.calculation.DocumentCost;
import edu.upc.essi.catalog.cost.calculation.ICost;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.query.prefixsuffix.DocumentPrefixSuffix;
import edu.upc.essi.catalog.query.prefixsuffix.IPrefixSuffix;
import edu.upc.essi.catalog.query.prefixsuffix.RelationalPrefixSuffix;

public class CostGenerator {

	IPrefixSuffix PrefixSuffix;
	ICost Cost;

	public CostGenerator() {
		Cost = new DocumentCost();
		// TODO Auto-generated constructor stub
	}

	public Triple GetPrefixSuffix(Element node, String path) {
		return PrefixSuffix.GetprefixSuffix(node, path);
	}

	public GenericTriple<Double, Double, Double> GetSize(Element node, String path) {
		return Cost.GetSize(node, path);
	}
	
	

	public void CreateCostFromMap(Element node, String path, AdjacencyList l, HyperedgeTypeEnum type) {

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
			System.out.println(CreateCostFromMap(element, path, l));
		}
		// return CreateQueryFromMap(node, path, l) + "\n";
	}

	public Pair<Double, Double> CreateCostFromMap(Element node, String path, AdjacencyList l) {
//		System.out.println("node -> " + node );
		Pair<Double, Double> p;
		double Q = 0;
		double mult = 1;

		GenericTriple<Double, Double, Double> pair = GetSize(node, path);

		mult = pair.getVal2();
		LinkedHashSet<Element> children = l.getAjadacencyList(node);

		if (children != null) {
			for (Element element : children) {
				Pair<Double, Double> x = CreateCostFromMap(element, "", l);
				Q = Q + x.getFirst();

			}
		}

//		System.out.println("mult Ss  " + mult);
		Q = Q * mult;
		Q = Q + pair.getVal1();
//		System.out.println("WWWWWWWWWWWWW    "+Q);
		p = new Pair<Double, Double>(Q, mult);
//		System.out.println(node.getName() + "--out$$$--" + p);
		return p;
	}
}