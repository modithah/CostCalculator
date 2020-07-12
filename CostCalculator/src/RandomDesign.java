import java.io.File;
import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.management.relation.Relation;

import org.apache.commons.io.FileUtils;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.algorithms.HGBreadthFirstTraversal;
import org.hypergraphdb.algorithms.SimpleALGenerator;
import org.hypergraphdb.query.HGQueryCondition;
import org.hypergraphdb.util.Pair;

import com.github.andrewoma.dexx.collection.HashMap;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.HGSubgraph2;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.loaders.LoadGraph;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.util.TargetSetALGenerator;

public class RandomDesign {

	public RandomDesign() {
		// TODO Auto-generated constructor stub

	}

	final double PinternalSet = 0.75;
//	static int atoms = 0;
//	static int structs = 0;

	public static void main(String[] args) throws IOException {
		File serverDir = new File(Const.HG_LOCATION_BOOK);
		FileUtils.cleanDirectory(serverDir);
		LoadGraph.LoadBaseFromJSONFile("C:\\Users\\Moditha\\Documents\\PhD\\SVN\\Schemas\\demo\\booksample.json");
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
		for (int i = 0; i < 100; i++) {
			String path = "C:\\hyper\\test\\designs\\" + i;

			Random rand = new Random();
			int atoms = 0;
			int structs = 0;

			ArrayList<Element> picks = new ArrayList<>();
			ArrayList<Element> nestedStructs = new ArrayList<>();
			ArrayList<Element> allAtoms = new ArrayList<>();
			ArrayList<Element> allRels = new ArrayList<>();

			// put all atoms to be picked in the beginning
			allAtoms.addAll(Graphoperations.getAtomList(graph));
			allRels.addAll(Graphoperations.getRelList(graph));
			picks.addAll(Graphoperations.getClassAtomList(graph));
			atoms = allAtoms.size();

//		System.out.println(setProb(atoms, structs));

			System.out.println(allAtoms.size());
	

			int loopcount = 0;
			ArrayList<Element> prevPicks = picks;
			while (!allAtoms.isEmpty() && !allRels.isEmpty()) {
				System.out.println("##################");
				System.out.println(allAtoms);
				System.out.println(allRels);
				System.out.println(picks);
				System.out.println("##################");

				Element start = picks.get(rand.nextInt(picks.size()));

				if (picks == prevPicks) {
					loopcount++;
				} else {
					loopcount = 0;
				}

				if (loopcount > 50) {
					System.out.println("couldnt make");
					break;
				}

				System.out.println("atoms  rels" + allAtoms + "  " + allRels);
				if (start instanceof Atom) {
					System.out.println(graph.getHandle(start));
					int walkLength = rand.nextInt(allAtoms.size()) + 1;
					randomWalk(graph, start, walkLength, picks, allAtoms, allRels, nestedStructs);
				}
				if (start instanceof Hyperedge) {

					System.out.println(graph.getHandle(start));
					int walkLength = rand.nextInt(allAtoms.size()) + 1;
					randomWalk(graph, start, walkLength, picks, allAtoms, allRels, nestedStructs);
				}
			}

//		}
			if (loopcount > 49) {
				Graphoperations.printDesign(path, null);
			} else {
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

				List<Hyperedge> sethyps = graph
						.getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.Set)));

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
//					Graphoperations.printDesign();
					Graphoperations.printDesign(path, graph);
//					Graphoperations.isConsistant(graph);

//					graph.close();

//					List<HGHandle> all = graph.findAll((hg.type(Hyperedge.class)));
//
//					for (HGHandle hyperedge : all) {
//						graph.remove(hyperedge);
//					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			List<HGHandle> all = graph.findAll((hg.type(Hyperedge.class)));

