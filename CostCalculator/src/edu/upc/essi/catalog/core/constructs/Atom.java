package edu.upc.essi.catalog.core.constructs;

import edu.upc.essi.catalog.enums.AtomTypeEnum;
import org.hypergraphdb.HGHandle;

import java.util.ArrayList;

public class Atom implements Element, Comparable<Atom> {

	private AtomTypeEnum type;
	private String name;
	
	/**
	 * 
	 *  For class
	 *  
	 */
	private int count;
	private int distinctVals;
	private ArrayList<HGHandle> incoming;
	/**
	 * 
	 *  For attributes
	 *  
	 */

	private String dataType;
	private int min;
	private int max;
	private int average;
	private int size;
	

	public Atom() {
		this.incoming=new ArrayList<>();
	}

	/**
	 * For attributes
	 * @param name
	 * @param dataType
	 * @param min
	 * @param max
	 * @param average
	 * @param size
	 */
	public Atom(String name, String dataType, int min, int max, int average, int size) {
		this.name = name;
		this.dataType = dataType;
		this.min = min;
		this.max = max;
		this.average = average;
		this.size = size;
		this.type = AtomTypeEnum.Attribute;
		this.incoming=new ArrayList<>();
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
		this.incoming=new ArrayList<>();
	}

	/**
	 * @param name
	 *            For class
	 */
	public Atom(String name) {
		this.name = name;
		this.type = AtomTypeEnum.Class;
		this.incoming=new ArrayList<>();
	}
	/**
	 * @param name
	 * @param count
	 *            For class
	 */
	public Atom(String name, int count) {
		this.name=name;
		this.count=count;
		this.type = AtomTypeEnum.Class;
		this.incoming=new ArrayList<>();
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public ArrayList<HGHandle> getParents() {
		return incoming;
	}

	public ArrayList<HGHandle> getIncoming() {
		return incoming;
	}

	@Override
	public void addToIncoming(HGHandle parent) {
		this.incoming.add(parent);
	}

	@Override
	public void removeFromIncoming(HGHandle parent) {
		this.incoming.remove(parent);
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getAverage() {
		return average;
	}

	public void setAverage(int average) {
		this.average = average;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDistinctVals() {
		return distinctVals;
	}

	public void setDistinctVals(int distinctVals) {
		this.distinctVals = distinctVals;
	}

	@Override
	public String toString() {
		return "Atom [" + name + "]";
	}
	
	 @Override
	    public boolean equals(Object obj) {
	        if(obj != null && obj instanceof Atom) {
	        	Atom s = (Atom)obj;
	            return name.equals(s.name);
	        }
	        return false;
	    }
	 
	 @Override
	    public int hashCode() {
	        return name.hashCode();
	    }

	public void setIncoming(ArrayList<HGHandle> incoming) {
		this.incoming = incoming;
	}

//	@Override
//	public int compareTo(Element arg0) {
//		// TODO Auto-generated method stub
//		return this.name.compareTo(arg0.getName());
//	}

	
	
}
