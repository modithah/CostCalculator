package edu.upc.essi.catalog.core.constructs;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class AdjacencyList {
	private HashMap<Element, LinkedHashSet<Element>> map = new HashMap<Element, LinkedHashSet<Element>>();

	public AdjacencyList() {
		// TODO Auto-generated constructor stub

	}

	public void AddToSet(Element parent, Element child) {
		LinkedHashSet<Element> set = new LinkedHashSet<Element>();
		if (map.containsKey(parent)) {
			set = map.get(parent);
		}
		set.add(child);
		map.put(parent, set);
	}

	public HashMap<Element, LinkedHashSet<Element>> getMap() {
		return map;
	}

	public LinkedHashSet<Element> getAjadacencyList(Element e) {
		return map.get(e);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return map.toString();
	}

}
