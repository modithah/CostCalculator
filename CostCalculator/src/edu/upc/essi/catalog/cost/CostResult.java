package edu.upc.essi.catalog.cost;

import java.util.ArrayList;

import org.hypergraphdb.util.Pair;

import edu.upc.essi.catalog.core.constructs.Atom;

public class CostResult {

	private double StorageSize;
	private ArrayList<Pair<Double, ArrayList<Atom>>> workload = new ArrayList<>();
	private ArrayList<Double> queryCosts = new ArrayList<>();

	public CostResult() {
		// TODO Auto-generated constructor stub

	}

	public double getStorageSize() {
		return StorageSize;
	}

	public void setStorageSize(double storageSize) {
		StorageSize = storageSize;
	}

	public ArrayList<Pair<Double, ArrayList<Atom>>> getWorkload() {
		return workload;
	}

	public void setWorkload(ArrayList<Pair<Double, ArrayList<Atom>>> workload) {
		this.workload = workload;
	}

	public ArrayList<Double> getQueryCosts() {
		return queryCosts;
	}

	public void setQueryCosts(ArrayList<Double> queryCosts) {
		this.queryCosts = queryCosts;
	}

	public void AddtoQueryCost(double val) {
		this.queryCosts.add(val);
	}

	@Override
	public String toString() {
		return "CostResult [StorageSize=" + StorageSize + ", queryCosts=" + queryCosts + "]";
	}
	

}
