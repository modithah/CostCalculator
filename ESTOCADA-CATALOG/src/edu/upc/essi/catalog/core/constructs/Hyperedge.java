package edu.upc.essi.catalog.core.constructs;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPlainLink;

import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;

public class Hyperedge extends HGPlainLink implements Element{

	private String name;
	private HyperedgeTypeEnum type;

	public Hyperedge(String name, HyperedgeTypeEnum type, HGHandle... targetSet) {
		super(targetSet);
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HyperedgeTypeEnum getType() {
		return type;
	}

	public void setType(HyperedgeTypeEnum type) {
		this.type = type;
	}

}
