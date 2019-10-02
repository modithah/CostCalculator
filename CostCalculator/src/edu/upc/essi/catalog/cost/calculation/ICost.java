package edu.upc.essi.catalog.cost.calculation;

import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.GenericTriple;
import edu.upc.essi.catalog.core.constructs.Triple;

public interface ICost {
	public GenericTriple<Double, Double, Double> GetSize(Element node, String path);
}