			for (HGHandle hyperedge : all) {
				graph.remove(hyperedge);
			}
		}
	}

	private static double setProb() {
		return 0;//
//		return Double.valueOf(structs + 1) / Double.valueOf(2 + atoms + structs);
	}

	private static boolean canDoOperation(double probability) {
		Random r = new Random();
		return r.nextDouble() <= probability;
	}

	private static HGHandle getOther(HyperGraph graph, Relationship rel, Atom a) {

		HGHandle ret = null;

		Iterator<HGHandle> it = rel.iterator();

		while (it.hasNext()) {
			HGHandle hgHandle = (HGHandle) it.next();
			Atom atm = graph.get(hgHandle);
//			System.out.println(atm);
			if (atm != a) {
				ret = hgHandle;
				break;
			}
		}

		return ret;
	}

	private static void randomWalk(HyperGraph graph, Element e, int elements, ArrayList<Element> picks,
			ArrayList<Element> allAtoms, ArrayList<Element> allRels, ArrayList<Element> nestedStructs) {
		System.out.println("picking  " + elements);
		ArrayList<HGHandle> handles = new ArrayList<>();
		ArrayList<HGHandle> setHandles = new ArrayList<>();
		HGHandle atomHAndle = null;
		HGHandle updated = null;
		if (e instanceof Atom) {
//			System.out.println(e);
			atomHAndle = graph.getHandle(e);
			System.out.println("starting with" + e);
			handles.add(atomHAndle);
			picks.remove(picks.indexOf(e));

			System.out.println(allAtoms);
			if (allAtoms.contains(e)) {
				allAtoms.remove(allAtoms.indexOf(e));
			}
		}

		if (e instanceof Hyperedge) {
//				System.out.println(e);
			if (((Hyperedge) e).getType() == HyperedgeTypeEnum.Struct) {

				atomHAndle = ((Hyperedge) e).getRoot();
				System.out.println("starting with" + e);
//				picks.remove(picks.indexOf(e));
				updated = graph.getHandle(e);
			}

			else {
				System.out.println("set");
				System.out.println(e);
				List<HGHandle> hypList = ((Hyperedge) e).findAll(hg.type(Hyperedge.class));

				System.out.println(hypList.get(0).getClass());

				// MAybe use atom too and grow into a set
				HGHandle tmpHandle = hypList.get(new Random().nextInt(hypList.size()));

				System.out.println(tmpHandle);
				System.out.println(((Hyperedge) graph.get(tmpHandle)).getRoot());

				atomHAndle = ((Hyperedge) graph.get(tmpHandle)).getRoot();

//				System.out.println(atomHAndle);
				System.out.println("starting with" + graph.get(atomHAndle));
//				picks.remove(picks.indexOf(e));
				updated = tmpHandle;
			}
		}

		System.out.println("here");

		HGBreadthFirstTraversal traversal = new HGBreadthFirstTraversal(atomHAndle, new SimpleALGenerator(graph));

		System.out.println("Traversing");
		while (traversal.hasNext() && elements > 0) {

			Pair<HGHandle, HGHandle> current = traversal.next();
			Object atom = graph.get(current.getSecond());
//
			if (canDoOperation(0.75)) {
				System.out.println("going in" + atom);
				System.out.println("Continuing");
				continue;
			}

			if (atom instanceof Atom) {
				System.out.println("going in" + atom);
				ArrayList<Element> everything = new ArrayList<>();
				everything.addAll(picks);
				everything.addAll(nestedStructs);
				everything.addAll(allAtoms);
				if (updated != null) {
					System.out.println("updated is" + updated);
					System.out.println((Hyperedge) graph.get(updated));
					System.out.println(everything.contains(graph.get(updated)));
					System.out.println(everything);
					everything.remove(graph.get(updated));
					System.out.println(everything);
				}
//				if() 

				Set<Element> set = new LinkedHashSet<>();

				set.addAll(everything);
				everything.clear();
				everything.addAll(set);

				Collections.shuffle(everything);
				Relationship relationship = graph.get(current.getFirst());
				Atom atm = graph.get(current.getSecond());
				System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
				System.out.println(everything);
				for (Element el : everything) {
					System.out.println("everything" + el);
					if (el == atom) {
						System.out.println("ATOM");
						// cant add relationship if its not already there
						if (e instanceof Hyperedge) {
							if (((Hyperedge) e).getType() == HyperedgeTypeEnum.Struct
									&& !((Hyperedge) e).isMember(getOther(graph, relationship, atm))) {
								System.out.println("breaking 1");
								continue;
							}
							if (((Hyperedge) e).getType() == HyperedgeTypeEnum.Set
									&& (!((Hyperedge) graph.get(updated)).isMember(getOther(graph, relationship, atm))
											|| !((Hyperedge) graph.get(updated))
													.isMember(getOther(graph, relationship, atm)))) {
								System.out.println("breaking 2");
								continue;
							}
						}

						if (e instanceof Atom) {

							System.out.println("other   ->" + getOther(graph, relationship, atm));
							if (!handles.contains(getOther(graph, relationship, atm))) {
								System.out.println("breaking 3");
								continue;
							}
						}
						System.out.println("XXXXXXXXXXXXX");
						handles.add(current.getFirst());
						handles.add(current.getSecond());
						System.out.println((Element) graph.get(current.getFirst()));
						if (picks.contains(graph.get(current.getSecond())))
							picks.remove(picks.indexOf(graph.get(current.getSecond())));
						if (allAtoms.contains(graph.get(current.getSecond())))
							allAtoms.remove(allAtoms.indexOf(graph.get(current.getSecond())));
						if (allRels.contains(graph.get(current.getFirst())))
							allRels.remove(allRels.indexOf(graph.get(current.getFirst())));
						System.out.println("Visiting atom " + atom);
						elements--;
						break;
					}
					if (el instanceof Hyperedge) {
						System.out.println("HYPEREDGE");
						Hyperedge hyp = (Hyperedge) el;
						System.out.println(hyp);
						if (hyp.getType() == HyperedgeTypeEnum.Struct && (hyp.getRoot() == current.getSecond()
								|| hyp.getRoot() == getOther(graph, relationship, atm))) {

							System.out.println("KKKKKKKKKKKKKKKKKKKKKKK");

//							System.out.println(hyp);
							if (updated != null)
								System.out.println(graph.get(updated).toString());

							if (e instanceof Hyperedge) {
								if (((Hyperedge) e).getType() == HyperedgeTypeEnum.Struct
										&& !((Hyperedge) e).isMember(getOther(graph, relationship, atm))) {
									System.out.println("breaking 4");
									continue;
								}

							}
							if (e instanceof Atom) {
								if (!handles.contains(getOther(graph, relationship, atm))) {
									System.out.println("breaking 6");
									continue;
								}

							}
							System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
							handles.add(graph.getHandle(hyp));
//							handles.add(current.getSecond());
							handles.add(current.getFirst());
							if (allAtoms.contains(graph.get(current.getSecond())))
								allAtoms.remove(allAtoms.indexOf(graph.get(current.getSecond())));
							if (allRels.contains(graph.get(current.getFirst())))
								allRels.remove(allRels.indexOf(graph.get(current.getFirst())));
							System.out.println("Adding struct " + hyp);
							picks.remove(picks.indexOf(hyp));
							elements--;
							break;
						} else {
//							if (((Hyperedge) hyp).getType() == HyperedgeTypeEnum.Set
//									&& (!((Hyperedge) graph.get(updated)).isMember(getOther(graph, relationship, atm))
//											|| !((Hyperedge) graph.get(updated))
//													.isMember(getOther(graph, relationship, atm)))) {
//								System.out.println("breaking 5");
//								continue;
//							}
//
//							if (e instanceof Hyperedge && ((Hyperedge) hyp).getType() == HyperedgeTypeEnum.Struct
//									&& ((Hyperedge) el).getType() == HyperedgeTypeEnum.Set) {
//
//								
//								for (HGHandle hgHandle : ((Hyperedge) el).findAll()) {
//									Object element = graph.get(hgHandle);
//
//									if (element instanceof Atom) {
//
//									} else if (element instanceof Hyperedge) {
//
//									}
//								}
//
//							}
//							else 

							if (e instanceof Hyperedge && hyp.getType() == HyperedgeTypeEnum.Set
									&& ((Hyperedge) e).getType() == HyperedgeTypeEnum.Struct) {
								System.out.println("FUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
//								handles.add(current.getFirst());
//								handles.add(current.getSecond());
//								System.out.println((Element) graph.get(current.getFirst()));
//								if (picks.contains(graph.get(current.getSecond())))
//									picks.remove(picks.indexOf(graph.get(current.getSecond())));
//								if (allAtoms.contains(graph.get(current.getSecond())))
//									allAtoms.remove(allAtoms.indexOf(graph.get(current.getSecond())));
//								if (allRels.contains(graph.get(current.getFirst())))
//									allRels.remove(allRels.indexOf(graph.get(current.getFirst())));
//								System.out.println("Visiting atom by set" + atom);
//								elements--;
								break;
							}

						}

					}

				}

			}

		}

		try {
			if (updated == null) {
				HGHandle struct = Graphoperations.addHyperedgetoGraph(graph, e.getName(), HyperedgeTypeEnum.Struct,
						handles.toArray(new HGHandle[handles.size()]));

				if (canDoOperation(setProb())) {
					System.out.println("making set");
					HGHandle set = Graphoperations.addSetHyperedgetoGraph(graph, e.getName(), new ArrayList<>(),
							struct);
					picks.add(graph.get(set));
					nestedStructs.add(graph.get(struct));
				} else
					picks.add(graph.get(struct));
			} else {
				Hyperedge updatedElm = (Hyperedge) graph.get(updated);
				List<HGHandle> existing = updatedElm.findAll();
				for (HGHandle handle : handles) {
					if (!existing.contains(handle)) {
						updatedElm.add(handle);
					}
				}
				graph.update(updatedElm);
				if (!setHandles.isEmpty()) {
					System.out.println("WHOOO not empty");
					Hyperedge updatedset = (Hyperedge) graph.get(updated);
					List<HGHandle> existingrels = updatedset.findAll();
					for (HGHandle handle : setHandles) {
						if (!existingrels.contains(handle)) {
							updatedset.add(handle);
						}
					}
					graph.update(updatedset);
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
