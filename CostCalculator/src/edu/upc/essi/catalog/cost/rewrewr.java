package edu.upc.essi.catalog.cost;

import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.hypergraphdb.HGHandle;

import edu.upc.essi.catalog.core.constructs.AdjacencyList;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Triple;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.query.prefixsuffix.DocumentPrefixSuffix;
import edu.upc.essi.catalog.query.prefixsuffix.IPrefixSuffix;
import edu.upc.essi.catalog.query.prefixsuffix.RelationalPrefixSuffix;
import org.hypergraphdb.HyperGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class rewrewr{

	IPrefixSuffix PrefixSuffix;
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
	public rewrewr() {
		// TODO Auto-generated constructor stub
	}

	public Triple GetPrefixSuffix(Element node, String path) {
		return PrefixSuffix.GetprefixSuffix(node, path);
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
			break;
		default:
			break;
		}

		for (Element element : l.getAjadacencyList(node)) {
			logger.info(CreateQueryFromMap(element, path, l));
		}
		// return CreateQueryFromMap(node, path, l) + "\n";
	}

	public String CreateQuery(Element node, String path) {
		String Q = "";

		Triple pair = GetPrefixSuffix(node, path);
		Q = Q + pair.getPrefix();

		if (node instanceof Hyperedge) {
			Iterator<HGHandle> x = ((Hyperedge) node).iterator();
			while (x.hasNext()) {
				HyperGraph graph= new HyperGraph();
				Q = Q + CreateQuery(Graphoperations.getElementbyHandle(graph,x.next()), pair.getPath());
			}
		}
		Q = Q + pair.getSuffix();
		Q = Q.replaceAll(",~", "");

		return Q;
	}

	public String CreateQueryFromMap(Element node, String path, AdjacencyList l) {

		// logger.info("node -> " + node + "path ->" + path + "l-->" + l);
		String Q = "";

		Triple pair = GetPrefixSuffix(node, path);
		Q = Q + pair.getPrefix();

		LinkedHashSet<Element> children = l.getAjadacencyList(node);

		if (children != null) {
			for (Element element : children) {
				Q = Q + CreateQueryFromMap(element, pair.getPath(), l);
			}
		}
		Q = Q + pair.getSuffix();
		Q = Q.replaceAll(",~", "");
		return Q;
	}
}
