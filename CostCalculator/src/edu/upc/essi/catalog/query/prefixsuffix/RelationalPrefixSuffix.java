package edu.upc.essi.catalog.query.prefixsuffix;

import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Triple;

public class RelationalPrefixSuffix implements IPrefixSuffix {

	public RelationalPrefixSuffix() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Triple GetprefixSuffix(Element node, String path) {
		// TODO Auto-generated method stub
		Triple t = new Triple();
		String prefix = "";
		String suffix = "";
		String p = "";

		if (node instanceof Atom) {
			prefix = node.getName();
			suffix = ",";
		} else if (node instanceof Hyperedge) {
			switch (((Hyperedge) node).getType()) {
			case FirstLevel:
				suffix = " FROM " + node.getName();
				break;
			case SecondLevel:
				prefix = "SELECT ";
				suffix = "~";
				break;
			default:
				break;
			}
		}

		t.setPrefix(prefix);
		t.setSuffix(suffix);
		t.setPath(p);
		return t;
	}

}
