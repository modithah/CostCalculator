package edu.upc.essi.catalog.constants;

import org.hypergraphdb.HyperGraph;

public final class Const {

	private Const() {
	}
	public static final String HG_LOCATION = "C:\\hyper\\estocada";
	public static final String HG_LOCATION2 = "C:\\hyper\\tpcc";
	public static final String HG_LOCATION_BOOK = "C:\\hyper\\book";
	public static final HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
}
