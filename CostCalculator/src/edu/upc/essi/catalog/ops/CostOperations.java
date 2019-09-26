package edu.upc.essi.catalog.ops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSet;
import org.hypergraphdb.atom.HGAtomSet;
import org.hypergraphdb.HGQuery.hg;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.AtomTypeEnum;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;

public final class CostOperations {

	public static int CalculateCounts(HGHandle hyperedgehandle) {

		HyperGraph graph = Const.graph;
		Hyperedge hyperedge = graph.get(hyperedgehandle);
		int count = 0;
		Iterator<HGHandle> iter = hyperedge.iterator();
		HGHandle parentAtomHandle, childAtomHandle = null;
		String childName = "";
		HashMap<Atom, HGHandle> candidateAtoms = new HashMap<>();
		ArrayList<HGHandle> relationships = new ArrayList<>();

		// go throuhg all the second level/ structs (ideally it should be one)
		while (iter.hasNext()) {
			HGHandle hgHandle = (HGHandle) iter.next();
			Hyperedge secondlevel = graph.get(hgHandle); // got the struct/second level
//			System.out.println(secondlevel);
			Iterator<HGHandle> seconditer = secondlevel.iterator();
			while (seconditer.hasNext()) {
				HGHandle hgHandle2 = (HGHandle) seconditer.next();

				Object a = graph.get(hgHandle2);

				if (a.getClass().equals(Atom.class) && ((Atom) a).getType() == AtomTypeEnum.Class) {

					candidateAtoms.put((Atom) a, hgHandle2);
//					System.out.println("child " + ((Atom) a).getName());
				}

				if (a.getClass().equals(Relationship.class)) {
					relationships.add(hgHandle2);
				}
			}

			ArrayList<Atom> atoms = new ArrayList<>();
			candidateAtoms.keySet().iterator().forEachRemaining(atoms::add);

			if (atoms.size() == 1) {
				count = (atoms.get(0)).getCount(); // find the class atom
				childAtomHandle = candidateAtoms.get(atoms.get(0));
				childName = atoms.get(0).getName();
			} else {
				for (int i = 0; i < atoms.size(); i++) {
					ArrayList<Boolean> evals = new ArrayList<>();
					Atom atom1 = atoms.get(i);
					for (int j = 0; j < atoms.size(); j++) {
						if (i != j) {
							Atom atom2 = atoms.get(j);
							HGHandle rel = Graphoperations.getRelationshipByNameAtoms(graph, "has" + atom2.getName(),
									candidateAtoms.get(atom1), candidateAtoms.get(atom2));
							if (rel != null && relationships.contains(rel)) {
								evals.add(true);
							} else
								break;
						}
					}
					if (evals.size() == atoms.size() - 1) {
						if (!evals.contains(false)) {
							count = (atom1).getCount(); // find the class atom
							childAtomHandle = candidateAtoms.get(atom1);
							childName = atom1.getName();
						}
					}
				}
			}
		}

		switch (hyperedge.getType()) {

		// In first level it contains a second level that has a class that has the count
		case FirstLevel:

//			System.out.println("setting count  " + count);
//			hyperedge.setCount(count);

			break;

		// in set it has a struct that has a class atom with count
		case Set:
			// need to find the parent struct / second level
			IncidenceSet incidence = graph.getIncidenceSet(hyperedgehandle);
			for (HGHandle hgHandle : incidence) {
				Object hyper = graph.get(hgHandle);
				if (hyper.getClass().equals(Hyperedge.class)
						&& (((Hyperedge) hyper).getType() == HyperedgeTypeEnum.SecondLevel || // found the second level
																								// or
																								// struct parent
								((Hyperedge) hyper).getType() == HyperedgeTypeEnum.Struct)) {
					// check the class atom of the parent
					Hyperedge secondlevel = graph.get(hgHandle);
					Iterator<HGHandle> iteratorOfParent = secondlevel.iterator();
					while (iteratorOfParent.hasNext()) {
						HGHandle hgHandle2 = (HGHandle) iteratorOfParent.next();
						Object atomobject = graph.get(hgHandle2);
						// this is to find the atom
						if (atomobject.getClass().equals(Atom.class)
								&& ((Atom) atomobject).getType() == AtomTypeEnum.Class) {

							HGHandle rel = Graphoperations.getRelationshipByNameAtoms(graph, "has" + childName,
									hgHandle2, childAtomHandle);

							Relationship relationship = graph.get(rel);
//									hg.getOne(graph,
//									hg.and(hg.type(Relationship.class), hg.orderedLink(hgHandle2, childAtomHandle))); // find
							// the

							count = (int) relationship.getMultiplicity();
							hyperedge.setCount(count);
						}
					}
				}
			}

			break;
		default:
			break;
		}
		graph.update(hyperedge);
		return count;
	}

}
