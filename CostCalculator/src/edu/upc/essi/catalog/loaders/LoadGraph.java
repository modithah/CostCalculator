package edu.upc.essi.catalog.loaders;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.jsonldjava.utils.Obj;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import edu.upc.essi.catalog.IO.FileReaderWriter;
import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.CSVRow;
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
							atomHandles.put(id, Graphoperations.addAtomtoGraph(graph, atm));

							for (String otherstring : others) { // add other atom relationships
								Graphoperations.makeRelation(graph, atomHandles, relHandles, id, otherstring, 1);
							}

							others = new ArrayList<>();
						} else {
							atm.setSize(keyatom.getJSONObject(key).getInt(keyn));
							atoms.put(keyn, atm);
							atomHandles.put(keyn, Graphoperations.addAtomtoGraph(graph, atm));
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

	public static void LoadDesignFromCSV(List<CSVRow> rows) {
		// TODO Auto-generated method stub
		try {
//			FileUtils.cleanDirectory(new File(Const.HG_LOCATION_BOOK));
			HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//			HashMap<String, Atom> atoms = new HashMap<>();
//			ArrayList<Atom> atoms = new ArrayList<>();
			HashMap<String, HGHandle> atomHandles = new HashMap<>();
			Table<String, String, HGHandle> relHandles = HashBasedTable.create();
			HashMap<String, HGHandle> sets = new HashMap<>();
			HashMap<String, HGHandle> setHandles = new HashMap<>();
			HashMap<String, HGHandle> structHandles = new HashMap<>();
//			HashMap<String, HGHandle> setHandles= new HashMap<>();

			logger.info("Creating Atoms");

			Queue<Pair> setnames = new LinkedList<>();
			ArrayList<String> structnames = new ArrayList<>();
			ArrayList<String> done = new ArrayList<>();

			rows.stream().filter(x -> x.getType().equals("Set")).forEach(y -> {
				Pair p = Pair.of(y.getNode(), HyperedgeTypeEnum.Set);
				if (!setnames.contains(y.getNode())) {
					setnames.add(p);
				}
			});

			rows.stream().filter(x -> x.getType().equals("FirstLevel")).forEach(y -> {
				Pair p = Pair.of(y.getNode(), HyperedgeTypeEnum.FirstLevel);
				if (!setnames.contains(y.getNode())) {
					setnames.add(p);
				}
			});

			while (!setnames.isEmpty()) {

				Pair p = setnames.poll();
				String setName = (String) p.getLeft();
				HyperedgeTypeEnum type = (HyperedgeTypeEnum) p.getRight();
				String structName = type == HyperedgeTypeEnum.Struct ? setName : setName + "~struct";

				System.out.println(setName + type);

				// if the struct contains more sets not done dont continue
				if (rows.stream().anyMatch(x -> x.getParent().equals(structName) && (x.getType().equals("Set"))
						&& !done.contains(x.getNode()))) {
					setnames.add(Pair.of(setName, HyperedgeTypeEnum.Set));
					setnames.add(p);
				} else if (rows.stream().anyMatch(x -> x.getParent().equals(structName)
						&& (x.getType().equals("Struct")) && !done.contains(x.getNode()))) {

					rows.stream().filter(x -> x.getParent().equals(structName) && (x.getType().equals("Struct"))
							&& !done.contains(x.getNode())).forEach(y -> {
								setnames.add(Pair.of(y.getNode(), HyperedgeTypeEnum.Struct));
								System.out.println("addding   " + y.getNode());
								structnames.add(y.getNode());
							});

					setnames.add(p);

				} else {
					ArrayList<HGHandle> list = new ArrayList<>();
					String root = rows.stream()
							.filter(x -> x.getParent().equals(structName) && x.getNode().contains("*")).findFirst()
							.orElse(null).getNode().replace("*", "");
					System.out.println("ROOOT" + root);

					HGHandle keyHandle = atomHandles.get(root);
					if (keyHandle == null) {
						atomHandles.put(root, Graphoperations.getAtomByName(graph, root));
						keyHandle = atomHandles.get(root);
					}
					list.add(keyHandle);

					rows.stream().filter(x -> x.getParent().equals(structName) && !x.getNode().contains("*"))
							.forEach(el -> {

								if (el.getType().equals("Class") || el.getType().equals("Attribute")) {
									String attName = el.getNode();
									System.out.println(attName);
									HGHandle attributehandle = atomHandles.get(attName);
									if (attributehandle == null) {
										atomHandles.put(attName, Graphoperations.getAtomByName(graph, attName));
										attributehandle = atomHandles.get(attName);
									}
									list.add(attributehandle);

									HGHandle relHandle = relHandles.get(root, "has" + attName);
									if (relHandle == null) {

										relHandle = Graphoperations.getRelationshipByNameAtoms(graph, "has" + attName,
												atomHandles.get(root), attributehandle);
										System.out.println(root);
										System.out.println("has" + attName);
										System.out.println(relHandle);
										relHandles.put(root, "has" + attName, relHandle);
									}
									if (relHandle == null) {
										try {
											throw new Exception("Non Existant relationship");
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									} else {
										list.add(relHandle);
									}
								} else {
									if (el.getType().equals("Struct")) {
										list.add(structHandles.get(el.getNode()));
									}
									if (el.getType().equals("Set")) {
										list.add(setHandles.get(el.getNode()));
									}
								}

							});
					if (type == HyperedgeTypeEnum.Set) {

						HGHandle struct = Graphoperations.addHyperedgetoGraph(graph, "~" + UUID.randomUUID().toString(),
								HyperedgeTypeEnum.Struct, list.toArray(new HGHandle[list.size()]));
						structHandles.put(structName, struct);
						done.add(structName);
						System.out.println(graph.get(struct).toString());
						String[] names = setName.split("~");
						setHandles.put(setName, Graphoperations.addHyperedgetoGraph(graph, names[names.length - 1],
								HyperedgeTypeEnum.Set, struct));
						done.add(setName);
						System.out.println("added ss  " + setName);
					} else if (type == HyperedgeTypeEnum.Struct) {
						String[] names = setName.split("~");
						HGHandle struct = Graphoperations.addHyperedgetoGraph(graph, names[names.length - 1],
								HyperedgeTypeEnum.Struct, list.toArray(new HGHandle[list.size()]));
						structHandles.put(setName, struct);
						done.add(setName);
						System.out.println("Created struct XXXXXXXXXXXXX  " + setName);
						System.out.println(graph.get(struct).toString());

//						sets.put(setName, Graphoperations.addHyperedgetoGraph(graph, names[names.length - 1],
//								HyperedgeTypeEnum.FirstLevel, struct));
//						done.add(setName);
					} else {
						HGHandle struct = Graphoperations.addHyperedgetoGraph(graph, "~" + UUID.randomUUID().toString(),
								HyperedgeTypeEnum.SecondLevel, list.toArray(new HGHandle[list.size()]));
						structHandles.put(structName, struct);
						done.add(structName);
						System.out.println("Created struct");
						System.out.println(graph.get(struct).toString());
						String[] names = setName.split("~");
						sets.put(setName, Graphoperations.addHyperedgetoGraph(graph, names[names.length - 1],
								HyperedgeTypeEnum.FirstLevel, struct));
						done.add(setName);
						System.out.println("added xx  " + setName);
					}
				}

			}

			Graphoperations.addHyperedgetoGraph(graph, "MongoDB", HyperedgeTypeEnum.Database_Doc,
					sets.values().toArray(new HGHandle[sets.values().size()]));
//				d++;
//				design = "design" + d;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void LoadDesignFromJSON(String schema) {

		try {
			HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
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
								atomHandles.put(attributes.getString(i), Graphoperations.getAtomByName(graph, attName));
								attributehandle = atomHandles.get(attributes.getString(i));
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

					HGHandle struct = Graphoperations.addHyperedgetoGraph(graph, "~" + UUID.randomUUID().toString(),
							HyperedgeTypeEnum.SecondLevel, list.toArray(new HGHandle[list.size()]));
					System.out.println(graph.get(struct).toString());
					sets.put(design, entitykey, Graphoperations.addHyperedgetoGraph(graph, entitykey,
							HyperedgeTypeEnum.FirstLevel, struct));

				}
				for (HGHandle HNDL : sets.values()) {
					System.out.println(graph.get(HNDL).toString());
				}

				Graphoperations.addHyperedgetoGraph(graph, "MongoDB", HyperedgeTypeEnum.Database_Doc,
						sets.values().toArray(new HGHandle[sets.values().size()]));
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

	public static void SaveDesignToCSV(Hyperedge topLevel, String name) {
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
		List<CSVRow> list = new ArrayList<>();
		Queue<Hyperedge> queue = new LinkedList<>();
		HashMap<String, String> map = new HashMap<>();
		queue.add(topLevel);
		map.put(topLevel.getName(), topLevel.getName());
		int i = 0;

		while (!queue.isEmpty()) {
			i++;
			Hyperedge parent = queue.poll();
			Iterator<HGHandle> iter = parent.findAll().iterator();

			while (iter.hasNext()) {
				HGHandle hgHandle = (HGHandle) iter.next();
				Object obj = graph.get(hgHandle);
				CSVRow row = new CSVRow();
				int lastindex = parent.getName().lastIndexOf("~");

				String hypname = lastindex > 0 ? parent.getName().substring(0, lastindex) : parent.getName();// parent.getName().split("~")[0];
				row.setId(String.valueOf(i));
				row.setParent(map.get(parent.getName()));
				row.setPos("not_used");
				if (obj.getClass().equals(Atom.class)) {
					if (Graphoperations.isRootofHyperedge(graph, hgHandle, parent)) {
						row.setNode(((Atom) obj).getName() + "*");
					} else {
						row.setNode(((Atom) obj).getName());
					}
					row.setType(((Atom) obj).getType().toString());
					list.add(row);
				} else if (obj.getClass().equals(Relationship.class)) {

				} else {
					String original = ((Hyperedge) obj).getName();
					System.out.println("original    " + original);
					if (map.containsKey(original)) {
						row.setNode(map.get(original));
					} else {
						lastindex = original.lastIndexOf("~");
						System.out.println(lastindex);
						String childname = original;
						if (lastindex >= 0) {
							childname = original.substring(0, lastindex);
						}
						System.out.println("childname    " + childname);
						String actname = childname.isEmpty() ? row.getParent() + "~struct" : childname;
						if (map.values().contains(actname)) {
							row.setNode(actname + i);
							map.put(original, actname + i);
						} else {
							row.setNode(actname);
							map.put(original, actname);
						}
					}

					row.setType(((Hyperedge) obj).getType().toString());
					queue.add((Hyperedge) obj);

					list.add(row);
				}

			}

			FileReaderWriter.writeToCSV(list, name);

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
