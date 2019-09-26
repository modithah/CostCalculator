package edu.upc.essi.catalog.loaders;

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
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.AtomTypeEnum;
import edu.upc.essi.catalog.enums.CardinalityEnum;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.samples.BookSample;

public class LoadGraph {

	static final Logger logger = Logger.getLogger(BookSample.class);
	JSONObject jo;

	public static void LoadBaseFromJSON(String schema) {
		// TODO Auto-generated method stub
		try {
//			FileUtils.cleanDirectory(new File(Const.HG_LOCATION_BOOK));
			HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
			HashMap<String, Atom> atoms = new HashMap<>();
//			ArrayList<Atom> atoms = new ArrayList<>();
			HashMap<String, HGHandle> atomHandles = new HashMap<>();
			Table<String, String, HGHandle> relHandles = HashBasedTable.create();

			logger.info("Creating Atoms");
			JSONObject jo = new JSONObject(new String(Files.readAllBytes(Paths.get(schema)), StandardCharsets.UTF_8));
			JSONArray atomstrings = jo.getJSONArray("atoms");

			for (int i = 0; i < atomstrings.length(); i++) {
				JSONObject keyatom = atomstrings.getJSONObject(i);
				Iterator<String> keys = keyatom.keys();

				while (keys.hasNext()) {
					String key = keys.next(); // name of the class atom (only one)
					System.out.println(key + "-----");
					System.out.println(keyatom.getJSONObject(key).names());
					Iterator<String> keys2 = keyatom.getJSONObject(key).keys();
					String id = "";
					ArrayList<String> others = new ArrayList<>();

					while (keys2.hasNext()) { // all other atoms
						String keyn = keys2.next();

						Atom atm = new Atom(keyn);

						atm.setType(AtomTypeEnum.Attribute);

						if (keyn.contains("*")) {
							id = keyn.substring(1);
							atm.setName(id);
							atm.setType(AtomTypeEnum.Class);
							atm.setSize(keyatom.getJSONObject(key).getJSONObject(keyn).getInt("size"));
							atm.setCount(keyatom.getJSONObject(key).getJSONObject(keyn).getInt("count"));
							atoms.put(id, atm);
							atomHandles.put(id, Graphoperations.addtoGraph(graph, atm));

							for (String otherstring : others) { // add other atom relationships
								Graphoperations.makeRelation(graph, atomHandles, relHandles, id, otherstring, 1);
							}

							others = new ArrayList<>();
						} else {
							atm.setSize(keyatom.getJSONObject(key).getInt(keyn));
							atoms.put(keyn, atm);
							atomHandles.put(keyn, Graphoperations.addtoGraph(graph, atm));
							if (id.equals("")) {
								others.add(keyn);
							} else {
								Graphoperations.makeRelation(graph, atomHandles, relHandles, id, keyn, 1);
							}

						}

					}
				}

			}

			JSONArray relStrings = jo.getJSONArray("relationships");

			for (int i = 0; i < relStrings.length(); i++) {
				JSONObject keyatom = relStrings.getJSONObject(i);
				Iterator<String> keys = keyatom.keys();

				while (keys.hasNext()) {
					String from = keys.next(); // name of the main atom (only one)
					System.out.println(from + "-----");
					System.out.println(keyatom.getJSONObject(from).names());
					Iterator<String> keys2 = keyatom.getJSONObject(from).keys();

					while (keys2.hasNext()) { // all other atoms
						String to = keys2.next();

						System.out.println(from + " ->" + to + "  " + keyatom.getJSONObject(from).getDouble(to));
						Graphoperations.makeRelation(graph, atomHandles, relHandles, from, to,
								keyatom.getJSONObject(from).getDouble(to));

					}
				}

			}

			System.out.println(relHandles);
			graph.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void LoadDesignFromJSON(String schema) {
		// TODO Auto-generated method stub
		try {
//			FileUtils.cleanDirectory(new File(Const.HG_LOCATION_BOOK));
			HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//			HashMap<String, Atom> atoms = new HashMap<>();
//			ArrayList<Atom> atoms = new ArrayList<>();
			HashMap<String, HGHandle> atomHandles = new HashMap<>();
			Table<String, String, HGHandle> relHandles = HashBasedTable.create();
			Table<String, String, HGHandle> sets = HashBasedTable.create();

			logger.info("Creating Atoms");
			JSONObject jo = new JSONObject(new String(Files.readAllBytes(Paths.get(schema)), StandardCharsets.UTF_8));

			String design = "design1";
			int d = 1;

			while (jo.has(design)) {
				System.out.println(design);
				JSONObject dobj = jo.getJSONObject("design1");
				Iterator<String> entitykeys = dobj.keys();

				while (entitykeys.hasNext()) {
					String entitykey = entitykeys.next();
					JSONObject entity = dobj.getJSONObject(entitykey);
					String root = entity.getString("key");
					HGHandle keyHandle = atomHandles.get(root);
					if (keyHandle == null) {
						atomHandles.put(entity.getString("key"), Graphoperations.getAtomByName(graph, root));
						keyHandle = atomHandles.get(root);
					}
					ArrayList<HGHandle> list = new ArrayList<>();
					list.add(keyHandle);
					JSONArray attributes = entity.has("attributes") ? entity.getJSONArray("attributes") : null;
					JSONArray multiples = entity.has("multiples") ? entity.getJSONArray("multiples") : null;

					if (attributes.length() != 0) {
						for (int i = 0; i < attributes.length(); i++) {
							String attName = attributes.getString(i);
							System.out.println(attName);
							HGHandle attributehandle = atomHandles.get(attName);
							if (attributehandle == null) {
								atomHandles.put(entity.getString(attributes.getString(i)),
										Graphoperations.getAtomByName(graph, attName));
								attributehandle = atomHandles.get(entity.getString(attributes.getString(i)));
							}
							list.add(attributehandle);

							HGHandle relHandle = relHandles.get(root, "has" + attName);
							if (relHandle == null) {
								relHandle = Graphoperations.getRelationshipByNameAtoms(graph, "has" + attName,
										keyHandle, attributehandle);
								System.out.println(root);
								System.out.println("has" + attName);
								System.out.println(relHandle);
								relHandles.put(root, "has" + attName, relHandle);
							}
							if (relHandle == null) {
								throw new Exception("Non Existant relationship");
							} else {
								list.add(relHandle);
							}
						}
					}

					
					HGHandle struct = Graphoperations.addtoGraph(graph,
							new Hyperedge("", HyperedgeTypeEnum.SecondLevel, list.toArray(new HGHandle[list.size()])));
					System.out.println(graph.get(struct).toString());
					sets.put(design, entitykey, Graphoperations.addtoGraph(graph,
							new Hyperedge(entitykey, HyperedgeTypeEnum.FirstLevel, struct)));

				}
				for (HGHandle HNDL : sets.values()) {
					System.out.println(graph.get(HNDL).toString());
				}

				Graphoperations.addtoGraph(graph, new Hyperedge("MongoDB", HyperedgeTypeEnum.Database_Doc,
						sets.values().toArray(new HGHandle[sets.values().size()])));
				d++;
				design = "design" + d;
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static HGHandle getRelationHandle(Atom a1, Atom a2, Table t) {
		HGHandle handle = null;
		Object o = t.get(a1, a2);
		if (o.equals(null))
			handle = (HGHandle) t.get(a2, a1);
		return handle;
	}
}
