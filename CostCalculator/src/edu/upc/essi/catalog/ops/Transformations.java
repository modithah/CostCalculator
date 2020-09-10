package edu.upc.essi.catalog.ops;

import java.util.ArrayList;
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
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.algorithms.HGBreadthFirstTraversal;
import org.hypergraphdb.util.Pair;

import com.github.andrewoma.dexx.collection.HashMap;

import org.hypergraphdb.HyperGraph;

import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
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

	public static boolean group(HyperGraph graph, Hyperedge hyp, ArrayList<Relationship> rels, Element elm) {

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
			if (!parentContents.containsAll(relHandles) || parentContents.contains(elementHandle)) {
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
						HGHandle newSet = Graphoperations.addSetHyperedgetoGraph(graph, elm.getName() + "Set", rels,
								elementHandle);
						hyp.remove(elementHandle);
						Set<Relationship> used = new HashSet();
						parentContents.forEach(hdl -> {
							Object e = graph.get(hdl);
							if (e instanceof Atom) {
								used.addAll(findRelPath(graph, hyp, (Atom) e));
							} else if (e instanceof Hyperedge) {
								Hyperedge edge = (Hyperedge) e;
								if (edge.getType() == HyperedgeTypeEnum.Struct) {
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
						});

						rels.removeAll(used);
						for (Relationship relationship : used) {
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
		if (!rels.isEmpty()) {
			do {
				Pair<Relationship, HGHandle> relHandlePair = findNextHop(graph, targetHandle, rels);
				if (relHandlePair == null)
					break;
				if (finalPath.contains(relHandlePair.getFirst())) {
					break;
				} else {
					finalPath.add(relHandlePair.getFirst());
					targetHandle = relHandlePair.getSecond();
				}
			} while (true);
		}
		return finalPath;
	}

	private static Pair<Relationship, HGHandle> findNextHop(HyperGraph graph, HGHandle atomHAndle,
			ArrayList<Relationship> rels) {
		Pair<Relationship, HGHandle> relHandlePair = null;
		for (Relationship relationship : rels) {
			int target = -1;
			if (relationship.getTargetAt(0) == atomHAndle)
				target = 0;
			else if (relationship.getTargetAt(1) == atomHAndle)
				target = 1;
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
