package edu.upc.essi.catalog.core.constructs;

import java.util.ArrayList;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPlainLink;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.atom.HGSubgraph;

import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;

public class Hyperedge extends HGSubgraph2 implements Element {

	public Hyperedge() {
	}

	private String name;
	private HyperedgeTypeEnum type;
	private int size = 1;
	private double count = 1;

	public Hyperedge(HyperGraph graph, HGHandle handle, String name, HyperedgeTypeEnum type, HGHandle... targetSet) {
		super();
		this.name = name;
		this.type = type;
		setHyperGraph(graph);
		setAtomHandle(handle);
		for (int i = 0; i < targetSet.length; i++) {
			System.out.println(i);
			add(targetSet[i]);
		}
	}

	public Hyperedge(HGHandle... targets) {
		for (int i = 0; i < targets.length; i++) {
			add(targets[i]);
		}
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
		return name + "  " + type + "[" + count(hg.memberOf(thisHandle)) + "]" + getCount();
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

	private Object getTargetAt(int i) {
		// TODO Auto-generated method stub
		return outgoingSet.get(i);
	}

	private int getArity() {
		// TODO Auto-generated method stub
		return outgoingSet.size();
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

}
