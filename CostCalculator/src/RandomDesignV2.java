import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;
import org.hypergraphdb.HGQuery.hg;

import com.github.andrewoma.dexx.collection.HashMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.ibm.icu.impl.StringUCharacterIterator;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.RelStructure;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.enums.OperationTypeEnum;
import edu.upc.essi.catalog.loaders.LoadGraph;
import edu.upc.essi.catalog.ops.Graphoperations;

public class RandomDesignV2 {

	public RandomDesignV2() {
		// TODO Auto-generated constructor stub
	}

	final double pSet = 0.5;
	final static double skip = 0;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File serverDir = new File(Const.HG_LOCATION_BOOK);
		FileUtils.cleanDirectory(serverDir);
//		LoadGraph.LoadBaseFromJSONFile("C:\\Users\\Moditha\\Documents\\PhD\\SVN\\Schemas\\demo\\booksample2.json");
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
		Atom dummyAtom = new Atom();
		Relationship dummyRel = new Relationship();
		RelStructure dummyRelStr = new RelStructure(dummyRel, null);
//		ArrayList<Atom> allAtoms = new ArrayList<>();
		ArrayList<Relationship> allRels = new ArrayList<>();
		ArrayList<Relationship> usedRels = new ArrayList<>();
		ArrayList<Relationship> IteratingRels = new ArrayList<>();
		ArrayList<Relationship> reification = new ArrayList<>();
		Table<Atom, Atom, RelStructure> structures = HashBasedTable.create();
		Random rand = new Random();

//		allAtoms.addAll(Graphoperations.getClassAtomList(graph));
		allRels.addAll(Graphoperations.getClassRelList(graph));

//		System.out.println("FFFFFFFFFFF" + allAtoms.get(0));
//		Graphoperations.getAtomClassRelList(graph,graph.getHandle(allAtoms.get(0))).forEach(r -> {
//			System.out.println(r);
//		});
//		System.out.println("Xxxxxxxx");
//		allRels.forEach(r -> {
//			System.out.println(r);
//		});

//		Graphoperations.getAttributeRelsofClass(graph, graph.getHandle(allAtoms.get(1))).forEach(r -> {
//			System.out.println(graph.get(r).toString());
//		});
//
//		Graphoperations.getAttributesClass(graph, graph.getHandle(allAtoms.get(1))).forEach(r -> {
//			System.out.println(graph.get(r).toString());
//		});

		// pick a random relationship first
		Relationship firstPick = allRels.remove(rand.nextInt(allRels.size()));

		boolean refer = rand.nextBoolean();
//		for (int i = 0; i < 10; i++) {

		int firstChoice = rand.nextInt(4);
		System.out.println(firstChoice);

		chooseOperation(graph, dummyAtom, dummyRelStr, reification, structures, firstPick, refer, firstChoice, allRels);

		System.out.println(allRels);
		while (!allRels.isEmpty()) {
			Relationship newPick = allRels.remove(rand.nextInt(allRels.size()));
			int newChoice = rand.nextInt(2);
			refer = rand.nextBoolean();
			int root = newChoice;

			chooseOperation(graph, dummyAtom, dummyRelStr, reification, structures, newPick, refer, newChoice, allRels);
			List<Relationship> edges = Graphoperations.getAtomClassRelList(graph, newPick.getTargetAt(newChoice));
			edges.remove(newPick);
			structures.values().forEach(v -> {
				edges.remove(v.rel);
			});

			while (!edges.isEmpty()) {
				newPick = edges.remove(rand.nextInt(edges.size()));
				allRels.remove(newPick);
				newChoice = rand.nextInt(2);
				refer = true;
//				rand.nextBoolean();
				root = newChoice;
				chooseOperation(graph, dummyAtom, dummyRelStr, reification, structures, newPick, refer, newChoice,
						allRels);
				edges.addAll(Graphoperations.getAtomClassRelList(graph, newPick.getTargetAt(newChoice)));
				edges.remove(newPick);

				structures.values().forEach(v -> {
					edges.remove(v.rel);
				});

				if (refer && rand.nextDouble() > skip) {
					System.out.println("SKIPPING");
					RelStructure struct = structures.get(graph.get(newPick.getTargetAt(root)),
							graph.get(newPick.getTargetAt(root == 1 ? 0 : 1)));
					struct.op = OperationTypeEnum.RefSkip;
					structures.put(graph.get(newPick.getTargetAt(root)),
							graph.get(newPick.getTargetAt(root == 1 ? 0 : 1)), struct);
//					allAtoms.remove(graph.get(newPick.getTargetAt(root == 1 ? 0 : 1)));
				}

				Map<Atom, RelStructure> rootAsChild = structures.column(graph.get(newPick.getTargetAt(root)));

				for (RelStructure relst : rootAsChild.values()) {
					if (relst.op == OperationTypeEnum.RefSkip) {
						allRels.add(relst.rel);
					}
				}

			}

		}

		System.out.println(structures);

