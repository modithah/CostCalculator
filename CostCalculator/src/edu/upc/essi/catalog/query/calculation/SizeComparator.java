package edu.upc.essi.catalog.query.calculation;

import java.io.Serializable;
import java.util.Comparator;

import edu.upc.essi.catalog.core.constructs.Hyperedge;

public class SizeComparator implements Comparator<Hyperedge>, Serializable {

	@Override
	public int compare(Hyperedge o1, Hyperedge o2) {
		// TODO Auto-generated method stub
		return (int) (o1.getSize()-o2.getSize());
	}

}
