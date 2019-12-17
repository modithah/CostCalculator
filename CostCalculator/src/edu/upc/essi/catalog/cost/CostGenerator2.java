package edu.upc.essi.catalog.cost;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.util.Pair;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.AdjacencyList;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.GenericTriple;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.core.constructs.Triple;
import edu.upc.essi.catalog.cost.calculation.DocumentCost;
import edu.upc.essi.catalog.cost.calculation.DocumentCost2;
import edu.upc.essi.catalog.cost.calculation.ICost;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.query.prefixsuffix.DocumentPrefixSuffix;
import edu.upc.essi.catalog.query.prefixsuffix.IPrefixSuffix;
import edu.upc.essi.catalog.query.prefixsuffix.RelationalPrefixSuffix;

public class CostGenerator2 {

	IPrefixSuffix PrefixSuffix;
	ICost Cost;

	public CostGenerator2() {
		Cost = new DocumentCost2();
		// TODO Auto-generated constructor stub
	}

	public Triple GetPrefixSuffix(Element node, String path) {
		return PrefixSuffix.GetprefixSuffix(node, path);
	}

	public double GetSize(Element node) {
		return Cost.GetSize(node);
	}

	public double GetMultiplier(Hyperedge source, HGHandle child) {
		return Cost.GetMultiplier(source, child);
	}

	public void CalculateSize(Hyperedge node, HyperedgeTypeEnum type) {

		switch (type) {
		case Database_Col:

			break;
		case Database_Rel:
			PrefixSuffix = new RelationalPrefixSuffix();
			break;
		case Database_Doc:
			PrefixSuffix = new DocumentPrefixSuffix();
			Cost = new DocumentCost2();
			break;
		default:
			break;
		}

		System.out.println(CalculateSize(node));

		// return CreateQueryFromMap(node, path, l) + "\n";
	}

	public Pair<Double, HashMap<Atom, Double>> CalculateSize(Element node) {
		System.out.println("node -> " + node);
		double size = GetSize(node);
//		System.out.println("size   " + size);
		HashMap<Atom, Double> map = new HashMap<>();
		Pair<Double, HashMap<Atom, Double>> p;

		if (node instanceof Atom) {
			map.put((Atom) node, 1.0);
		} else { // hyperedge

			HyperGraph graph = Const.graph;
			Iterator<HGHandle> iter = ((Hyperedge) node).iterator();

			while (iter.hasNext()) {

				HGHandle child = (HGHandle) iter.next();
				Element childObject = Graphoperations.getElementbyHandle(child);

				if (!(childObject instanceof Relationship)) {
					Pair<Double, HashMap<Atom, Double>> value = CalculateSize(childObject);
					double multiplier = GetMultiplier((Hyperedge) node, child);
					size = size + value.getFirst() * multiplier;
					for (Atom atom : value.getSecond().keySet()) {
						map.put(atom, value.getSecond().get(atom) * multiplier);
					}
				}

			}
		}

		p = new Pair<Double, HashMap<Atom, Double>>(size, map);
		return p;
	}
}
