package edu.upc.essi.catalog.core.constructs.opsparams;

import java.util.ArrayList;

import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;

public class EmbedParams {

	private Hyperedge grandParent;
	private Hyperedge parent;
	private Hyperedge child;
	ArrayList<Relationship> path = new ArrayList<>();
	private Hyperedge embeddingParent;

	/**
	 * @param parent
	 * @param child
	 * @param path
	 */
	public EmbedParams(Hyperedge grandParent, Hyperedge parent, Hyperedge child) {
		this.grandParent = grandParent;
		this.parent = parent;
		this.child = child;
	}

	public EmbedParams() {
		// TODO Auto-generated constructor stub

	}

	public Hyperedge getParent() {
		return parent;
	}

	public void setParent(Hyperedge parent) {
		this.parent = parent;
	}

	public Hyperedge getChild() {
		return child;
	}

	public void setChild(Hyperedge child) {
		this.child = child;
	}

	public ArrayList<Relationship> getPath() {
		return path;
	}

	public void setPath(ArrayList<Relationship> path) {
		this.path = path;
	}

	public Hyperedge getEmbeddingParent() {
		return embeddingParent;
	}

	public void setEmbeddingParent(Hyperedge embeddingParent) {
		this.embeddingParent = embeddingParent;
	}

	public Hyperedge getGrandParent() {
		return grandParent;
	}

	public void setGrandParent(Hyperedge grandParent) {
		this.grandParent = grandParent;
	}

}
