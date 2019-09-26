package edu.upc.essi.catalog.core.constructs;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPlainLink;

import edu.upc.essi.catalog.enums.CardinalityEnum;

public class Relationship extends HGPlainLink implements Element {

	public Relationship() {
	}

	private String IRI;
	private CardinalityEnum Cardinality = CardinalityEnum.ONE_TO_ONE;
	private double Multiplicity = 1;

	public Relationship(String iRI, HGHandle... targets) throws Exception {
		super(targets);
		assertBinary();
		this.IRI = iRI;
	}

	public Relationship(String iRI, CardinalityEnum cardinality, double multiplicity, HGHandle... targets)
			throws Exception {
		super(targets);
		assertBinary();
		this.IRI = iRI;
		this.Cardinality = cardinality;
		this.Multiplicity = multiplicity;
		System.out.println("MULTO" + multiplicity);
	}

	public Relationship(HGHandle... targets) {
		super(targets);
	}

	private void assertBinary() throws Exception {
		if (this.getArity() > 2)
			throw new Exception("Relationship is between two Atoms");
	}

	private void assertClassRelationship() throws Exception {
		if (this.getArity() > 2)
			throw new Exception("Cardinality and Selectivity is only between class Atoms");
	}

	public String getIRI() {
		return IRI;
	}

	public void setIRI(String iRI) {
		IRI = iRI;
	}

	public String toString() {
		// TODO Auto-generated method stub
		return IRI + "cardinality " + Cardinality.toString() + "selectivity " + Multiplicity + super.toString();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public CardinalityEnum getCardinality() {
		return Cardinality;
	}

	public void setCardinality(CardinalityEnum cardinality) {
		Cardinality = cardinality;
	}

	public double getMultiplicity() {
		return Multiplicity;
	}

	public void setMultiplicity(double multiplicity) {
		Multiplicity = multiplicity;
	}

}