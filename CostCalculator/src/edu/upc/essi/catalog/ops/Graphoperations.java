package edu.upc.essi.catalog.ops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGLink;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HGSearchResult;

import com.google.common.collect.Table;

import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSet;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.AdjacencyList;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.AtomTypeEnum;
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
//		graph.close();
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

//		System.out.println(graph);
		for (String atomName : atomNames) {

			HGHandle atom = hg.findOne(graph, hg.and(hg.type(Atom.class), hg.eq("name", atomName)));
			if (atom != null) {
//				System.out.println(atomName);
				HGSearchResult<Object> y = graph.find(hg.contains(atom));
				while (y.hasNext()) {
					HGHandle type = (HGHandle) y.next();
//					System.out.println(type);
					q.add(Pair.of(type, atom));
				}

//				IncidenceSet incidence = graph.getIncidenceSet(atom);
//				for (HGHandle hgHandle : incidence) {
//					q.add(Pair.of(hgHandle, atom));
//				}
			}
		}
//		System.out.println("xxxx");
		while (!q.isEmpty()) {
			Pair<HGHandle, HGHandle> tmp = q.poll();
			Object parent = graph.get(tmp.getLeft());
			Object child = graph.get(tmp.getRight());
			if (parent instanceof Hyperedge) {

//				System.out.println("addding"+ (Element) parent +"- >"+ (Element) child);
//				System.out.println(l.getMap().keySet());
				l.AddToSet((Element) parent, (Element) child);

				HGSearchResult<Object> y = graph.find(hg.contains(tmp.getLeft()));
//				System.out.println(graph.get(tmp.getLeft()) + "sssssssssss");
				while (y.hasNext()) {
					HGHandle type = (HGHandle) y.next();
//					System.out.println(graph.get(type).toString());
					q.add(Pair.of(type, tmp.getLeft()));
				}

//				for (HGHandle hgHandle : graph.getIncidenceSet(tmp.getLeft())) {
//					q.add(Pair.of(hgHandle, tmp.getLeft()));
//				}
			}
		}
//		graph.close();
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

	public static List<Hyperedge> getAllFirstLevels() {
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
		List<Hyperedge> hyperedges = graph
				.getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.FirstLevel)));
//		graph.close();
		return hyperedges;
	}

	public static List<Hyperedge> getAllDesigns() {
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
		List<Hyperedge> hyperedges = graph
				.getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.Design)));
//		graph.close();
		return hyperedges;
	}

	public static List<Hyperedge> GetFirstLevelsOfDesign(Hyperedge design) {
		List<Hyperedge> l = new ArrayList<>();
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//		HGHandle designHandle = graph.getHandle(design);
		design.findAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.FirstLevel))).iterator()
				.forEachRemaining(d -> {
					l.add(graph.get(d));
				});
		return l;
	}

	public static void addElementToHyperedge(HGHandle parent, HGHandle child) {
		Hyperedge parentel = (Hyperedge) getElementbyHandle(parent);
		parentel.add(child);
		Const.graph.update(parentel);
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
		return hg.findOne(graph, hg.and(hg.type(Relationship.class), hg.eq("IRI", name), hg.orderedLink(atom1, atom2)));
	}

	public static HGHandle getRelationshipByNameandSource(HyperGraph graph, String name, HGHandle source) {
		return hg.findOne(graph,
				hg.and(hg.type(Relationship.class), hg.eq("IRI", name), hg.orderedLink(source, hg.anyHandle())));
	}

