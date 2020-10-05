package edu.upc.essi.catalog.ops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

//import javax.management.relation.Relation;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPlainLink;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.algorithms.HGBreadthFirstTraversal;
import org.hypergraphdb.algorithms.HGDepthFirstTraversal;
import org.hypergraphdb.algorithms.SimpleALGenerator;
import org.hypergraphdb.util.Pair;


import org.hypergraphdb.HyperGraph;

import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.core.constructs.opsparams.EmbedParams;
import edu.upc.essi.catalog.core.constructs.opsparams.GroupParams;
import edu.upc.essi.catalog.enums.AtomTypeEnum;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.util.TargetSetALGenerator;

public final class Transformations {

	public static boolean union(HyperGraph graph, Hyperedge hyp1, Hyperedge hyp2) {

		// TODO condidions
		List<HGHandle> parents1 = graph.findAll(hg.contains(graph.getHandle(hyp1)));
		List<HGHandle> parents2 = graph.findAll(hg.contains(graph.getHandle(hyp2)));

		if (!((hyp1.getType() == hyp2.getType() && hyp2.getType() == HyperedgeTypeEnum.Set)
				|| (hyp1.getType() == hyp2.getType() && hyp2.getType() == HyperedgeTypeEnum.FirstLevel))) {
			System.out.println("Hyperedges must be sets");
			return false;
		}

		if (!(parents1.containsAll(parents2) && parents2.containsAll(parents1))) {
			System.out.println("they should have the same parent");
			return false;
		}

		// Add children to the first hyperedge
		List<HGHandle> children = hyp2.findAll();

		for (HGHandle child : children) {
			hyp1.add(child);
		}

		graph.update(hyp1);

		graph.remove(graph.getHandle(hyp2));

		return true;
	}

