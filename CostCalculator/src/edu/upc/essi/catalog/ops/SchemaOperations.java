package edu.upc.essi.catalog.ops;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPlainLink;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.HGQuery.hg;

import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.CardinalityEnum;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;

public class SchemaOperations {

	public static void makeReference(HyperGraph graph, HGHandle rootAtom, HGHandle otherAtom, HGHandle relationship,
			HGHandle parentOfRoot) {
		Atom root = graph.get(rootAtom);
		Atom child = graph.get(otherAtom);
		Relationship rel = graph.get(relationship);
		Hyperedge parent = graph.get(parentOfRoot);

		if (rel.getCardinality().equals(CardinalityEnum.ONE_TO_ONE)) {
			HGHandle struct = Graphoperations.addHyperedgetoGraph(graph, child.getName(), HyperedgeTypeEnum.Struct,
					otherAtom);
			ArrayList<HGHandle> toAdd = new ArrayList<>();
			toAdd.add(struct);
			makeNewtargets(parent, toAdd);
			graph.update(parent);

		} else if (rel.getCardinality().equals(CardinalityEnum.ONE_TO_MANY)) {
			HGHandle struct = Graphoperations.addHyperedgetoGraph(graph, "~" + UUID.randomUUID().toString(),
					HyperedgeTypeEnum.Struct, otherAtom);
			HGHandle set = Graphoperations.addHyperedgetoGraph(graph,
					child.getName() + "s~" + UUID.randomUUID().toString(), HyperedgeTypeEnum.Set, struct);
			ArrayList<HGHandle> toAdd = new ArrayList<>();
			toAdd.add(set);
			makeNewtargets(parent, toAdd);
			graph.update(parent);
		}
	}

	public static void embed(HyperGraph graph, HGHandle rootAtom, HGHandle otherAtom, HGHandle relationship,
			HGHandle parentOfRoot) {
		Atom root = graph.get(rootAtom);
		Atom child = graph.get(otherAtom);
		Relationship rel = graph.get(relationship);
		Hyperedge parent = graph.get(parentOfRoot);

		if (rel.getCardinality().equals(CardinalityEnum.ONE_TO_ONE)) {
			HGHandle struct = Graphoperations.addHyperedgetoGraph(graph, child.getName(), HyperedgeTypeEnum.Struct,
					otherAtom);
			ArrayList<HGHandle> toAdd = new ArrayList<>();
			toAdd.add(struct);
			makeNewtargets(parent, toAdd);
			graph.update(parent);

		} else if (rel.getCardinality().equals(CardinalityEnum.ONE_TO_MANY)) {
//			HGHandle struct = Graphoperations.addtoGraph(graph, new Hyperedge("", HyperedgeTypeEnum.Struct, otherAtom));
//			HGHandle set = Graphoperations.addtoGraph(graph, new Hyperedge(
//					child.getName() + "s~" + UUID.randomUUID().toString(), HyperedgeTypeEnum.Set, struct));
//			ArrayList<HGHandle> toAdd = new ArrayList<>();
//			toAdd.add(set);
//			ArrayList<HGHandle> newobjs = makeNewtargets(parent, toAdd); 
//			parent.setNewOutgoing(newobjs);
//			graph.update(parent);

			HGHandle firstlevel = Graphoperations.getFirstLevelHyperedgesContainingAtom(graph, otherAtom);
//			System.out.println(graph.get(firstlevel).toString());
		}
	}

	private static void makeNewtargets(Hyperedge original, ArrayList<HGHandle> toAdd, ArrayList<HGHandle> toDelete) {

		for (HGHandle hgHandle : toDelete) {
			original.remove(hgHandle);
		}

		for (HGHandle hgHandle : toAdd) {
			original.add(hgHandle);
		}
//
//		Iterator<HGHandle> iter = original.iterator();
//		ArrayList<HGHandle> targets = new ArrayList<>();
//
//		while (iter.hasNext()) {
//			HGHandle hgHandle = (HGHandle) iter.next();
//			if (!toDelete.contains(hgHandle)) {
//				targets.add(hgHandle);
//			}
//		}
//
//		targets.addAll(toAdd);
//
//		return targets;
	}

	private static void makeNewtargets(Hyperedge original, ArrayList<HGHandle> toAdd) {
		makeNewtargets(original, toAdd, new ArrayList<HGHandle>());
	}
}
