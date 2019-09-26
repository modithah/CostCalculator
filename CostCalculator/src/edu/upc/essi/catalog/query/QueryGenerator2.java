package edu.upc.essi.catalog.query;

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

public class QueryGenerator2 {

	IPrefixSuffix PrefixSuffix;
	ICost Cost;

	public QueryGenerator2() {
		Cost = new DocumentCost();
		// TODO Auto-generated constructor stub
	}

	public Triple GetPrefixSuffix(Element node, String path) {
		return PrefixSuffix.GetprefixSuffix(node, path);
	}

	public GenericTriple<Integer, Integer, Integer> GetSize(Element node, String path) {
		return Cost.GetSize(node, path);
	}
	// Relational
	// public Triple GetprefixSuffix(Element node, String path) {
	// Triple t = new Triple();
	// String prefix = "";
	// String suffix = "";
	// String p = "";
	//
	// if (node instanceof Atom) {
	// prefix = node.getName();
	// suffix = ",";
	// } else if (node instanceof Hyperedge) {
	// switch (((Hyperedge) node).getType()) {
	// case FirstLevel:
	// suffix = " FROM " + node.getName();
	// break;
	//
	// case SecondLevel:
	// prefix = "SELECT ";
	// suffix = "~";
	// break;
	//
	// default:
	// break;
	//
	// }
	// }
	//
	// t.setPrefix(prefix);
	// t.setSuffix(suffix);
	// t.setPath(p);
	// return t;
	// }

	public void CreateQueryFromMap(Element node, String path, AdjacencyList l, HyperedgeTypeEnum type) {

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
			System.out.println(CreateQueryFromMap(element, path, l));
		}
		// return CreateQueryFromMap(node, path, l) + "\n";
	}

//	public int CreateQueryFromMap(Element node, String path, AdjacencyList l) {
//		System.out.println(node.getName());
//		// System.out.println("node -> " + node + "path ->" + path + "l-->" + l);
//		String Q = "";
//		int size = 0;
//
//		size = size + GetSize(node, path);
//
//		LinkedHashSet<Element> children = l.getAjadacencyList(node);
//
//		if (children != null) {
//			for (Element element : children) {
//				size = size + CreateQueryFromMap(element, "", l);
//			}
//		}
//
//		return size;
//	}
//	
//	public int CreateQuery(Element node, String path) {
//		System.out.println(node.getName());
//		
//		int size = 0;
//
//		size = size + GetSize(node, path);
//
//		if (node instanceof Hyperedge) {
//			Iterator<HGHandle> x = ((Hyperedge) node).iterator();
//			while (x.hasNext()) {
//				size = size + CreateQuery(Graphoperations.getElementbyHandle(x.next()), "");
//			}
//		}
//		return size;
//	}

	public Pair<Integer, Integer> CreateQueryFromMap(Element node, String path, AdjacencyList l) {

//		 System.out.println("node -> " + node  + "l-->" + l);
//		System.out.println(node.getName() + "--in");
		Pair<Integer, Integer> p;
		int Q = 0;
		int mult = 1;

		GenericTriple<Integer, Integer, Integer> pair = GetSize(node, path);
//		Q = Q + pair.getVal1();
//		mult = pair.getVal2();
//		// p.
//		LinkedHashSet<Element> children = l.getAjadacencyList(node);
//
//		if (children != null) {
//			mult = 1;
//			for (Element element : children) {
//				Pair<Integer, Integer> x = CreateQueryFromMap(element, "", l);
//				Q = Q + x.getFirst();
//				if (x.getSecond() != 1) {
//					mult = x.getSecond();
//				}
//			}
//			if (pair.getVal3() != -1) {
//				Q = Q * mult;
//				mult = 1;
//			}
//
//		}

		Q = Q + pair.getVal1();
		mult = pair.getVal2();
		// p.
		LinkedHashSet<Element> children = l.getAjadacencyList(node);

		if (children != null) {
			for (Element element : children) {
				Pair<Integer, Integer> x = CreateQueryFromMap(element, "", l);
				Q = Q + x.getFirst();
			}
		}

//		System.out.println("mult Ss  " + mult);
		Q = Q * mult;
		p = new Pair<Integer, Integer>(Q, mult);
//		System.out.println(node.getName() + "--out$$$--" + p);
		return p;
	}
}
