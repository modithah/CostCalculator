package edu.upc.essi.catalog.util;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.algorithms.HGALGenerator;
import org.hypergraphdb.algorithms.HGBreadthFirstTraversal;
import org.hypergraphdb.util.Ref;

public class HGBreadthFirstTraversalExt extends HGBreadthFirstTraversal {

	public HGBreadthFirstTraversalExt() {
		// TODO Auto-generated constructor stub
	}

	public HGBreadthFirstTraversalExt(HGHandle startAtom, HGALGenerator adjListGenerator) {
		super(startAtom, adjListGenerator);
		// TODO Auto-generated constructor stub
	}

	public HGBreadthFirstTraversalExt(Ref<HGHandle> startAtom, HGALGenerator adjListGenerator) {
		super(startAtom, adjListGenerator);
		// TODO Auto-generated constructor stub
	}

	public HGBreadthFirstTraversalExt(HGHandle startAtom, HGALGenerator adjListGenerator, int maxDistance) {
		super(startAtom, adjListGenerator, maxDistance);
		// TODO Auto-generated constructor stub
	}

	public HGBreadthFirstTraversalExt(Ref<HGHandle> startAtom, HGALGenerator adjListGenerator, int maxDistance) {
		super(startAtom, adjListGenerator, maxDistance);
		// TODO Auto-generated constructor stub
	}
	
	

}
