package edu.upc.essi.catalog.ops;

import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSet;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.AdjacencyList;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;

public final class Graphoperations {

	private Graphoperations() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Returns all hyperedges of a specific type
	 * 
	 * @param type
	 * @return
	 */
	public static Hyperedge getDBHyperedgebyType(HyperedgeTypeEnum type) {
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION);
		List<Object> r = hg.getAll(graph, hg.and(hg.type(Hyperedge.class), hg.eq("type", type)));
		graph.close();
		return (Hyperedge) r.get(0);
	}

	/**
	 * Returns the element by a hgHandle
	 * 
	 * @param handle
	 * @return
	 */
	public static Element getElementbyHandle(HGHandle handle) {
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION);
		Element el = graph.get(handle);
		graph.close();
		return el;
	}

	/**
	 * Legacy code for a single atom
	 * 
	 * @param atomName
	 * @return
	 */
	public static AdjacencyList makeHashmap(String atomName) {
		AdjacencyList l = new AdjacencyList();
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION);
		HGHandle atom = hg.findOne(graph, hg.and(hg.type(Atom.class), hg.eq("name", atomName)));

		IncidenceSet incidence = graph.getIncidenceSet(atom);

		PriorityQueue<Pair<HGHandle, HGHandle>> q = new PriorityQueue<Pair<HGHandle, HGHandle>>();

		for (HGHandle hgHandle : incidence) {
			q.add(Pair.of(hgHandle, atom));
		}

		while (!q.isEmpty()) {
			Pair<HGHandle, HGHandle> tmp = q.poll();
			Object parent = graph.get(tmp.getLeft());
			Object child = graph.get(tmp.getRight());
			if (parent instanceof Hyperedge) {
				l.AddToSet((Element) parent, (Element) child);
				for (HGHandle hgHandle : graph.getIncidenceSet(tmp.getLeft())) {
					q.add(Pair.of(hgHandle, tmp.getLeft()));
				}
			}
		}
		graph.close();
		return l;
	}

	/**
	 * Algorithm 1 in the paper
	 * 
	 * @param atomNames
	 *            - queried atoms
	 * @return adjacency list of hyperedges corresponding to the queries
	 */
	public static AdjacencyList makeHashmap(String... atomNames) {
		AdjacencyList l = new AdjacencyList();
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION);
		PriorityQueue<Pair<HGHandle, HGHandle>> q = new PriorityQueue<Pair<HGHandle, HGHandle>>();

		for (String atomName : atomNames) {
			HGHandle atom = hg.findOne(graph, hg.and(hg.type(Atom.class), hg.eq("name", atomName)));
			if (atom != null) {
				IncidenceSet incidence = graph.getIncidenceSet(atom);
				for (HGHandle hgHandle : incidence) {
					q.add(Pair.of(hgHandle, atom));
				}
			}
		}
		while (!q.isEmpty()) {
			Pair<HGHandle, HGHandle> tmp = q.poll();
			Object parent = graph.get(tmp.getLeft());
			Object child = graph.get(tmp.getRight());
			if (parent instanceof Hyperedge) {
				l.AddToSet((Element) parent, (Element) child);
				for (HGHandle hgHandle : graph.getIncidenceSet(tmp.getLeft())) {
					q.add(Pair.of(hgHandle, tmp.getLeft()));
				}
			}
		}
		graph.close();
		return l;
	}

	/**
	 * 
	 * @return list of all atom names in the catalog
	 */
	public static List<String> getAllAtoms() {
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION);
		List<Atom> atoms = graph.getAll(hg.type(Atom.class));
		graph.close();
		return atoms.stream().sorted().map(Atom::getName).collect(Collectors.toList());
	}
}
