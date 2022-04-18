package edu.upc.essi.catalog.constants;

import org.hypergraphdb.HyperGraph;

public final class Const {

    public static final String WL_LOCATION = "C:\\Users\\Moditha\\Desktop\\EDBT 2021\\rubis\\workload-booksample1.json";//"data/schemas/rubis/difsizes/workload.json";//"data/schemas/workload-booksample1.json";//
	public static final String SCHEMA_LOCATION =  "data/schemas/rubis/difsizes/30.json"; //"data/schemas/booksample1.json";

    private Const() {
	}

	public static final String HG_LOCATION = "C:\\hyper\\estocada";
//	public static final String HG_LOCATION_Test = "C:\\hyper\\test\\1";
	public static final String HG_LOCATION2 = "C:\\hyper\\tpcc";
	public static final String HG_LOCATION_BASE = "C:\\hyper\\test\\";
	public static final String HG_LOCATION_BOOK = HG_LOCATION_BASE + "2";// "C:\\hyper\\book";
	public static final String DESIGN_LOCATION = "C:\\Users\\Moditha\\Documents\\PhD\\SVN\\Schemas\\demo\\";
	public static final HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
}
