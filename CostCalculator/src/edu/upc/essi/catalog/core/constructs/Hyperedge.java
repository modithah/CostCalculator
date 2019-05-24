package edu.upc.essi.catalog.core.constructs;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPlainLink;

import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;

public class Hyperedge extends HGPlainLink implements Element {

	public Hyperedge() {
	}

	private String name;
	private HyperedgeTypeEnum type;

	public Hyperedge(String name, HyperedgeTypeEnum type, HGHandle... targetSet) {
		super(targetSet);
		this.name = name;
		this.type = type;
	}
	
	public Hyperedge(HGHandle...targets)
	{
	    super(targets);
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

	public String toString() {
		return name + "[" + getArity() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hyperedge other = (Hyperedge) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (getArity() != other.getArity())
			return false;
		for (int i = 0; i < getArity(); i++)
			if (!getTargetAt(i).equals(other.getTargetAt(i)))
				return false;
		return true;
	}

}
