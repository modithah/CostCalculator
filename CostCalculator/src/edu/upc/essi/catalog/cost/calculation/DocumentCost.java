package edu.upc.essi.catalog.cost.calculation;

import org.hypergraphdb.HGHandle;

import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.GenericTriple;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.ops.CostOperations;
import edu.upc.essi.catalog.ops.Graphoperations;
import org.hypergraphdb.HyperGraph;

public class DocumentCost implements ICost {

	

	@Override
	public GenericTriple<Double,Double,Double> GetSize(HyperGraph graph,Element node, String path) {
//		logger.info(node.getName());
		GenericTriple<Double, Double, Double> t = new GenericTriple<>();
		int size = 0;
		double multiply = 1.0;
		int noop = -1;

		if (node instanceof Atom) {

			Atom atm = (Atom) node;
			size = atm.getSize();

//			if (atm.getType() == AtomTypeEnum.Class) {
//				logger.info("count out");
//				multiply = atm.getCount();
//			}

		} else if (node instanceof Hyperedge) {
			switch (((Hyperedge) node).getType()) {
			case FirstLevel: // nultiply by the count
				size = 0;
				multiply = CostOperations.CalculateCounts(
						Graphoperations.getHyperedgebyNameType(graph,node.getName(), ((Hyperedge) node).getType()));
				noop = 0;
				break;
			case Struct:
				size = node.getName().split("~")[0].isEmpty() ? 0 : node.getName().split("~")[0].length();
				break;
			case Set:
				size = node.getName().split("~")[0].length();
				multiply = CostOperations.CalculateCounts(
						Graphoperations.getHyperedgebyNameType(graph,node.getName(), ((Hyperedge) node).getType()));
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



	@Override
	public double GetSize(HyperGraph graph,Element node) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public double GetMultiplier(HyperGraph graph, Hyperedge source, HGHandle child) {
		// TODO Auto-generated method stub
		return 0;
	}

}
