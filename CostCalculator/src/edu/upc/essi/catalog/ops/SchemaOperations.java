package edu.upc.essi.catalog.ops;

import java.util.ArrayList;
import java.util.UUID;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;

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
		try {
			if (rel.getCardinality().equals(CardinalityEnum.ONE_TO_ONE)) {
				HGHandle struct;

				struct = Graphoperations.addHyperedgetoGraph(graph, child.getName(), HyperedgeTypeEnum.Struct,
						otherAtom);

				ArrayList<HGHandle> toAdd = new ArrayList<>();
				toAdd.add(struct);
				makeNewtargets(parent, toAdd);
				graph.update(parent);

			} else if (rel.getCardinality().equals(CardinalityEnum.ONE_TO_MANY)) {
				HGHandle struct = Graphoperations.addHyperedgetoGraph(graph, "~" + UUID.randomUUID().toString(),
						HyperedgeTypeEnum.Struct, otherAtom);
				HGHandle set = Graphoperations.addSetHyperedgetoGraph(graph,
						child.getName() + "s~" + UUID.randomUUID().toString(), rel, struct);
				ArrayList<HGHandle> toAdd = new ArrayList<>();
				toAdd.add(set);
				makeNewtargets(parent, toAdd);
				graph.update(parent);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void embed(HyperGraph graph, HGHandle rootAtom, HGHandle otherAtom, HGHandle relationship,
			HGHandle parentOfRoot) {
		try {
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
//			logger.info(graph.get(firstlevel).toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
