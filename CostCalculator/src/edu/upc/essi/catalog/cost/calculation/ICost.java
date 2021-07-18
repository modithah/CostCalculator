package edu.upc.essi.catalog.cost.calculation;

import org.hypergraphdb.HGHandle;

import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.GenericTriple;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import org.hypergraphdb.HyperGraph;

public interface ICost {
	public GenericTriple<Double, Double, Double> GetSize(HyperGraph graph,Element node, String path);

	public double GetMultiplier(HyperGraph graph, Hyperedge source, HGHandle child);

	public double GetSize(HyperGraph graph,Element node);
}
