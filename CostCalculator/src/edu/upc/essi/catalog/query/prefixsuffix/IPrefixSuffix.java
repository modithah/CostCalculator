package edu.upc.essi.catalog.query.prefixsuffix;

import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Triple;

public interface IPrefixSuffix {
	public Triple GetprefixSuffix(Element node, String path);
}
