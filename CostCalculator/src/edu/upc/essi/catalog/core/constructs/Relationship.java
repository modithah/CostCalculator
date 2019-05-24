package edu.upc.essi.catalog.core.constructs;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPlainLink;

public class Relationship extends HGPlainLink implements Element{

	public Relationship() {
	}

	private String IRI;

	public Relationship(String iRI, HGHandle... targets) throws Exception {
		super(targets);
		assertBinary();
		this.IRI = iRI;
	}

	public Relationship(HGHandle... targets) {
		super(targets);
	}

	private void assertBinary() throws Exception {
		if (this.getArity() > 2)
			throw new Exception("Relationship is between two Atoms");
	}

	public String getIRI() {
		return IRI;
	}

	public void setIRI(String iRI) {
		IRI = iRI;
	}

	public String toString() {
		// TODO Auto-generated method stub
		return IRI + super.toString();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
