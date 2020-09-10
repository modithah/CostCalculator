package edu.upc.essi.catalog.core.constructs.opsparams;

import java.util.ArrayList;

import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;

public class GroupParams {

	/**
	 * 
	 */
	public GroupParams() {
	}

	private Hyperedge hyp;
	private ArrayList<Relationship> rels = new ArrayList<>();
	private Element elm;

	public Hyperedge getHyp() {
		return hyp;
	}

	public void setHyp(Hyperedge hyp) {
		this.hyp = hyp;
	}

	public ArrayList<Relationship> getRels() {
		return rels;
	}

	public void setRels(ArrayList<Relationship> rels) {
		this.rels = rels;
	}

	public Element getElm() {
		return elm;
	}

	public void setElm(Element elm) {
		this.elm = elm;
	}

	@Override
	public String toString() {
		return "GroupParams [hyp=" + hyp + ", elm=" + elm + ", \n rels=" + rels + "]";
	}

}
