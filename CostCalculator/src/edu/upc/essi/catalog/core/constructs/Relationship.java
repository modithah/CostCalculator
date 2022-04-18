package edu.upc.essi.catalog.core.constructs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPlainLink;

import com.github.andrewoma.dexx.collection.HashMap;

import edu.upc.essi.catalog.enums.CardinalityEnum;
import edu.upc.essi.catalog.enums.MultiplicityEnum;

public class Relationship extends HGPlainLink implements Element {



	public Relationship() {
	}

	private String IRI;
	private CardinalityEnum Cardinality = CardinalityEnum.ONE_TO_ONE;
	private double Multiplicity = 1.0;
	private double[] multiplicities;
	private ArrayList<HGHandle> incoming;

	public Relationship(String iRI, HGHandle... targets) throws Exception {
		super(targets);
		assertBinary();
		this.IRI = iRI;
		this.incoming=new ArrayList<>();
	}

	public Relationship(String iRI, CardinalityEnum cardinality, double multiplicity, HGHandle... targets)
			throws Exception {
		super(targets);
		assertBinary();
		this.IRI = iRI;
		this.Cardinality = cardinality;
		this.Multiplicity = multiplicity;
		this.multiplicities= new double[2];
		Arrays.fill(this.multiplicities	, 1);
		this.incoming=new ArrayList<>();
//		logger.info("MULTO" + multiplicity);
	}

	public Relationship(String iRI, double[] multiplicity, HGHandle... targets) throws Exception {
		super(targets);
		assertBinary();
		this.IRI = iRI;
		this.Cardinality = Arrays.stream(multiplicity).anyMatch(m -> (m == 1)) ? Cardinality.ONE_TO_MANY
				: Cardinality.MANY_TO_MANY;
		this.incoming=new ArrayList<>();
//		for (int i = 0; i < targets.length; i++) {
//			logger.info(targets[i]+"   YYYYYYYYYYYY  "+ multiplicity[i]);
//			
//		}
		this.multiplicities=multiplicity;
		
//		logger.info(this.multiplicities[0]);
	}

	public Relationship(HGHandle... targets) {
		super(targets);
		this.incoming=new ArrayList<>();
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
		return IRI + "cardinality " + Cardinality.toString() + "selectivity " + Arrays.toString(this.multiplicities) + super.toString();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<HGHandle> getParents() {
		return incoming;
	}

	public ArrayList<HGHandle> getIncoming() {
		return incoming;
	}

	public void setIncoming(ArrayList<HGHandle> incoming) {
		this.incoming = incoming;
	}

	@Override
	public void addToIncoming(HGHandle parent) {
		this.incoming.add(parent);
	}

	@Override
	public void removeFromIncoming(HGHandle parent) {
		this.incoming.remove(parent);
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

	public double[] getMultiplicities() {
		return multiplicities;
	}

	public void setMultiplicities(double[] multiplicities) {
		this.multiplicities = multiplicities;
	}

	

}