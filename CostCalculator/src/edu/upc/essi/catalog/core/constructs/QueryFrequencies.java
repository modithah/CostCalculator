package edu.upc.essi.catalog.core.constructs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.hypergraphdb.util.Pair;

public class QueryFrequencies {

	private HashMap<Hyperedge, Map<Atom, Double>> globalFrequencies;
	private ArrayList<Pair<ArrayList<Atom>, HashMap<Hyperedge, Map<Atom, Double>>>> queryFrequencies;

	public QueryFrequencies() {
		this.globalFrequencies = new HashMap<>();
		this.queryFrequencies = new ArrayList<>();

	}

	public HashMap<Hyperedge, Map<Atom, Double>> getGlobalFrequencies() {
		return globalFrequencies;
	}

	public void setGlobalFrequencies(HashMap<Hyperedge, Map<Atom, Double>> globalFrequencies) {
		this.globalFrequencies = globalFrequencies;
	}

	public ArrayList<Pair<ArrayList<Atom>, HashMap<Hyperedge, Map<Atom, Double>>>> getQueryFrequencies() {
		return queryFrequencies;
	}

	public void setQueryFrequencies(
			ArrayList<Pair<ArrayList<Atom>, HashMap<Hyperedge, Map<Atom, Double>>>> queryFrequencies) {
		this.queryFrequencies = queryFrequencies;
	}

	public void addQueryFrequency(ArrayList<Atom> query, HashMap<Hyperedge, Map<Atom, Double>> freq) {
		Pair<ArrayList<Atom>, HashMap<Hyperedge, Map<Atom, Double>>> p = new Pair<>(query, freq);
		queryFrequencies.add(p);
	}
}