	public static ArrayList<Pair<Hyperedge, Hyperedge>> getUnionCandidates(HyperGraph graph) {
		ArrayList<Pair<Hyperedge, Hyperedge>> candidates = new ArrayList<>();
		Queue<Hyperedge> queue = new LinkedList<>();
		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph);
		makeHyperedgePairs(firstLevels, candidates);
		firstLevels.forEach(f -> {
			f.findAll().forEach(s -> {
				Element e = graph.get(s); // secondLevel
				if (e instanceof Hyperedge) {
					queue.add((Hyperedge) e);
				}
			});
		});
//		System.out.println("-----------");
		while (!queue.isEmpty()) {
			Hyperedge hyp = queue.poll();
			if (hyp.getType() == HyperedgeTypeEnum.Set) {
				hyp.findAll().forEach(cand -> {
					Element candE = graph.get(cand); // inner elements
					if (candE instanceof Hyperedge && ((Hyperedge) candE).getType() == HyperedgeTypeEnum.Struct) {
						queue.add((Hyperedge) candE);
					}
				});
			} else if (hyp.getType() == HyperedgeTypeEnum.Struct || hyp.getType() == HyperedgeTypeEnum.SecondLevel) {
				List<Hyperedge> innerSets = new ArrayList<>();
				hyp.findAll().forEach(cand -> {
					Element candE = graph.get(cand); // inner elements
					if (candE instanceof Hyperedge) {
						Hyperedge hyper = (Hyperedge) candE;
						if (hyper.getType() == HyperedgeTypeEnum.Struct) {
							queue.add((Hyperedge) hyper);
						} else if (hyper.getType() == HyperedgeTypeEnum.Set) {
							innerSets.add(hyper);
						}
					}
				});
				if (innerSets.size() > 1) {
					makeHyperedgePairs(innerSets, candidates);
				}
			}
		}
		return candidates;
	}

	private static void makeHyperedgePairs(List<Hyperedge> hyperedges, ArrayList<Pair<Hyperedge, Hyperedge>> pairs) {
		Set<Set<Hyperedge>> combos = Sets.combinations(ImmutableSet.copyOf(hyperedges), 2);
		Iterator<Set<Hyperedge>> comboIterator = combos.iterator();
		while (comboIterator.hasNext()) {
			Set<Hyperedge> set = (Set<Hyperedge>) comboIterator.next();
			Object[] arr = set.toArray();
//			System.out.println(arr[0] + "---" + arr[1]);
			pairs.add(new Pair<Hyperedge, Hyperedge>((Hyperedge) arr[0], (Hyperedge) arr[1]));
		}
	}

	public static boolean flatten(HyperGraph graph, Hyperedge hyp) {
		HGHandle hypHandle = graph.getHandle(hyp);
		HGHandle parentHandle = graph.findAll(hg.contains(hypHandle)).get(0);
		Hyperedge parent = graph.get(parentHandle);

		if (!(parent.getType() == HyperedgeTypeEnum.Struct || parent.getType() == HyperedgeTypeEnum.SecondLevel)) {
			System.out.println("parent must be a struct to flatten");
			return false;
		}

		// Add children to the first hyperedge
		List<HGHandle> children = hyp.findAll();

		for (HGHandle child : children) {
			parent.add(child);
		}

		parent.remove(hypHandle);
		graph.remove(hypHandle);
		graph.update(parent);
		return true;
	}

	public static ArrayList<Hyperedge> getFlattenCandidates(HyperGraph graph) {
		ArrayList<Hyperedge> candidates = new ArrayList<>();
		Queue<Hyperedge> queue = new LinkedList<>();
		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph);
		firstLevels.forEach(f -> {
			f.findAll().forEach(s -> {
				Element e = graph.get(s); // secondLevel
				if (e instanceof Hyperedge) {
					((Hyperedge) e).findAll().forEach(cand -> {
						Element candE = graph.get(cand); // inner elements
						if (candE instanceof Hyperedge) {
							queue.add((Hyperedge) candE);
						}
					});
				}
			});
		});

		while (!queue.isEmpty()) {
			Hyperedge hyp = queue.poll();
			if (hyp.getType() == HyperedgeTypeEnum.Set) {
				candidates.add(hyp);
			}
			hyp.findAll().forEach(cand -> {
				Element candE = graph.get(cand); // inner elements
				if (candE instanceof Hyperedge) {
					queue.add((Hyperedge) candE);
				}
			});
		}

		return candidates;
	}

	public static boolean group(HyperGraph graph, GroupParams param) {

		Hyperedge hyp = param.getHyp();
		ArrayList<Relationship> rels = param.getRels();
		Element elm = param.getElm();

		if (!(hyp.getType() == HyperedgeTypeEnum.Struct || hyp.getType() == HyperedgeTypeEnum.SecondLevel)) {
			System.out.println("Group can be only inside a struct");
			return false;
		} else {
			ArrayList<HGHandle> relHandles = new ArrayList<>();
			HGHandle elementHandle = graph.getHandle(elm);
			rels.forEach(r -> {
				relHandles.add(graph.getHandle(r));
			});
			List<HGHandle> parentContents = hyp.findAll();
			if (!parentContents.containsAll(relHandles) || !parentContents.contains(elementHandle)) {
				System.out.println(parentContents.containsAll(relHandles));
				System.out.println(parentContents.contains(elementHandle));
				System.out.println("relationships and element should be inside the parent");
				return false;
			} else {
				if (elm instanceof Relationship) {
					System.out.println("element cannot be a relationship");
					return false;
				} else if (elm instanceof Hyperedge && ((Hyperedge) elm).getType() != HyperedgeTypeEnum.Struct) {
					System.out.println("hyperedge element must be a struct");
					return false;
				} else {
					try {
						System.out.println("hype is " + hyp);
						HGHandle newSet = Graphoperations.addSetHyperedgetoGraph(graph, elm.getName() + "Set", rels,
								elementHandle);
//						System.out.println((Hyperedge) graph.get(newSet));
						((Hyperedge) graph.get(newSet)).print(0);
//						newSet=null;
						hyp.remove(elementHandle);
//						System.out.println("removing  "+ elementHandle);
						Set<Relationship> used = findUsedRelationships(graph, hyp, newSet);

//						System.out.println(used.size());
						rels.removeAll(used);
//						System.out.println("rels   " + rels.size());
						for (Relationship relationship : rels) {
							hyp.remove(graph.getHandle(relationship));
						}
						hyp.add(newSet);
						graph.update(hyp);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
				}
			}
		}

//		HGHandle hypHandle = graph.getHandle(hyp);
//		HGHandle parentHandle = graph.findAll(hg.contains(hypHandle)).get(0);
//		Hyperedge parent = graph.get(parentHandle);
//
//		
//
//		// Add children to the first hyperedge
//		List<HGHandle> children = hyp.findAll();
//
//		for (HGHandle child : children) {
//			parent.add(child);
//		}
//
//		parent.remove(hypHandle);
//		graph.remove(hypHandle);
//		graph.update(parent);
		return true;
	}

	private static Set<Relationship> findUsedRelationships(HyperGraph graph, Hyperedge hyp, HGHandle except) {
		List<HGHandle> parentContents;
		Set<Relationship> used = new HashSet();
		parentContents = hyp.findAll();
		System.out.println("FFFFFFFFFF \n" + parentContents);
		parentContents.forEach(hdl -> {
			if (!hdl.equals(except)) {
//				System.out.println(hdl);

				Element e = graph.get(hdl);
//								System.out.println("path to" + e.getName());
				System.out.println(e);
				if (e instanceof Atom) {
					used.addAll(findRelPath(graph, hyp, (Atom) e));
				} else if (e instanceof Hyperedge) {
					Hyperedge edge = (Hyperedge) e;

					if (edge.getType() == HyperedgeTypeEnum.Struct || edge.getType() == HyperedgeTypeEnum.SecondLevel) {
						used.addAll(findRelPath(graph, hyp, graph.get(edge.getRoot())));
					} else {
						edge.findAll().forEach(h -> {
							Element el = graph.get(h);
							if (el instanceof Atom) {
								used.addAll(findRelPath(graph, hyp, (Atom) el));
							} else if (el instanceof Hyperedge) {
								used.addAll(findRelPath(graph, hyp, graph.get(((Hyperedge) el).getRoot())));
							}
						});
					}
				}
			}
		});
		return used;
	}

	public static ArrayList<GroupParams> getGroupCandidates(HyperGraph graph) {
		ArrayList<GroupParams> candidates = new ArrayList<>();
		Queue<Pair<Hyperedge, Element>> queue = new LinkedList<>();
		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph);

		firstLevels.forEach(f -> {
			f.findAll().forEach(s -> {
				Element e = graph.get(s); // secondLevel
				if (e instanceof Hyperedge) {
					((Hyperedge) e).findAll().forEach(cand -> {
						Element candE = graph.get(cand); // inner elements
						if (candE instanceof Hyperedge) {
							queue.add(new Pair<Hyperedge, Element>((Hyperedge) e, (Element) candE));
						} else if (candE instanceof Atom) {
							// TODO : should we make set of atoms ?
						}
					});
				}
			});
		});

		while (!queue.isEmpty()) {
			Pair<Hyperedge, Element> pair = queue.poll();
			Hyperedge parent = pair.getFirst();
			Element child = pair.getSecond();
			if (child instanceof Hyperedge) {
				Hyperedge childHyperedge = ((Hyperedge) child);
				if (childHyperedge.getType() == HyperedgeTypeEnum.Set) {
					childHyperedge.findAll().forEach(s -> {
						Element e = graph.get(s); // structs
						if (e instanceof Hyperedge && ((Hyperedge) e).getType() == HyperedgeTypeEnum.Struct) {
							((Hyperedge) e).findAll().forEach(cand -> {
								Element candE = graph.get(cand); // inner elements
								if (candE instanceof Hyperedge) {
									queue.add(new Pair<Hyperedge, Element>((Hyperedge) e, (Element) candE));
								} else if (candE instanceof Atom) {
									// TODO : should we make set of atoms ?
								}
							});
						}
					});

				} else if (childHyperedge.getType() == HyperedgeTypeEnum.Struct) {
					GroupParams params = new GroupParams();
					params.setHyp(parent);
					params.setElm(childHyperedge);
					params.setRels(findRelPath(graph, parent, graph.get(childHyperedge.getRoot())));

					candidates.add(params);

					childHyperedge.findAll().forEach(cand -> {
						Element candE = graph.get(cand); // inner elements
						if (candE instanceof Hyperedge) {
							queue.add(new Pair<Hyperedge, Element>(childHyperedge, (Element) candE));
						} else if (candE instanceof Atom) {
							// TODO : should we make set of atoms ?
						}
					});
				}
			}

			else {
				// TODO: making sets with only atoms ??
			}
		}

		return candidates;
	}

	public static boolean segregate(HyperGraph graph, Hyperedge set, Element el) {

		if (!(set.getType() == HyperedgeTypeEnum.Set || set.getType() == HyperedgeTypeEnum.FirstLevel)) {
			System.out.println("Segregate can be only inside a Set");
			return false;
		} else {
			HGHandle elementHandle = graph.getHandle(el);
			HGHandle setHandle = graph.getHandle(set);
			if (!set.isMember(elementHandle)) {
				System.out.println("Element must be inside the Set");
				return false;
			} else {
				try {
					if (set.getType() == HyperedgeTypeEnum.FirstLevel) {
						HGHandle newFirstLevel = Graphoperations.addHyperedgetoGraph(graph, el.getName() + "FL",
								HyperedgeTypeEnum.FirstLevel, elementHandle);
						set.remove(elementHandle);
						graph.update(set);
						HGHandle topDesignHandle = Graphoperations.getParentHyperedges(graph, setHandle).get(0);
						Hyperedge topDesign = graph.get(topDesignHandle);
						topDesign.add(newFirstLevel);
						graph.update(topDesign);
						return true;
					} else {
						// set can have only one parent
						HGHandle grandParentHandle = Graphoperations.getParentHyperedges(graph, setHandle).get(0);
						Hyperedge grandParent = graph.get(grandParentHandle);
						Set<Relationship> used = new HashSet<>();
						set.findAll().forEach(c -> {
							if (!c.equals(elementHandle)) {
								Element child = graph.get(c);
								if (child instanceof Atom) {
									used.addAll(findRelPath(graph, grandParent, (Atom) child));
								} else {
									HGHandle rootofStruct = ((Hyperedge) child).getRoot();
									used.addAll(findRelPath(graph, grandParent, graph.get(rootofStruct)));
								}

							}
						});

						if (el instanceof Hyperedge) {
							ArrayList<Relationship> pathtoStruct = findRelPath(graph, grandParent,
									graph.get(((Hyperedge) el).getRoot()));
							HGHandle newSet = Graphoperations.addSetHyperedgetoGraph(graph, el.getName() + "Set",
									pathtoStruct, elementHandle);
							grandParent.add(newSet);
							pathtoStruct.removeAll(used);
							for (Relationship relationship : pathtoStruct) {
								set.remove(graph.getHandle(relationship));
							}
							set.remove(elementHandle);
							graph.update(grandParent);
							graph.update(set);
						} else {
//							TODO : do we create with atoms ??
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}

		}
		return false;
	}

	public static ArrayList<Pair<Hyperedge, Hyperedge>> getSegregateCandidates(HyperGraph graph) {
		ArrayList<Pair<Hyperedge, Hyperedge>> candidates = new ArrayList<>();
		Queue<Hyperedge> queue = new LinkedList<>();
		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph);
		firstLevels.forEach(f -> {
			ArrayList<Pair<Hyperedge, Hyperedge>> tmpCandidates = new ArrayList<>();
			f.findAll().forEach(s -> {
				Element e = graph.get(s); // secondLevel
				if (e instanceof Hyperedge) {
					tmpCandidates.add(new Pair<Hyperedge, Hyperedge>(f, (Hyperedge) e));
					queue.add((Hyperedge) e);
				}
			});
			if (tmpCandidates.size() > 1) { // only add sets with more than one struct
				candidates.addAll(tmpCandidates);
			}
		});
//		System.out.println("-----------");
		while (!queue.isEmpty()) {
			Hyperedge hyp = queue.poll();
			if (hyp.getType() == HyperedgeTypeEnum.Set) {
				ArrayList<Pair<Hyperedge, Hyperedge>> tmpCandidates = new ArrayList<>();
				hyp.findAll().forEach(cand -> {
					Element candE = graph.get(cand); // inner elements
					if (candE instanceof Hyperedge && ((Hyperedge) candE).getType() == HyperedgeTypeEnum.Struct) {
						tmpCandidates.add(new Pair<Hyperedge, Hyperedge>(hyp, (Hyperedge) candE));
						queue.add((Hyperedge) candE);
					} else if (candE instanceof Atom && ((Atom) candE).getType() == AtomTypeEnum.Class) {
						// TODO : are we separating atomas ??
					}
				});
				if (tmpCandidates.size() > 1) { // only add sets with more than one struct
					candidates.addAll(tmpCandidates);
				}
			} else if (hyp.getType() == HyperedgeTypeEnum.Struct || hyp.getType() == HyperedgeTypeEnum.SecondLevel) {
				hyp.findAll().forEach(cand -> {
					Element candE = graph.get(cand); // inner elements
					if (candE instanceof Hyperedge) {
						Hyperedge hyper = (Hyperedge) candE;
						queue.add((Hyperedge) hyper);
					}
				});
			}
		}
		return candidates;
	}

	// TODO : Maybe error Handling
//	public static boolean embed(HyperGraph graph, EmbedParams param) {
//
//		HGHandle childHandle = graph.getHandle(param.getChild());
//		Hyperedge grandParent = param.getGrandParent();
//		Hyperedge newParent = param.getEmbeddingParent();
//		Set<Relationship> used = findUsedRelationships(graph, grandParent, childHandle);
//		ArrayList<Relationship> relPath = findRelPath(graph, grandParent, graph.get(param.getChild().getRoot()));
//		relPath.removeAll(used);
//		for (Relationship relationship : relPath) {
//			grandParent.remove(graph.getHandle(relationship));
//		}
//		grandParent.remove(childHandle);
//		newParent.add(childHandle);
//		newParent.remove(param.getChild().getRoot());
//		graph.update(grandParent);
//		graph.update(newParent);
//		return true;
//
//	}

	public static boolean embed(HyperGraph graph, EmbedParams param) {

		Hyperedge child = param.getChild();
		HGHandle childHandle = graph.getHandle(child);
		Hyperedge grandParent = param.getGrandParent();
		Hyperedge newParent = param.getParent();
		Set<Relationship> used = findUsedRelationships(graph, grandParent, childHandle);
//		System.out.println("XXXXXXXXXXXXXXXXXXXXX");
//		System.out.println(param.getChild());
//		System.out.println(graph.get(param.getChild().getRoot()).toString());
		ArrayList<Relationship> relPath = findRelPath(graph, grandParent, graph.get(param.getChild().getRoot()));
		relPath.removeAll(used);
		for (Relationship relationship : relPath) {
			HGHandle rel = graph.getHandle(relationship);
			grandParent.remove(rel);
			newParent.add(rel);
		}
		if (child.getType() == HyperedgeTypeEnum.FirstLevel) {
			child.setType(HyperedgeTypeEnum.Struct);
			graph.update(child);
		}
		grandParent.remove(childHandle);
		newParent.add(childHandle);
		// TODO : should we remove if this is from a reference ? how would we know ?
//		newParent.remove(param.getChild().getRoot());  
		graph.update(grandParent);
		graph.update(newParent);
		return true;

	}

	public static ArrayList<EmbedParams> getEmbedCandidates(HyperGraph graph) {
		ArrayList<EmbedParams> candidates = new ArrayList<>();
		Queue<Hyperedge> queue = new LinkedList<>();
		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph);

		queue.addAll(firstLevels);

		while (!queue.isEmpty()) {
			Hyperedge hyp = queue.poll();
			if (hyp.getType() == HyperedgeTypeEnum.Set || hyp.getType() == HyperedgeTypeEnum.FirstLevel) {
				ArrayList<Hyperedge> tmpCandidates = new ArrayList<>();
				hyp.findAll().forEach(cand -> {
					Element candE = graph.get(cand); // inner elements
					if (candE instanceof Hyperedge && (((Hyperedge) candE).getType() == HyperedgeTypeEnum.Struct
							|| ((Hyperedge) candE).getType() == HyperedgeTypeEnum.SecondLevel)) {
						tmpCandidates.add((Hyperedge) candE);
						queue.add((Hyperedge) candE);
					}
				});

				executeStructCombinations(graph, candidates, tmpCandidates, hyp);
			} else if (hyp.getType() == HyperedgeTypeEnum.Struct || hyp.getType() == HyperedgeTypeEnum.SecondLevel) {
				ArrayList<Hyperedge> tmpCandidates = new ArrayList<>();
				hyp.findAll().forEach(cand -> {
					Element candE = graph.get(cand); // inner elements
					if (candE instanceof Hyperedge) {
						Hyperedge hyper = (Hyperedge) candE;
						queue.add((Hyperedge) hyper);
						if (hyper.getType() == HyperedgeTypeEnum.Struct) { // struct inside struct
							tmpCandidates.add((Hyperedge) candE);
						}
					}
				});
				executeStructCombinations(graph, candidates, tmpCandidates, hyp);
			}
		}

		return candidates;
	}

	public static Pair<Double, Double> getSizeMinMax(HyperGraph graph) {
		List<Atom> classes = Graphoperations.getClassAtomList(graph);
		Double global = 0.0;
		for (Atom atom : classes) {
			long local = atom.getSize();
			List<HGHandle> attHandles = Graphoperations.getAttributesClass(graph, graph.getHandle(atom));
			for (HGHandle handle : attHandles) {
				Atom attrib = graph.get(handle);
				local += attrib.getSize();
			}
			local = local * atom.getCount();
			global += local;
		}

		return new Pair<Double, Double>(global, 10 * global);
	}

	public static Pair<Double, Double> getCostMinMax(HyperGraph graph, ArrayList<Pair<Double, ArrayList<Atom>>> workload) {
		double cost = 0.0;
		
		for (Pair<Double, ArrayList<Atom>> pair : workload) {
			ArrayList<Atom> atomlist = pair.getSecond();
			Atom entry = atomlist.get(0);
			HashMap<Atom, Double> map= new HashMap<Atom, Double>();
			map.put(entry, 1.0);
			
			HGDepthFirstTraversal traversal = 
				    new HGDepthFirstTraversal(graph.getHandle(entry), new SimpleALGenerator(graph));

				while (traversal.hasNext() && !atomlist.isEmpty())
				{
				    Pair<HGHandle, HGHandle> current = traversal.next();
				    Relationship l = (Relationship)graph.get(current.getFirst());
				    Atom atom = graph.get(current.getSecond());
				    if(atom.getType()==AtomTypeEnum.Class) {
//				    	l.getMultiplicities();
				    	ArrayList<String> relorder = new ArrayList<>();
				    	Atom atom1 = ((Atom) graph.get(l.getTargetAt(0)));
				    	Atom atom2 = ((Atom) graph.get(l.getTargetAt(1)));
						relorder.add(atom1.getName());
						relorder.add(atom2.getName());
						Collections.sort(relorder);
						int index=relorder.indexOf(atom.getName());
						Atom parentAtom = atom1==atom?atom2:atom1;
						System.out.println(parentAtom+"  "+map.get(parentAtom));
						double val=l.getMultiplicities()[index]*map.get(parentAtom);
						cost+=val;
						map.put(atom, val);
						System.out.println(cost);
				    }
				    atomlist.remove(atom);
				}
		}
		
		return new Pair<Double, Double>(0.0, cost);

	}

	private static void executeStructCombinations(HyperGraph graph, ArrayList<EmbedParams> candidates,
			ArrayList<Hyperedge> tmpCandidates, Hyperedge grandParent) {
		if (tmpCandidates.size() >= 2) {
			System.out.println(tmpCandidates);
			Set<Set<Hyperedge>> combos = Sets.combinations(ImmutableSet.copyOf(tmpCandidates), 2);
			Iterator<Set<Hyperedge>> comboIterator = combos.iterator();
			while (comboIterator.hasNext()) {
				Set<Hyperedge> set = (Set<Hyperedge>) comboIterator.next();
				Object[] arr = set.toArray();
				for (int i = 0; i < 2; i++) {
					Hyperedge parent = i == 0 ? (Hyperedge) arr[0] : (Hyperedge) arr[1];
					Hyperedge child = i == 0 ? (Hyperedge) arr[1] : (Hyperedge) arr[0];
					EmbedParams param = new EmbedParams(grandParent, parent, child);
					findRelPathWithParent(graph, parent, graph.get(child.getRoot()), param);
					if (param.getEmbeddingParent() != null) {
						candidates.add(param);
					}
				}

			}
		}
	}

	public static ArrayList<Relationship> findRelPath(HyperGraph graph, Hyperedge hyp, Atom atm) {
		HGHandle hypHandle = graph.getHandle(hyp);
		HGHandle atomHAndle = graph.getHandle(atm);
		HashMap<HGHandle, HGHandle> path = new HashMap<>();
		ArrayList<Relationship> rels = new ArrayList<>();
		boolean breakable = false;

		HGBreadthFirstTraversal traversal = new HGBreadthFirstTraversal(hypHandle, new TargetSetALGenerator(graph));

		while (traversal.hasNext()) {
			Pair<HGHandle, HGHandle> current = traversal.next();
			Hyperedge parent = (Hyperedge) graph.get(current.getFirst());
			Object atom = graph.get(current.getSecond());

			if (atom instanceof Relationship) {
				rels.add((Relationship) atom);
//				System.out.println("adding" + atom);
			}

			path.put(current.getSecond(), current.getFirst());
			if (atom instanceof Atom && atom.equals(atm)) {
				System.out.println(atom);
				breakable = true;
			}
			// we found the atom no need to dig deeper
			if (breakable && atom instanceof Hyperedge) {
				break;
			}

//			    if(((Atom)atom).getName().equals("B_ID")) {
//			    	System.out.println("XXXXXXXXXX");
//			    	break;
//			    	}
//			System.out.println("Visiting atom " + atom + " pointed to by " + parent);
		}
//		System.out.println(rels);
		ArrayList<Relationship> finalPath = new ArrayList<>();
		HGHandle targetHandle = atomHAndle;
		if (!rels.isEmpty() && !targetHandle.equals(hyp.getRoot())) {
			do {
//				if () {
//					break;
//				}
				Pair<Relationship, HGHandle> relHandlePair = findNextHop(graph, targetHandle, rels);
//				System.out.println("OOOO  " + relHandlePair.getFirst());

				if (relHandlePair == null) {
//					System.out.println("YYYYYYY");
					break;
				}
				if (finalPath.contains(relHandlePair.getFirst())) {
//					System.out.println("SSSSSSSSSSSSSSSS");
					break;
				} else {
					finalPath.add(relHandlePair.getFirst());
//					System.out.println(relHandlePair.getSecond() + "  " + hyp.getRoot());
//					if (!((hyp.getType() == HyperedgeTypeEnum.Struct || hyp.getType() == HyperedgeTypeEnum.SecondLevel)
//							&& hyp.getRoot().equals(relHandlePair.getSecond()))) {
//					System.out.println();

					if (relHandlePair.getSecond().equals(hyp.getRoot())) {
						// MAYBE we have to check about the sets usability
						break;
					} else {
						targetHandle = relHandlePair.getSecond();
					}
//					}
//					}
				}
			} while (true);
		}
		return finalPath;
	}

	public static void findRelPathWithParent(HyperGraph graph, Hyperedge hyp, Atom atm, EmbedParams param) {
		HGHandle hypHandle = graph.getHandle(hyp);
		HGHandle atomHAndle = graph.getHandle(atm);
		HashMap<HGHandle, HGHandle> path = new HashMap<>();
		ArrayList<Relationship> rels = new ArrayList<>();
		boolean breakable = false;

		HGBreadthFirstTraversal traversal = new HGBreadthFirstTraversal(hypHandle, new TargetSetALGenerator(graph));

		while (traversal.hasNext()) {
			Pair<HGHandle, HGHandle> current = traversal.next();
			Hyperedge parent = (Hyperedge) graph.get(current.getFirst());
			Object atom = graph.get(current.getSecond());

			if (atom instanceof Relationship) {
				rels.add((Relationship) atom);
//				System.out.println("adding" + atom);
			}

			path.put(current.getSecond(), current.getFirst());
			if (atom instanceof Atom && atom.equals(atm)) {
				System.out.println(atom);
				breakable = true;
				param.setEmbeddingParent(parent);
			}
			// we found the atom no need to dig deeper
			if (breakable && atom instanceof Hyperedge) {
				break;
			}

//			    if(((Atom)atom).getName().equals("B_ID")) {
//			    	System.out.println("XXXXXXXXXX");
//			    	break;
//			    	}
//			System.out.println("Visiting atom " + atom + " pointed to by " + parent);
		}
//		System.out.println(rels);
		ArrayList<Relationship> finalPath = new ArrayList<>();
		HGHandle targetHandle = atomHAndle;
		if (!rels.isEmpty() && !targetHandle.equals(hyp.getRoot())) {
			do {
//				if () {
//					break;
//				}
				Pair<Relationship, HGHandle> relHandlePair = findNextHop(graph, targetHandle, rels);
//				System.out.println("OOOO  " + relHandlePair.getFirst());

				if (relHandlePair == null) {
//					System.out.println("YYYYYYY");
					break;
				}
				if (finalPath.contains(relHandlePair.getFirst())) {
//					System.out.println("SSSSSSSSSSSSSSSS");
					break;
				} else {
					finalPath.add(relHandlePair.getFirst());
//					System.out.println(relHandlePair.getSecond() + "  " + hyp.getRoot());
//					if (!((hyp.getType() == HyperedgeTypeEnum.Struct || hyp.getType() == HyperedgeTypeEnum.SecondLevel)
//							&& hyp.getRoot().equals(relHandlePair.getSecond()))) {
//					System.out.println();

					if (relHandlePair.getSecond().equals(hyp.getRoot())) {
						// MAYBE we have to check about the sets usability
						break;
					} else {
						targetHandle = relHandlePair.getSecond();
					}
//					}
//					}
				}
			} while (true);
		}
		param.setPath(finalPath);
	}

	private static Pair<Relationship, HGHandle> findNextHop(HyperGraph graph, HGHandle atomHAndle,
			ArrayList<Relationship> rels) {
		Pair<Relationship, HGHandle> relHandlePair = null;
		for (Relationship relationship : rels) {
//			System.out.println("ssss " + relationship);
//			System.out.println(atomHAndle);
//			System.out.println(relationship.getTargetAt(0)+"  " +relationship.getTargetAt(1));
//			System.out.println(relationship.getTargetAt(0));
			int target = -1;
			if (relationship.getTargetAt(0).equals(atomHAndle))
				target = 0;
			else if (relationship.getTargetAt(1).equals(atomHAndle))
				target = 1;
//			System.out.println(target);
			if (target != -1) {
				int other = target == 1 ? 0 : 1;
				Atom otherAtom = graph.get(relationship.getTargetAt(other));
				if (otherAtom.getType() == AtomTypeEnum.Class) {
					relHandlePair = new Pair(relationship, relationship.getTargetAt(other));
				}
//				System.out.println();

			}
		}
		return relHandlePair;
	}
}
