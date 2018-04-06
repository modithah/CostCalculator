package edu.upc.essi.catalog.core.constructs;

import edu.upc.essi.catalog.enums.AtomTypeEnum;

public class Atom implements Element {

	public Atom() {
		// TODO Auto-generated constructor stub
	}

	private AtomTypeEnum type;
	private String name;
	private String dataType;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public AtomTypeEnum getType() {
		return type;
	}

	public void setType(AtomTypeEnum type) {
		this.type = type;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setName(String name) {
		this.name = name;
	}

}
