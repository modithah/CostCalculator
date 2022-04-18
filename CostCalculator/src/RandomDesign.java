import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.algorithms.HGBreadthFirstTraversal;
import org.hypergraphdb.algorithms.SimpleALGenerator;
import org.hypergraphdb.util.Pair;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.ops.Graphoperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomDesign {

	public RandomDesign() {
		// TODO Auto-generated constructor stub

	}

	final double PinternalSet = 0.75;
//	static int atoms = 0;
//	static int structs = 0;
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
	public static void main(String[] args) throws IOException {
		File serverDir = new File(Const.HG_LOCATION_BOOK);
		FileUtils.cleanDirectory(serverDir);
//		LoadGraph.LoadBaseFromJSONFile("C:\\Users\\Moditha\\Documents\\PhD\\SVN\\Schemas\\demo\\booksample.json");
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

//		logger.info(setProb(atoms, structs));

			logger.info(String.valueOf(allAtoms.size()));
	

			int loopcount = 0;
			ArrayList<Element> prevPicks = picks;
			while (!allAtoms.isEmpty() && !allRels.isEmpty()) {
				logger.info("##################");
				logger.info(allAtoms.toString());
				logger.info(allRels.toString());
				logger.info(picks.toString());
				logger.info("##################");

				Element start = picks.get(rand.nextInt(picks.size()));

				if (picks == prevPicks) {
					loopcount++;
				} else {
					loopcount = 0;
				}

				if (loopcount > 50) {
					logger.info("couldnt make");
					break;
				}

				logger.info("atoms  rels" + allAtoms + "  " + allRels);
				if (start instanceof Atom) {
					logger.info(graph.getHandle(start).toString());
					int walkLength = rand.nextInt(allAtoms.size()) + 1;
					randomWalk(graph, start, walkLength, picks, allAtoms, allRels, nestedStructs);
				}
				if (start instanceof Hyperedge) {

					logger.info(graph.getHandle(start).toString());
					int walkLength = rand.nextInt(allAtoms.size()) + 1;
					randomWalk(graph, start, walkLength, picks, allAtoms, allRels, nestedStructs);
				}
			}

//		}
			if (loopcount > 49) {
				Graphoperations.printDesign(path, null);
			} else {
				logger.info("end");
				List<HGHandle> firstLevels = new ArrayList<>();
				List<Hyperedge> structhyps = graph
						.getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.Struct)));

				// wrap structs without sets around sets and make them top level

				for (Hyperedge hyperedge : structhyps) {
					HGHandle handle = graph.getHandle(hyperedge);
					// HGSearchResult<Object> x = graph.find(hg.contains(handle));

					if (hyperedge.getParents().size()>0) {
						logger.info("not empty");
//						logger.info(graph.get((HGHandle) x.next()).toString());

					} else {
						logger.info("empty");
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
//					HGSearchResult<Object> x = graph.find(hg.contains(handle));
					if (hyperedge.getParents().size()>0) {
						logger.info("not empty");
					} else {
						logger.info("empty");
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
//			logger.info(atm);
			if (atm != a) {
				ret = hgHandle;
				break;
			}
		}

		return ret;
	}

	private static void randomWalk(HyperGraph graph, Element e, int elements, ArrayList<Element> picks,
			ArrayList<Element> allAtoms, ArrayList<Element> allRels, ArrayList<Element> nestedStructs) {
		logger.info("picking  " + elements);
		ArrayList<HGHandle> handles = new ArrayList<>();
		ArrayList<HGHandle> setHandles = new ArrayList<>();
		HGHandle atomHAndle = null;
		HGHandle updated = null;
		if (e instanceof Atom) {
//			logger.info(e);
			atomHAndle = graph.getHandle(e);
			logger.info("starting with" + e);
			handles.add(atomHAndle);
			picks.remove(picks.indexOf(e));

			logger.info(allAtoms.toString());
			if (allAtoms.contains(e)) {
				allAtoms.remove(allAtoms.indexOf(e));
			}
		}

		if (e instanceof Hyperedge) {
//				logger.info(e);
			if (((Hyperedge) e).getType() == HyperedgeTypeEnum.Struct) {

				atomHAndle = ((Hyperedge) e).getRoot();
				logger.info("starting with" + e);
//				picks.remove(picks.indexOf(e));
				updated = graph.getHandle(e);
			}

			else {
				logger.info("set");
				logger.info(e.toString());
				List<HGHandle> hypList = ((Hyperedge) e).findAll(hg.type(Hyperedge.class));

				logger.info(hypList.get(0).getClass().toString());

				// MAybe use atom too and grow into a set
				HGHandle tmpHandle = hypList.get(new Random().nextInt(hypList.size()));

				logger.info(tmpHandle.toString());
				logger.info(((Hyperedge) graph.get(tmpHandle)).getRoot().toString());

				atomHAndle = ((Hyperedge) graph.get(tmpHandle)).getRoot();

//				logger.info(atomHAndle);
				logger.info("starting with" + graph.get(atomHAndle));
//				picks.remove(picks.indexOf(e));
				updated = tmpHandle;
			}
		}

		logger.info("here");

		HGBreadthFirstTraversal traversal = new HGBreadthFirstTraversal(atomHAndle, new SimpleALGenerator(graph));

		logger.info("Traversing");
		while (traversal.hasNext() && elements > 0) {

			Pair<HGHandle, HGHandle> current = traversal.next();
			Object atom = graph.get(current.getSecond());
//
			if (canDoOperation(0.75)) {
				logger.info("going in" + atom);
				logger.info("Continuing");
				continue;
			}

			if (atom instanceof Atom) {
				logger.info("going in" + atom);
				ArrayList<Element> everything = new ArrayList<>();
				everything.addAll(picks);
				everything.addAll(nestedStructs);
				everything.addAll(allAtoms);
				if (updated != null) {
					logger.info("updated is" + updated);
					logger.info(((Hyperedge) graph.get(updated)).toString());
					logger.info(String.valueOf(everything.contains(graph.get(updated))));
					logger.info(everything.toString());
					everything.remove(graph.get(updated));
					logger.info(everything.toString());
				}
//				if() 

				Set<Element> set = new LinkedHashSet<>();

				set.addAll(everything);
				everything.clear();
				everything.addAll(set);

				Collections.shuffle(everything);
				Relationship relationship = graph.get(current.getFirst());
				Atom atm = graph.get(current.getSecond());
				logger.info("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
				logger.info(everything.toString());
				for (Element el : everything) {
					logger.info("everything" + el);
					if (el == atom) {
						logger.info("ATOM");
						// cant add relationship if its not already there
						if (e instanceof Hyperedge) {
							if (((Hyperedge) e).getType() == HyperedgeTypeEnum.Struct
									&& !((Hyperedge) e).isMember(getOther(graph, relationship, atm))) {
								logger.info("breaking 1");
								continue;
							}
							if (((Hyperedge) e).getType() == HyperedgeTypeEnum.Set
									&& (!((Hyperedge) graph.get(updated)).isMember(getOther(graph, relationship, atm))
											|| !((Hyperedge) graph.get(updated))
													.isMember(getOther(graph, relationship, atm)))) {
								logger.info("breaking 2");
								continue;
							}
						}

						if (e instanceof Atom) {

							logger.info("other   ->" + getOther(graph, relationship, atm));
							if (!handles.contains(getOther(graph, relationship, atm))) {
								logger.info("breaking 3");
								continue;
							}
						}
						logger.info("XXXXXXXXXXXXX");
						handles.add(current.getFirst());
						handles.add(current.getSecond());
						logger.info(((Element) graph.get(current.getFirst())).toString());
						if (picks.contains(graph.get(current.getSecond())))
							picks.remove(picks.indexOf(graph.get(current.getSecond())));
						if (allAtoms.contains(graph.get(current.getSecond())))
							allAtoms.remove(allAtoms.indexOf(graph.get(current.getSecond())));
						if (allRels.contains(graph.get(current.getFirst())))
							allRels.remove(allRels.indexOf(graph.get(current.getFirst())));
						logger.info("Visiting atom " + atom);
						elements--;
						break;
					}
					if (el instanceof Hyperedge) {
						logger.info("HYPEREDGE");
						Hyperedge hyp = (Hyperedge) el;
						logger.info(hyp.toString());
						if (hyp.getType() == HyperedgeTypeEnum.Struct && (hyp.getRoot() == current.getSecond()
								|| hyp.getRoot() == getOther(graph, relationship, atm))) {

							logger.info("KKKKKKKKKKKKKKKKKKKKKKK");

//							logger.info(hyp);
							if (updated != null)
								logger.info(graph.get(updated).toString());

							if (e instanceof Hyperedge) {
								if (((Hyperedge) e).getType() == HyperedgeTypeEnum.Struct
										&& !((Hyperedge) e).isMember(getOther(graph, relationship, atm))) {
									logger.info("breaking 4");
									continue;
								}

							}
							if (e instanceof Atom) {
								if (!handles.contains(getOther(graph, relationship, atm))) {
									logger.info("breaking 6");
									continue;
								}

							}
							logger.info("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
							handles.add(graph.getHandle(hyp));
//							handles.add(current.getSecond());
							handles.add(current.getFirst());
							if (allAtoms.contains(graph.get(current.getSecond())))
								allAtoms.remove(allAtoms.indexOf(graph.get(current.getSecond())));
							if (allRels.contains(graph.get(current.getFirst())))
								allRels.remove(allRels.indexOf(graph.get(current.getFirst())));
							logger.info("Adding struct " + hyp);
							picks.remove(picks.indexOf(hyp));
							elements--;
							break;
						} else {
//							if (((Hyperedge) hyp).getType() == HyperedgeTypeEnum.Set
//									&& (!((Hyperedge) graph.get(updated)).isMember(getOther(graph, relationship, atm))
//											|| !((Hyperedge) graph.get(updated))
//													.isMember(getOther(graph, relationship, atm)))) {
//								logger.info("breaking 5");
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
								logger.info("FUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
//								handles.add(current.getFirst());
//								handles.add(current.getSecond());
//								logger.info((Element) graph.get(current.getFirst()));
//								if (picks.contains(graph.get(current.getSecond())))
//									picks.remove(picks.indexOf(graph.get(current.getSecond())));
//								if (allAtoms.contains(graph.get(current.getSecond())))
//									allAtoms.remove(allAtoms.indexOf(graph.get(current.getSecond())));
//								if (allRels.contains(graph.get(current.getFirst())))
//									allRels.remove(allRels.indexOf(graph.get(current.getFirst())));
//								logger.info("Visiting atom by set" + atom);
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
					logger.info("making set");
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
					logger.info("WHOOO not empty");
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