//	public static HGHandle getRelationshipByName(HyperGraph graph, String name) {
//		return hg.findOne(graph, hg.and(hg.type(Relationship.class), hg.eq("IRI", name)));
//	}

	public static List<HGHandle> getHyperedgesContainingAtoms(HyperGraph graph, HGHandle... atoms) {
		return hg.findAll(graph, hg.and(hg.type(Hyperedge.class), hg.link(atoms)));
	}

	public static HGHandle getFirstLevelHyperedgesContainingAtom(HyperGraph graph, HGHandle atom) {

		HGHandle s = hg.findOne(graph, hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.SecondLevel),
				hg.link(new HGHandle[] { atom })));

		List<HGHandle> x = hg.findAll(graph, hg.and(hg.type(Hyperedge.class), hg.contains(atom)));
		System.out.println(x.size());
		return null;
	}

	public static void makeRelation(HyperGraph graph, HashMap<String, HGHandle> atomHandles,
			Table<String, String, HGHandle> relHandles, String id, String keyn, double multiplicity) throws Exception {
		relHandles.put(id, keyn,
				graph.add(new Relationship("has" + keyn,
						multiplicity > 1 ? CardinalityEnum.ONE_TO_MANY : CardinalityEnum.ONE_TO_ONE, multiplicity,
						atomHandles.get(id), atomHandles.get(keyn))));
		System.out.println(id + "->" + "has" + keyn);
	}

	public static void makeRelationWithName(HyperGraph graph, HashMap<String, HGHandle> atomHandles,
			Table<String, String, HGHandle> relHandles, String id, String keyn, double multiplicity, String name)
			throws Exception {
		relHandles.put(id, keyn,
				graph.add(new Relationship(name,
						multiplicity > 1 ? CardinalityEnum.ONE_TO_MANY : CardinalityEnum.ONE_TO_ONE, multiplicity,
						atomHandles.get(id), atomHandles.get(keyn))));
		System.out.println(id + "->" + "has" + keyn);
	}

	public static HGHandle addHyperedgetoGraph(HyperGraph graph, String name, HyperedgeTypeEnum type,
			HGHandle... targetSet) throws Exception {
		if (type == HyperedgeTypeEnum.Set) {
			throw new Exception("Plese use the set constructor");
		}
		HGHandle handle = graph.add(new Hyperedge());
		graph.replace(handle, new Hyperedge(graph, handle, name, type, targetSet));
		return handle;
	}

	public static HGHandle addSetHyperedgetoGraph(HyperGraph graph, String name, Relationship rel,
			HGHandle... targetSet) throws Exception {

		if (targetSet.length > 1) {
			throw new Exception("Set can have only one struct");
		}
		HGHandle handle = graph.add(new Hyperedge());
		Hyperedge hyp = new Hyperedge(graph, handle, name, HyperedgeTypeEnum.Set, targetSet);
		hyp.addToMap(targetSet[0], rel);
		graph.replace(handle, hyp);
		return handle;
	}

	public static ArrayList<HGHandle> getParentHyperesdeofAtom(HyperGraph graph, HGHandle atom) {
		ArrayList<HGHandle> parents = new ArrayList<>();
		HGSearchResult<Object> incidence = graph.find(hg.contains(atom));

		while (incidence.hasNext()) {
			HGHandle hgHandle = (HGHandle) incidence.next();
			if (isRootofHyperedge(graph, atom, hgHandle))
				parents.add(hgHandle);
		}

		return parents;
	}

	public static boolean isRootofHyperedge(HyperGraph graph, HGHandle atom, HGHandle hyperedge) {
		boolean b = false;
		HashMap<Atom, HGHandle> candidateAtoms = new HashMap<>();
		ArrayList<HGHandle> relationships = new ArrayList<>();
		Object hyp = graph.get(hyperedge);
		Atom target = graph.get(atom);
		if (hyp.getClass() != Hyperedge.class) {
			return false;
		}

		Iterator<HGHandle> seconditer = ((Hyperedge) hyp).findAll().iterator();
		while (seconditer.hasNext()) {
			HGHandle hgHandle2 = (HGHandle) seconditer.next();

			Object a = graph.get(hgHandle2);

			if (a.getClass().equals(Atom.class) && ((Atom) a).getType() == AtomTypeEnum.Class) {

				candidateAtoms.put((Atom) a, hgHandle2);
//				System.out.println("child " + ((Atom) a).getName());

			}

			if (a.getClass().equals(Relationship.class)) {
				relationships.add(hgHandle2);
			}
		}

		ArrayList<Atom> atoms = new ArrayList<>();
		candidateAtoms.keySet().iterator().forEachRemaining(atoms::add);

		if (atoms.size() == 1) {
			return true;
		} else {

			ArrayList<Boolean> evals = new ArrayList<>();

			for (int j = 0; j < atoms.size(); j++) {
				Atom atom2 = atoms.get(j);
				if (atom2 != target) {
					HGHandle rel = Graphoperations.getRelationshipByNameAtoms(graph, "has" + atom2.getName(),
							candidateAtoms.get(target), candidateAtoms.get(atom2));
					if (rel != null && relationships.contains(rel)) {
						evals.add(true);
					} else
						break;
				}
			}
			if (evals.size() == atoms.size() - 1) {
				if (!evals.contains(false)) {
					return true;
				}
			}

		}
		return b;
	}

	public static boolean isRootofHyperedge(HyperGraph graph, HGHandle atom, Hyperedge hyp) {
		boolean b = false;
		HashMap<Atom, HGHandle> candidateAtoms = new HashMap<>();
		ArrayList<HGHandle> relationships = new ArrayList<>();
		Atom target = graph.get(atom);
		if (hyp.getClass() != Hyperedge.class) {
			return false;
		}
		if (target.getType() != AtomTypeEnum.Class) {
			return false;
		}

		Iterator<HGHandle> seconditer = ((Hyperedge) hyp).findAll().iterator();
		while (seconditer.hasNext()) {
			HGHandle hgHandle2 = (HGHandle) seconditer.next();

			Object a = graph.get(hgHandle2);

			if (a.getClass().equals(Atom.class) && ((Atom) a).getType() == AtomTypeEnum.Class) {

				candidateAtoms.put((Atom) a, hgHandle2);
//				System.out.println("child " + ((Atom) a).getName());

			}

			if (a.getClass().equals(Relationship.class)) {
				relationships.add(hgHandle2);
			}
		}

		ArrayList<Atom> atoms = new ArrayList<>();
		candidateAtoms.keySet().iterator().forEachRemaining(atoms::add);

		if (atoms.size() == 1) {
			return true;
		} else {

			ArrayList<Boolean> evals = new ArrayList<>();

			for (int j = 0; j < atoms.size(); j++) {
				Atom atom2 = atoms.get(j);
				if (atom2 != target) {
					HGHandle rel = Graphoperations.getRelationshipByNameAtoms(graph, "has" + atom2.getName(),
							candidateAtoms.get(target), candidateAtoms.get(atom2));
					if (rel != null && relationships.contains(rel)) {
						evals.add(true);
					} else
						break;
				}
			}
			if (evals.size() == atoms.size() - 1) {
				if (!evals.contains(false)) {
					return true;
				}
			}

		}
		return b;
	}

