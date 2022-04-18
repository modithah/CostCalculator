package edu.upc.essi.catalog.core.constructs;

import org.hypergraphdb.HGHandle;

import java.util.ArrayList;

public interface Element {
	 String getName();
	 ArrayList<HGHandle> getParents();
	 void addToIncoming (HGHandle parent);
	 void removeFromIncoming (HGHandle parent);
}
