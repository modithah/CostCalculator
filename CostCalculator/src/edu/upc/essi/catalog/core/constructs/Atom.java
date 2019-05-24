package edu.upc.essi.catalog.core.constructs;

import edu.upc.essi.catalog.enums.AtomTypeEnum;

public class Atom implements Element, Comparable<Atom> {

	private AtomTypeEnum type;
	private String name;
	private String dataType;

	public Atom() {
	}

	/**
	 * @param name
	 * @param dataType
	 *            For attributes
	 */
	public Atom(String name, String dataType) {
		this.name = name;
		this.dataType = dataType;
		this.type = AtomTypeEnum.Attribute;
	}

	/**
	 * @param name
	 *            For class
	 */
	public Atom(String name) {
		this.name = name;
		this.type = AtomTypeEnum.Class;

	}

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

	@Override
	public int compareTo(Atom arg0) {
		// TODO Auto-generated method stub
		return this.name.compareTo(arg0.name);
	}

}
