package edu.upc.essi.catalog.ops;

import java.util.List;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;

public final class Graphoperations {

	private Graphoperations() {
		// TODO Auto-generated constructor stub
	}

	public static Hyperedge getDBHyperedgebyType(HyperedgeTypeEnum type) {
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION);
		List<Object> r = hg.getAll(graph, hg.and(hg.type(Hyperedge.class), hg.eq("type", type)));
		System.out.println(r.get(0).getClass());
		graph.close();
		return (Hyperedge) r.get(0);
	}
	
	public static Element getElementbyHandle(HGHandle handle) {
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION);
		Element el= graph.get(handle);
		return el;
	}

}
