package edu.upc.essi.catalog.ops;

import java.util.List;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.algorithms.HGBreadthFirstTraversal;
import org.hypergraphdb.util.Pair;

import com.github.andrewoma.dexx.collection.HashMap;

import org.hypergraphdb.HyperGraph;

import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.util.TargetSetALGenerator;

public final class Transformations {

	public static boolean union(HyperGraph graph, Hyperedge hyp1, Hyperedge hyp2) {

		// TODO condidions
		List<HGHandle> parents1 = graph.findAll(hg.contains(graph.getHandle(hyp1)));
		List<HGHandle> parents2 = graph.findAll(hg.contains(graph.getHandle(hyp2)));

		if (!((hyp1.getType() == hyp2.getType() && hyp2.getType() == HyperedgeTypeEnum.Set)
				|| (hyp1.getType() == hyp2.getType() && hyp2.getType() == HyperedgeTypeEnum.FirstLevel))) {
			System.out.println("Hyperedges must be sets");
			return false;
		}

		if (!(parents1.containsAll(parents2) && parents2.containsAll(parents1))) {
			System.out.println("they should have the same parent");
			return false;
		}

		// Add children to the first hyperedge
		List<HGHandle> children = hyp2.findAll();

		for (HGHandle child : children) {
			hyp1.add(child);
		}

		graph.update(hyp1);

		graph.remove(graph.getHandle(hyp2));

		return true;
	}

	public static boolean flatten(HyperGraph graph, Hyperedge hyp) {
		HGHandle hypHandle = graph.getHandle(hyp);
		HGHandle parentHandle = graph.findAll(hg.contains(hypHandle)).get(0);
		Hyperedge parent = graph.get(parentHandle);

		if (!(parent.getType() == HyperedgeTypeEnum.Struct || parent.getType() == HyperedgeTypeEnum.SecondLevel)) {
			System.out.println("parent must be a struct to flatten");
			return false;
		}

		// Add children to the first hyperedge
		List<HGHandle> children = hyp.findAll();

		for (HGHandle child : children) {
			parent.add(child);
		}

		parent.remove(hypHandle);
		graph.remove(hypHandle);
		graph.update(parent);
		return true;
	}

	public static void findRelPath(HyperGraph graph, Hyperedge hyp, Atom atm ) {
		HGHandle hypHandle = graph.getHandle(hyp);
		HGHandle atomHAndle = graph.getHandle(atm);
		HashMap<HGHandle, HGHandle> path = new HashMap<>();
		boolean breakable = false;

		HGBreadthFirstTraversal traversal = new HGBreadthFirstTraversal(hypHandle, new TargetSetALGenerator(graph));

		while (traversal.hasNext()) {
			Pair<HGHandle, HGHandle> current = traversal.next();
			Hyperedge parent = (Hyperedge) graph.get(current.getFirst());
			Object atom = graph.get(current.getSecond());

			path.put(current.getSecond(), current.getFirst());
			if (atom instanceof Atom && atom.equals(atm)) {
				System.out.println(atom);
				breakable = true;
			}
			// we found the atom no need to dig deeper
			if (breakable && atom instanceof Hyperedge) {
				break;
			}

//			    if(((Atom)atom).getName().equals("B_ID")) {
//			    	System.out.println("XXXXXXXXXX");
//			    	break;
//			    	}
			System.out.println("Visiting atom " + atom + " pointed to by " + parent);
		}
	}
}
