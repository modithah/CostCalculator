package edu.upc.essi.catalog.samples;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.AtomTypeEnum;
import edu.upc.essi.catalog.enums.CardinalityEnum;
import edu.upc.essi.catalog.estocada.TPCCCreateGraph;
import edu.upc.essi.catalog.loaders.LoadGraph;

public class BookSample {

//	static final Logger logger = Logger.getLogger(BookSample.class);
//	JSONObject jo;
	public static void main(String[] args) {
		
//		LoadGraph.LoadBaseFromJSON("C:\\Users\\Moditha\\Documents\\PhD\\SVN\\Schemas\\booksample.json");
//		LoadGraph.LoadDesignFromJSON("C:\\Users\\Moditha\\Documents\\PhD\\SVN\\Schemas\\booksample_design.json");
		// TODO Auto-generated method stub
//		try {
//			FileUtils.cleanDirectory(new File(Const.HG_LOCATION_BOOK));
//			HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//			HashMap<String, Atom> atoms = new HashMap<>();
////			ArrayList<Atom> atoms = new ArrayList<>();
//			HashMap<String, HGHandle> atomHandles = new HashMap<>();
//			Table<String, String, HGHandle> relHandles = HashBasedTable.create();
//
//			logger.info("Creating Atoms");
//			JSONObject jo = new JSONObject(
//					new String(Files.readAllBytes(Paths.get("C:\\Users\\Moditha\\Documents\\PhD\\SVN\\Schemas\\booksample.json")), StandardCharsets.UTF_8));
//			JSONArray atomstrings = jo.getJSONArray("atoms");
//
//			for (int i = 0; i < atomstrings.length(); i++) {
//				JSONObject keyatom = atomstrings.getJSONObject(i);
//				Iterator<String> keys = keyatom.keys();
//
//				while (keys.hasNext()) {
//					String key = keys.next(); // name of the class atom (only one)
//					System.out.println(key + "-----");
//					System.out.println(keyatom.getJSONObject(key).names());
//					Iterator<String> keys2 = keyatom.getJSONObject(key).keys();
//					String id = "";
//					ArrayList<String> others = new ArrayList<>();
//
//					while (keys2.hasNext()) { // all other atoms
//						String keyn = keys2.next();
//
//						Atom atm = new Atom(keyn);
//
//						atm.setType(AtomTypeEnum.Attribute);
//
//						if (keyn.contains("*")) {
//							id = keyn.substring(1);
//							atm.setName(id);
//							atm.setType(AtomTypeEnum.Class);
//							atm.setSize(keyatom.getJSONObject(key).getJSONObject(keyn).getInt("size"));
//							atm.setCount(keyatom.getJSONObject(key).getJSONObject(keyn).getInt("count"));
//							atoms.put(id, atm);
//							atomHandles.put(id, graph.add(atm));
//
//							for (String otherstring : others) { // add other atom relationships
//								makeRelation(graph, atomHandles, relHandles, id, otherstring, 1);
//							}
//
//							others = new ArrayList<>();
//						} else {
//							atm.setSize(keyatom.getJSONObject(key).getInt(keyn));
//							atoms.put(keyn, atm);
//							atomHandles.put(keyn, graph.add(atm));
//							if (id.equals("")) {
//								others.add(keyn);
//							} else {
//								makeRelation(graph, atomHandles, relHandles, id, keyn, 1);
//							}
//
//						}
//
//					}
//				}
//
//			}
//
//			JSONArray relStrings = jo.getJSONArray("relationships");
//
//			for (int i = 0; i < relStrings.length(); i++) {
//				JSONObject keyatom = relStrings.getJSONObject(i);
//				Iterator<String> keys = keyatom.keys();
//
//				while (keys.hasNext()) {
//					String from = keys.next(); // name of the main atom (only one)
//					System.out.println(from + "-----");
//					System.out.println(keyatom.getJSONObject(from).names());
//					Iterator<String> keys2 = keyatom.getJSONObject(from).keys();
//
//					while (keys2.hasNext()) { // all other atoms
//						String to = keys2.next();
//
//						System.out.println(from + " ->" + to + "  " + keyatom.getJSONObject(from).getDouble(to));
//						makeRelation(graph, atomHandles, relHandles, from, to, keyatom.getJSONObject(from).getDouble(to));
//
//					}
//				}
//
//			}
//
//			System.out.println(relHandles);
//			graph.close();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

//	private static void makeRelation(HyperGraph graph, HashMap<String, HGHandle> atomHandles,
//			Table<String, String, HGHandle> relHandles, String id, String keyn, double multiplicity) throws Exception {
//		relHandles.put(id, keyn,
//				graph.add(new Relationship("has" + keyn,
//						multiplicity > 1 ? CardinalityEnum.ONE_TO_MANY : CardinalityEnum.ONE_TO_ONE, multiplicity,
//						atomHandles.get(id), atomHandles.get(keyn))));
//	}
//
//	private static HGHandle getRelationHandle(Atom a1, Atom a2, Table t) {
//		HGHandle handle = null;
//		Object o = t.get(a1, a2);
//		if (o.equals(null))
//			handle = (HGHandle) t.get(a2, a1);
//		else
//			handle = (HGHandle) t.get(a1, a2);
//		return handle;
//	}

}
