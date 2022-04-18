package edu.upc.essi.catalog.cost.calculation;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;

import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.GenericTriple;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.ops.CostOperations;
import edu.upc.essi.catalog.ops.Graphoperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentCost2 implements ICost {

	public DocumentCost2() {
		// TODO Auto-generated constructor stub
	}
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

	public double GetSize(HyperGraph graph,Element node) {
		double size = 0;
		if (node instanceof Atom) {
			Atom atm = (Atom) node;
			size = atm.getSize();

		} else if (node instanceof Hyperedge) {
			switch (((Hyperedge) node).getType()) {
			case FirstLevel: // nultiply by the count
				size = 0;
				break;
			case Struct:
				size = node.getName().split("~")[0].isEmpty() ? 0 : node.getName().split("~")[0].length();
				break;
			case Set:
				size = node.getName().split("~")[0].length();
				break;
			case SecondLevel:
				size = 0;
				break;
			default:
				size = 0;
				break;
			}
		}
		return size;
	}

	public double GetMultiplier(HyperGraph graph, Hyperedge source, HGHandle child) {

		double multiplier = 1.0;
//		HyperGraph graph = Const.graph;

		if (source.getType() == HyperedgeTypeEnum.SecondLevel) {
			logger.info("NNNNNNN");
			logger.info(graph.get(source.getRoot()).toString());
			multiplier = ((Atom) graph.get(source.getRoot())).getCount();
		} else if (source.getType() == HyperedgeTypeEnum.Set) {
			Relationship relationship = source.getNestedRelationship(child);
//			logger.info("FFFFFFFFFFFFF");
					
//			logger.info(relationship);
			ArrayList<String> relorder = new ArrayList<>();
			relorder.add(((Atom) graph.get(relationship.getTargetAt(0))).getName());
			relorder.add(((Atom) graph.get(relationship.getTargetAt(1))).getName());
			Collections.sort(relorder);
			int index=relorder.indexOf(((Atom) graph.get(((Hyperedge) graph.get(child)).getRoot())).getName());
			multiplier = relationship.getMultiplicities()[index];
		}

		return multiplier;
	}

	@Override
	public GenericTriple<Double, Double, Double> GetSize(HyperGraph graph,Element node, String path) {
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

}
