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
	public GenericTriple<Double,Double,Double> GetSize(Element node, String path) {
//		System.out.println(node.getName());
		GenericTriple<Double, Double, Double> t = new GenericTriple<>();
		int size = 0;
		double multiply = 1.0;
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
				size = node.getName().split("~")[0].isEmpty() ? 0 : node.getName().split("~")[0].length();
				break;
			case Set:
				size = node.getName().split("~")[0].length();
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
		t.setVal1((double) size);
		t.setVal2(multiply);
		t.setVal3((double) noop);
		return t;
	}

}
