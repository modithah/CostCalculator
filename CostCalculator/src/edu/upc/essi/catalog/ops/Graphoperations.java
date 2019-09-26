package edu.upc.essi.catalog.ops;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGQuery.hg;

import com.google.common.collect.Table;

import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSet;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.AdjacencyList;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.CardinalityEnum;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;

public final class Graphoperations {

	static HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);

	static {
		graph = new HyperGraph(Const.HG_LOCATION_BOOK);
	}

	public Graphoperations() {
		// TODO Auto-generated constructor stub
		graph = new HyperGraph(Const.HG_LOCATION_BOOK);
	}

	/**
	 * Returns all hyperedges of a specific type
	 * 
	 * @param type
	 * @return
	 */
	public static Hyperedge getDBHyperedgebyType(HyperedgeTypeEnum type) {
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
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
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
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
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
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
	 * @param atomNames - queried atoms
	 * @return adjacency list of hyperedges corresponding to the queries
	 */
	public static AdjacencyList makeHashmap(String... atomNames) {
		AdjacencyList l = new AdjacencyList();
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
		PriorityQueue<Pair<HGHandle, HGHandle>> q = new PriorityQueue<Pair<HGHandle, HGHandle>>();

		System.out.println(graph);
		for (String atomName : atomNames) {
			System.out.println(atomName);
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
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
		List<Atom> atoms = graph.getAll(hg.type(Atom.class));
		graph.close();
		return atoms.stream().sorted().map(Atom::getName).collect(Collectors.toList());
	}

	public static HGHandle getHyperedgebyNameType(HyperGraph graph, String name, HyperedgeTypeEnum type) {
		return hg.findOne(graph, hg.and(hg.type(Hyperedge.class), hg.eq("name", name), hg.eq("type", type)));
	}

	public static HGHandle getHyperedgebyNameType(String name, HyperedgeTypeEnum type) {
		return hg.findOne(Const.graph, hg.and(hg.type(Hyperedge.class), hg.eq("name", name), hg.eq("type", type)));
	}

	public static HGHandle getAtomByName(HyperGraph graph, String name) {
		return hg.findOne(graph, hg.and(hg.type(Atom.class), hg.eq("name", name)));
	}

	public static HGHandle getRelationshipByNameAtoms(HyperGraph graph, String name, HGHandle atom1, HGHandle atom2) {

//		System.out.println(name);
		return hg.findOne(graph, hg.and(hg.type(Relationship.class), hg.eq("IRI", name), hg.orderedLink(atom1, atom2)));
	}

	public static void makeRelation(HyperGraph graph, HashMap<String, HGHandle> atomHandles,
			Table<String, String, HGHandle> relHandles, String id, String keyn, double multiplicity) throws Exception {
		relHandles.put(id, keyn,
				graph.add(new Relationship("has" + keyn,
						multiplicity > 1 ? CardinalityEnum.ONE_TO_MANY : CardinalityEnum.ONE_TO_ONE, multiplicity,
						atomHandles.get(id), atomHandles.get(keyn))));
		System.out.println(id + "->" + "has" + keyn);
	}

	public static HGHandle addtoGraph(HyperGraph graph, Object obj) {
		return graph.add(obj);
	}
}