//	public static HGHandle findRootAtom(HyperGraph graph, ArrayList<HGHandle> atomlist) {
//		boolean b = false;
//		HashMap<Atom, HGHandle> candidateAtoms = new HashMap<>();
//		ArrayList<HGHandle> relationships = new ArrayList<>();
//
//		for (HGHandle hgHandle2 : atomlist) {
//			Object a = graph.get(hgHandle2);
//
//			if (a.getClass().equals(Atom.class) && ((Atom) a).getType() == AtomTypeEnum.Class) {
//
//				candidateAtoms.put((Atom) a, hgHandle2);
//
//			}
//
//			if (a.getClass().equals(Relationship.class)) {
//				relationships.add(hgHandle2);
//			}
//		}
//
//		ArrayList<Atom> atoms = new ArrayList<>();
//		candidateAtoms.keySet().iterator().forEachRemaining(atoms::add);
//
//		if (atoms.size() == 1) {
//			b = true;
//		} else {
//
//			ArrayList<Boolean> evals = new ArrayList<>();
//
//			for (int i = 0; i < atoms.size(); i++) {
//
//				Object target = atoms.get(i);
//
//				for (int j = 0; j < atoms.size(); j++) {
//					Atom atom2 = atoms.get(j);
//					if (i != j) {
//						HGHandle rel = Graphoperations.getRelationshipByNameAtoms(graph, "has" + atom2.getName(),
//								candidateAtoms.get(target), candidateAtoms.get(atom2));
//						if (rel != null) {
//							evals.add(true);
//						} else
//							break;
//					}
//				}
//				if (evals.size() == atoms.size() - 1) {
//					if (!evals.contains(false)) {
//						return true;
//					}
//				}
//			}
//
//		}
//	}

	public static HGHandle addAtomtoGraph(HyperGraph graph, Atom atm) {
		// TODO Auto-generated method stub

		return graph.add(atm);
	}
}
