package edu.upc.essi.catalog.cost.calculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSet;
import org.hypergraphdb.query.HGQueryCondition;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HGSearchResult;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.GenericTriple;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.core.constructs.Triple;
import edu.upc.essi.catalog.enums.AtomTypeEnum;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.ops.CostOperations;
import edu.upc.essi.catalog.ops.Graphoperations;

public class DocumentCost2 implements ICost {

	public DocumentCost2() {
		// TODO Auto-generated constructor stub
	}

	public double GetSize(Element node) {
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

	public double GetMultiplier(Hyperedge source, HGHandle child) {

		double multiplier = 1.0;
		HyperGraph graph = Const.graph;

		if (source.getType() == HyperedgeTypeEnum.SecondLevel) {
			System.out.println(graph.get(source.getRoot()).toString());
			multiplier = ((Atom) graph.get(source.getRoot())).getCount();
		} else if (source.getType() == HyperedgeTypeEnum.Set) {
			Relationship relationship = source.getNestedRelationship(child);
			System.out.println("FFFFFFFFFFFFF");
					
			System.out.println(relationship);
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
	public GenericTriple<Double, Double, Double> GetSize(Element node, String path) {
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
