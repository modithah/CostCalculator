package edu.upc.essi.catalog.query.prefixsuffix;

import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Triple;

public class DocumentPrefixSuffix implements IPrefixSuffix {

	public DocumentPrefixSuffix() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Triple GetprefixSuffix(Element node, String path) {
		Triple t = new Triple();
		String prefix = "";
		String suffix = "";
		String p = "";

		if (node instanceof Atom) {

			if (path.isEmpty()) {
				prefix = path + node.getName() + ":1";
			} else {
				prefix = "\"" + path + node.getName() + "\":1";
			}
			suffix = ",";

		} else if (node instanceof Hyperedge) {
			switch (((Hyperedge) node).getType()) {
			case FirstLevel:
				prefix = "db." + node.getName() + ".find({},{";
				suffix = " })";
				break;
			case Struct:
				prefix = "";
				suffix = "";
				p = node.getName().isEmpty() ? path : path + node.getName() + ".";
				break;
			case Set:
				prefix = "";
				suffix = "";
				p = path + node.getName() + ".";
				break;
			case SecondLevel:
				prefix = "";
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
