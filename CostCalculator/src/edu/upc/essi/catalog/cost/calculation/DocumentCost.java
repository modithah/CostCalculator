package edu.upc.essi.catalog.cost.calculation;

import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.GenericTriple;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Triple;
import edu.upc.essi.catalog.enums.AtomTypeEnum;
import edu.upc.essi.catalog.ops.CostOperations;
import edu.upc.essi.catalog.ops.Graphoperations;

public class DocumentCost implements ICost {

	public DocumentCost() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public GenericTriple<Integer, Integer, Integer> GetSize(Element node, String path) {
//		System.out.println(node.getName());
		GenericTriple<Integer, Integer, Integer> t = new GenericTriple<>();
		int size = 0;
		int multiply = 1;
		int noop = -1;

		if (node instanceof Atom) {

			Atom atm = (Atom) node;
			size = atm.getSize();

//			if (atm.getType() == AtomTypeEnum.Class) {
//				System.out.println("count out");
//				multiply = atm.getCount();
//			}

		} else if (node instanceof Hyperedge) {
			switch (((Hyperedge) node).getType()) {
			case FirstLevel: // nultiply by the count
				size = 0;
				multiply = CostOperations.CalculateCounts(
						Graphoperations.getHyperedgebyNameType(node.getName(), ((Hyperedge) node).getType()));
				noop = 0;
				break;
			case Struct:
				size = node.getName().isEmpty() ? 0 : node.getName().length();
				break;
			case Set:
				size = node.getName().length();
				multiply = CostOperations.CalculateCounts(
						Graphoperations.getHyperedgebyNameType(node.getName(), ((Hyperedge) node).getType()));
				noop = 0;
				break;
			case SecondLevel:
				size = 0;
				break;
			default:
				size = 0;
				break;
			}
		}
		t.setVal1(size);
		t.setVal2(multiply);
		t.setVal3(noop);
		return t;
	}

}