		Queue<Atom> q = new LinkedList<Atom>(structures.rowKeySet());
		while (!q.isEmpty()) {
			Atom first = q.poll();
			System.out.println("choosing " + first);
			Map<Atom, RelStructure> row = structures.row(first);
			System.out.println(row);
			if (Collections.disjoint(row.keySet(), structures.rowKeySet())) {
				System.out.println("disjoint");
				ArrayList<HGHandle> nestedHandles = new ArrayList<>();
				for (Atom atom : row.keySet()) {
					if (!atom.equals(dummyAtom)) {

						HGHandle rootHandle = graph.getHandle(atom);
						List<HGHandle> handles = new ArrayList<>();
						handles.add(rootHandle);
						handles.addAll(Graphoperations.getAttributesClass(graph, rootHandle));
						handles.addAll(Graphoperations.getAttributeRelsofClass(graph, rootHandle));

						Object x = graph
								.findOne(hg.and(hg.type(Hyperedge.class), hg.eq("name", atom.getName() + "_Struct")));
						System.out.println("found" + x);
						try {
							nestedHandles.add(graph.getHandle(row.get(atom).rel));
							HGHandle newHandle = x == null
									? Graphoperations.addHyperedgetoGraph(graph, atom.getName() + "_Struct",
											HyperedgeTypeEnum.Struct, handles.toArray(new HGHandle[handles.size()]))
									: (HGHandle) x;
							if (row.get(atom).op == OperationTypeEnum.Refer) {
								nestedHandles.add(rootHandle);
							} else if (row.get(atom).op == OperationTypeEnum.RefSkip) {

							} else if (row.get(atom).op == OperationTypeEnum.Nest) {
								nestedHandles.add(newHandle);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

				Object x = graph.findOne(hg.and(hg.type(Hyperedge.class), hg.eq("name", first.getName() + "_Struct")));
				System.out.println("found2222   " + x);

				HGHandle rootHandle = graph.getHandle(first);
				List<HGHandle> handles = new ArrayList<>();
				handles.add(rootHandle);
				handles.addAll(Graphoperations.getAttributesClass(graph, rootHandle));
				handles.addAll(Graphoperations.getAttributeRelsofClass(graph, rootHandle));
				handles.addAll(nestedHandles);
				try {
					// maybe add the handle to be retrieved later
					Graphoperations.addHyperedgetoGraph(graph, first.getName() + "_Struct", HyperedgeTypeEnum.Struct,
							handles.toArray(new HGHandle[handles.size()]));
					ArrayList<Pair<Atom, Atom>> toremove = new ArrayList<>();
					for (Atom atom : row.keySet()) {
						toremove.add(new Pair<Atom, Atom>(first, atom));
					}

					for (Pair<Atom, Atom> pair : toremove) {
						structures.remove(pair.getFirst(), pair.getSecond());
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Set<Atom> intersection = new HashSet<>();
					intersection.addAll(row.keySet());
				boolean cando = true;
				intersection.retainAll(structures.rowKeySet());

				System.out.println(intersection);
				for (Atom atom : intersection) {
					if ((structures.get(first, atom).op == OperationTypeEnum.Both_Nest
							&& structures.get(atom, first).op == OperationTypeEnum.Both_Nest)
							|| (structures.get(first, atom).op == OperationTypeEnum.Both_Refer
									&& structures.get(atom, first).op == OperationTypeEnum.Both_Refer) || !q.contains(atom)

					) {
//						cando = true;
					} else {
						cando = false;
					}
				}
				System.out.println("cando" + cando);
				if (cando) {

					ArrayList<HGHandle> nestedHandles = new ArrayList<>();

					System.out.println(row.keySet());
					for (Atom atom : row.keySet()) {
						if (!atom.equals(dummyAtom)) {

							HGHandle rootHandle = graph.getHandle(atom);
							List<HGHandle> handles = new ArrayList<>();
							handles.add(rootHandle);
							handles.addAll(Graphoperations.getAttributesClass(graph, rootHandle));
							handles.addAll(Graphoperations.getAttributeRelsofClass(graph, rootHandle));
							Object x = graph
									.findOne(hg.and(hg.type(Hyperedge.class), hg.eq("name", atom.getName() + "_Struct")));
							try {

								nestedHandles.add(graph.getHandle(row.get(atom).rel));
								HGHandle newHandle = x == null
										? Graphoperations.addHyperedgetoGraph(graph, atom.getName() + "_Struct",
												HyperedgeTypeEnum.Struct, handles.toArray(new HGHandle[handles.size()]))
										: (HGHandle) x;
								if (row.get(atom).op == OperationTypeEnum.Refer) {
									nestedHandles.add(rootHandle);
								} else if (row.get(atom).op == OperationTypeEnum.RefSkip) {

								} else if (row.get(atom).op == OperationTypeEnum.Nest) {
									nestedHandles.add(newHandle);
								}
								if (row.get(atom).op == OperationTypeEnum.Both_Refer) {
									nestedHandles.add(rootHandle);
								} else if (row.get(atom).op == OperationTypeEnum.Both_Nest) {
									nestedHandles.add(graph.getHandle(row.get(atom).rel));
									newHandle =  x == null
											? Graphoperations.addHyperedgetoGraph(graph,
											atom.getName() + "_Struct", HyperedgeTypeEnum.Struct,
											handles.toArray(new HGHandle[handles.size()])) : (HGHandle) x;
									nestedHandles.add(newHandle);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}

					HGHandle rootHandle = graph.getHandle(first);
					List<HGHandle> handles = new ArrayList<>();
					handles.add(rootHandle);
					handles.addAll(Graphoperations.getAttributesClass(graph, rootHandle));
					handles.addAll(Graphoperations.getAttributeRelsofClass(graph, rootHandle));
					handles.addAll(nestedHandles);
					try {
						// maybe add the handle to be retrieved later
						Graphoperations.addHyperedgetoGraph(graph, first.getName() + "_Struct",
								HyperedgeTypeEnum.Struct, handles.toArray(new HGHandle[handles.size()]));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					q.add(first);
				}
			}

		}

		// make the reification

		for (Relationship rel : reification) {
			List<HGHandle> handles = new ArrayList<>();
			handles.add(rel.getTargetAt(0));
			handles.add(rel.getTargetAt(1));
			handles.add(graph.getHandle(rel));

			try {
				// maybe add the handle to be retrieved later
				Graphoperations.addHyperedgetoGraph(graph, rel.getIRI() + "_Reif", HyperedgeTypeEnum.Struct,
						handles.toArray(new HGHandle[handles.size()]));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		structures.clear();

		finalyzeGraph(graph);

	}

	private static void chooseOperation(HyperGraph graph, Atom dummyAtom, RelStructure dummyRelStr,
			ArrayList<Relationship> reification, Table<Atom, Atom, RelStructure> structures, Relationship rel,
			boolean refer, int opChoice, ArrayList<Relationship> allRels) {
		switch (opChoice) {
		case 0: // use first and put the next inside
			if (structures.contains(graph.get(rel.getTargetAt(0)), graph.get(rel.getTargetAt(1)))) {
				allRels.add(rel);
			} else {
				structures.put(graph.get(rel.getTargetAt(0)), graph.get(rel.getTargetAt(1)),
						new RelStructure(rel, refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest));
			}
			break;
		case 1: // use second and put the next inside
			if (structures.contains(graph.get(rel.getTargetAt(1)), graph.get(rel.getTargetAt(0)))) {
				allRels.add(rel);
			} else {
				structures.put(graph.get(rel.getTargetAt(1)), graph.get(rel.getTargetAt(0)),
						new RelStructure(rel, refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest));
			}
			break;

		case 2: // put refs on both ends
			structures.put(graph.get(rel.getTargetAt(0)), graph.get(rel.getTargetAt(1)),
					new RelStructure(rel, refer ? OperationTypeEnum.Both_Refer : OperationTypeEnum.Both_Nest));
			structures.put(graph.get(rel.getTargetAt(1)), graph.get(rel.getTargetAt(0)),
					new RelStructure(rel, refer ? OperationTypeEnum.Both_Refer : OperationTypeEnum.Both_Nest));
			break;

		case 3: // reification
			structures.put(graph.get(rel.getTargetAt(0)), dummyAtom, dummyRelStr);
			structures.put(graph.get(rel.getTargetAt(1)), dummyAtom, dummyRelStr);
			reification.add(rel);
			break;

		default:
			break;
		}
	}

	private static void finalyzeGraph(HyperGraph graph) {
		System.out.println("end");
		List<HGHandle> firstLevels = new ArrayList<>();
		List<Hyperedge> structhyps = graph
				.getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.Struct)));

		// wrap structs without sets around sets and make them top level

		for (Hyperedge hyperedge : structhyps) {
			HGHandle handle = graph.getHandle(hyperedge);
			HGSearchResult<Object> x = graph.find(hg.contains(handle));

			if (x.hasNext()) {
				System.out.println("not empty");
				System.out.println(graph.get((HGHandle) x.next()).toString());

			} else {
				System.out.println("empty");
				hyperedge.setType(HyperedgeTypeEnum.SecondLevel);
				graph.update(hyperedge);
				try {
					HGHandle set = Graphoperations.addHyperedgetoGraph(graph, hyperedge.getName(),
							HyperedgeTypeEnum.FirstLevel, handle);
					firstLevels.add(set);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		List<Hyperedge> sethyps = graph.getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.Set)));

		// make sets without parents second level
		for (Hyperedge hyperedge : sethyps) {
			HGHandle handle = graph.getHandle(hyperedge);
			HGSearchResult<Object> x = graph.find(hg.contains(handle));
			if (x.hasNext()) {
				System.out.println("not empty");
			} else {
				System.out.println("empty");
				hyperedge.findAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.Struct)))
						.forEach(h -> {
							Hyperedge y = graph.get(h);
							y.setType(HyperedgeTypeEnum.SecondLevel);
							graph.update(y);
						});
				hyperedge.setType(HyperedgeTypeEnum.FirstLevel);
				graph.update(hyperedge);
				firstLevels.add(handle);
			}
		}

		try {
			Graphoperations.addHyperedgetoGraph(graph, "design", HyperedgeTypeEnum.Database_Doc,
					firstLevels.toArray(new HGHandle[firstLevels.size()]));
//			Graphoperations.printDesign();


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
