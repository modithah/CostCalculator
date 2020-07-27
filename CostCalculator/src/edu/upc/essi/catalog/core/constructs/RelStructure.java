package edu.upc.essi.catalog.core.constructs;

import java.util.HashMap;

import edu.upc.essi.catalog.enums.OperationTypeEnum;

public class RelStructure {
	/**
	 * @param rel
	 * @param op
	 */

	public RelStructure(Relationship rel, OperationTypeEnum op) {
		this.rel = rel;
		this.op = op;
	}

	public RelStructure(Relationship rel, OperationTypeEnum op, Atom from,Atom to) {
		this.rel = rel;
		this.op = op;
		this.from = from;
		this.to=to;
	}

	public Relationship rel;
	public OperationTypeEnum op;
	public Atom from;
	public Atom to;
	public HashMap<Atom, Double> mult;

	@Override
	public String toString() {
		return from ==null?" ":from.getName()+"->" + op + " " + to.getName();
	}

}