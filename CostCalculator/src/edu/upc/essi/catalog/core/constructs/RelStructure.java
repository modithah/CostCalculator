package edu.upc.essi.catalog.core.constructs;

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

	public Relationship rel;
	public OperationTypeEnum op;
}