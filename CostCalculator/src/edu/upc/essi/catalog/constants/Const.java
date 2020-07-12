package edu.upc.essi.catalog.constants;

import org.hypergraphdb.HyperGraph;

public final class Const {

	private Const() {
	}

	public static final String HG_LOCATION = "C:\\hyper\\estocada";
//	public static final String HG_LOCATION_Test = "C:\\hyper\\test\\1";
	public static final String HG_LOCATION2 = "C:\\hyper\\tpcc";
	public static final String HG_LOCATION_BOOK = "C:\\hyper\\test\\1";//"C:\\hyper\\book";
	public static final String DESIGN_LOCATION = "C:\\Users\\Moditha\\Documents\\PhD\\SVN\\Schemas\\demo\\";
	public static final HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
}
