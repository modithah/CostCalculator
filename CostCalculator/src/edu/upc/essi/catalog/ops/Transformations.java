package edu.upc.essi.catalog.ops;

import java.lang.invoke.MethodHandles;
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

import org.hypergraphdb.*;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.algorithms.HGBreadthFirstTraversal;
import org.hypergraphdb.algorithms.HGDepthFirstTraversal;
import org.hypergraphdb.algorithms.SimpleALGenerator;
import org.hypergraphdb.util.Pair;

import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.core.constructs.opsparams.EmbedParams;
import edu.upc.essi.catalog.core.constructs.opsparams.GroupParams;
import edu.upc.essi.catalog.enums.AtomTypeEnum;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.util.TargetSetALGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Transformations {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    public static boolean union(HyperGraph graph, Hyperedge hyp1, Hyperedge hyp2) {

        // TODO condidions
//		Hyperedge x = graph.findOne(hg.eq(hyp1));
//        List<HGHandle> parents1 =  graph.findAll(hg.contains(graph.findOne(hg.eq(hyp1))));
       // ((Hyperedge)graph.findOne(hg.eq(hyp1))).getParents();

//        List<HGHandle> parents2 = graph.findAll(hg.contains(graph.findOne(hg.eq(hyp2))));
       // ((Hyperedge)graph.findOne(hg.eq(hyp2))).getParents();

        hyp1 = graph.get(graph.findOne(hg.and(hg.eq(hyp1),hg.eq("name",hyp1.getName()),hg.eq("type",hyp1.getType()))));
        hyp2 = graph.get(graph.findOne(hg.and(hg.eq(hyp2),hg.eq("name",hyp2.getName()),hg.eq("type",hyp2.getType()))));
//
        List<HGHandle> parents1= hyp1.getParents();
        List<HGHandle> parents2=hyp2.getParents();

//        List<HGHandle> parents1= graph.findAll(hg.contains(graph.findOne(hg.eq(hyp1))));
//        List<HGHandle> parents2=graph.findAll(hg.contains(graph.findOne(hg.eq(hyp2))));


//        System.out.println(hyp1);
//        System.out.println(hyp2);
        if (!((hyp1.getType() == hyp2.getType() && hyp2.getType() == HyperedgeTypeEnum.Set)
                || (hyp1.getType() == hyp2.getType() && hyp2.getType() == HyperedgeTypeEnum.FirstLevel))) {
            logger.error("Hyperedges must be sets");
            return false;
        }

        if (!(parents1.containsAll(parents2) && parents2.containsAll(parents1))) {
            logger.error("they should have the same parent");
            return false;
        }

        // Add children to the first hyperedge
        List<HGHandle> children = hyp2.findAll();
        List<HGHandle> remove = new ArrayList<>();

        for(HGHandle child : children){
            hyp1.add(child);
        }

        for (HGHandle child : remove) {
            hyp2.remove(child);
        }


        graph.update(hyp1);

        graph.remove(graph.getHandle(hyp2));

        return true;
    }

    public static ArrayList<Pair<Hyperedge, Hyperedge>> getUnionCandidates(HyperGraph graph) {
        ArrayList<Pair<Hyperedge, Hyperedge>> candidates = new ArrayList<>();
        Queue<Hyperedge> queue = new LinkedList<>();
        List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph);
//        makeHyperedgePairs(firstLevels, candidates);
        firstLevels.forEach(f -> {
            f.findAll().forEach(s -> {
                Element e = graph.get(s); // secondLevel
                if (e instanceof Hyperedge) {
                    queue.add((Hyperedge) e);
                }
            });
        });
//		logger.info("-----------");
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
//        candidates = new ArrayList<>();
        return candidates;
    }

    private static void makeHyperedgePairs(List<Hyperedge> hyperedges, ArrayList<Pair<Hyperedge, Hyperedge>> pairs) {
        if (hyperedges.size() >= 2) {

            Set<Set<Hyperedge>> combos = Sets.combinations(ImmutableSet.copyOf(hyperedges), 2);
            Iterator<Set<Hyperedge>> comboIterator = combos.iterator();
            while (comboIterator.hasNext()) {
                Set<Hyperedge> set = (Set<Hyperedge>) comboIterator.next();
                Object[] arr = set.toArray();
//			logger.info(arr[0] + "---" + arr[1]);
                pairs.add(new Pair<Hyperedge, Hyperedge>((Hyperedge) arr[0], (Hyperedge) arr[1]));
            }
        }
    }

    public static boolean flatten(HyperGraph graph, Hyperedge hyp) {
        hyp = graph.get(graph.findOne(hg.and(hg.eq(hyp),hg.eq("name",hyp.getName()),hg.eq("type",hyp.getType()))));
        HGHandle hypHandle = graph.getHandle(hyp);
        HGHandle parentHandle =  hyp.getParents().get(0);//graph.findAll(hg.contains(hypHandle)).get(0); //
        Hyperedge parent = graph.get(parentHandle);

        if (!(parent.getType() == HyperedgeTypeEnum.Struct || parent.getType() == HyperedgeTypeEnum.SecondLevel)) {
            logger.error("parent must be a struct to flatten");
            return false;
        }

        // Add children to the first hyperedge

        List<HGHandle> children = hyp.findAll();
        List<HGHandle> remove = new ArrayList<>();

        for(HGHandle child : children){
            parent.add(child);
            remove.add(child);
        }

        for (HGHandle child : remove) {
            hyp.remove(child);
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

//        logger.info("Grouping graph" + graph.getLocation());
//        logger.info(param);

        Hyperedge hyp = param.getHyp();
//        logger.info(hyp);
        hyp = graph.get(graph.findOne(hg.and(hg.eq(hyp), hg.eq("name", hyp.getName()), hg.eq("type", hyp.getType()))));
//        logger.info("after");
//        logger.info(hyp);
        ArrayList<Relationship> rels = new ArrayList<>();
        param.getRels().forEach(r -> {
//            logger.info(r);
//            logger.info();
            rels.add(graph.get(graph.findOne(hg.and(hg.type(Relationship.class),hg.eq("IRI", r.getIRI())))));
        });

        Element elm = param.getElm();
        List<HGHandle> found = graph.findAll(hg.eq(elm));
        if(found.size()>1){
            logger.error("Longer than one");
            elm = graph.get(found.get(0));
        }
        else {
            elm = graph.get(found.get(0));
        }

        if (!(hyp.getType() == HyperedgeTypeEnum.Struct || hyp.getType() == HyperedgeTypeEnum.SecondLevel)) {
//            logger.info(String.valueOf(hyp.getType()));
            logger.error("Group can be only inside a struct");
            return false;
        } else {
            ArrayList<HGHandle> relHandles = new ArrayList<>();
            HGHandle elementHandle = graph.getHandle(elm);
            rels.forEach(r -> {
                relHandles.add(graph.getHandle(r));
            });
            List<HGHandle> parentContents = hyp.findAll();
            if (!parentContents.containsAll(relHandles) || !parentContents.contains(elementHandle)) {
//                logger.info(String.valueOf(parentContents.containsAll(relHandles)));
//                logger.info(String.valueOf(parentContents.contains(elementHandle)));
                logger.error("relationships and element should be inside the parent");
                return false;
            } else {
                if (elm instanceof Relationship) {
                    logger.error("element cannot be a relationship");
                    return false;
                } else if (elm instanceof Hyperedge && ((Hyperedge) elm).getType() != HyperedgeTypeEnum.Struct) {
                    logger.error("hyperedge element must be a struct");
                    return false;
                } else {
                    try {
//                        logger.info("hype is " + hyp);
                        HGHandle newSet = Graphoperations.addSetHyperedgetoGraph(graph, elm.getName() + "Set", rels,
                                elementHandle);
//						logger.info((Hyperedge) graph.get(newSet));
//                        ((Hyperedge) graph.get(newSet)).print(0);
//						newSet=null;
                        hyp.remove(elementHandle);
//						logger.info("removing  "+ elementHandle);
                        Set<Relationship> used = findUsedRelationships(graph, hyp, newSet);

//						logger.info(used.size());
                        rels.removeAll(used);
//						logger.info("rels   " + rels.size());
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
//        logger.info("FFFFFFFFFF \n" + parentContents);
        parentContents.forEach(hdl -> {
            if (!hdl.equals(except)) {
//				logger.info(hdl);

                Element e = graph.get(hdl);
//								logger.info("path to" + e.getName());
//                logger.info(e.toString());
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
            } else {
                // TODO: making sets with only atoms ??
            }
        }

        return candidates;
    }

    public static boolean segregate(HyperGraph graph, Hyperedge set, Element el) {
        set = graph.get(graph.findOne(hg.and(hg.eq(set),hg.eq("name",set.getName()),hg.eq("type",set.getType()))));



        List<HGHandle> found = graph.findAll(hg.eq(el));
        if(found.size()>1){
                logger.error("Longer than one");
        }

            el = graph.get(found.get(0));

//        el = graph.get(graph.findOne(hg.eq(el)));

        if (!(set.getType() == HyperedgeTypeEnum.Set || set.getType() == HyperedgeTypeEnum.FirstLevel)) {
            logger.error("Segregate can be only inside a Set");
            return false;
        } else {
            HGHandle elementHandle = graph.getHandle(el);
            HGHandle setHandle = graph.getHandle(set);
            if (!set.isMember(elementHandle)) {
                logger.error("Element must be inside the Set");
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
                                } else if (child instanceof Hyperedge) {
                                    HGHandle rootofStruct = ((Hyperedge) child).getRoot();
                                    used.addAll(findRelPath(graph, grandParent, graph.get(rootofStruct)));
                                }
                                else if (child instanceof Relationship) {
                                    used.add((Relationship) child);
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
//		logger.info("-----------");
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
        child = graph.get(graph.findOne(hg.and(hg.eq(child),hg.eq("name",child.getName()),hg.eq("type",child.getType()))));




        HGHandle childHandle = graph.getHandle(child);
        Hyperedge grandParent = param.getGrandParent();
        grandParent = graph.get(graph.findOne(hg.and(hg.eq(grandParent),hg.eq("name",grandParent.getName()),hg.eq("type",grandParent.getType()))));
        Hyperedge newParent = param.getParent();
        newParent = graph.get(graph.findOne(hg.and(hg.eq(newParent),hg.eq("name",newParent.getName()),hg.eq("type",newParent.getType()))));
        Set<Relationship> used = findUsedRelationships(graph, grandParent, childHandle);
//		logger.info("XXXXXXXXXXXXXXXXXXXXX");
//		logger.info(param.getChild());
//		logger.info(graph.get(param.getChild().getRoot()).toString());
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

    public static Pair<Double, Double> getDepthMinMax(HyperGraph graph) {
        List<Atom> classes = Graphoperations.getClassAtomList(graph);

        return new Pair<Double, Double>(1.0, (double) classes.size());
    }

    public static Pair<Double, Double> getHeterogenietyMinMax(HyperGraph graph) {
        List<Atom> classes = Graphoperations.getClassAtomList(graph);

        return new Pair<Double, Double>(1.0, (double) classes.size());
    }

    public static Pair<Double, Double> getCostMinMax(HyperGraph graph,
                                                     ArrayList<Pair<Double, ArrayList<Atom>>> workload) {
        double cost = 0.0;

        for (Pair<Double, ArrayList<Atom>> pair : workload) {
            ArrayList<Atom> atomlist = pair.getSecond();
            Atom entry = atomlist.get(0);
            HashMap<Atom, Double> map = new HashMap<Atom, Double>();
            map.put(entry, 1.0);

            HGDepthFirstTraversal traversal = new HGDepthFirstTraversal(graph.getHandle(entry),
                    new SimpleALGenerator(graph));

            while (traversal.hasNext() && !atomlist.isEmpty()) {
                Pair<HGHandle, HGHandle> current = traversal.next();
                Relationship l = (Relationship) graph.get(current.getFirst());
                Atom atom = graph.get(current.getSecond());
                if (atom.getType() == AtomTypeEnum.Class) {
//				    	l.getMultiplicities();
                    ArrayList<String> relorder = new ArrayList<>();
                    Atom atom1 = ((Atom) graph.get(l.getTargetAt(0)));
                    Atom atom2 = ((Atom) graph.get(l.getTargetAt(1)));
                    relorder.add(atom1.getName());
                    relorder.add(atom2.getName());
                    Collections.sort(relorder);
                    int index = relorder.indexOf(atom.getName());
                    Atom parentAtom = atom1 == atom ? atom2 : atom1;
//						logger.info(parentAtom+"  "+map.get(parentAtom));
                    double val = l.getMultiplicities()[index] * map.get(parentAtom);
                    cost += val;
                    map.put(atom, val);
//						logger.info(cost);
                }
                atomlist.remove(atom);
            }
        }

        return new Pair<Double, Double>(0.0, cost);

    }

    private static void executeStructCombinations(HyperGraph graph, ArrayList<EmbedParams> candidates,
                                                  ArrayList<Hyperedge> tmpCandidates, Hyperedge grandParent) {
        if (tmpCandidates.size() >= 2) {
//            logger.info(tmpCandidates.toString());
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
//				logger.info("adding" + atom);
            }

            path.put(current.getSecond(), current.getFirst());
            if (atom instanceof Atom && atom.equals(atm)) {
                logger.info(atom.toString());
                breakable = true;
            }
            // we found the atom no need to dig deeper
            if (breakable && atom instanceof Hyperedge) {
                break;
            }

//			    if(((Atom)atom).getName().equals("B_ID")) {
//			    	logger.info("XXXXXXXXXX");
//			    	break;
//			    	}
//			logger.info("Visiting atom " + atom + " pointed to by " + parent);
        }
//		logger.info(rels);
        ArrayList<Relationship> finalPath = new ArrayList<>();
        HGHandle targetHandle = atomHAndle;
        if (!rels.isEmpty() && !targetHandle.equals(hyp.getRoot())) {
            do {
//				if () {
//					break;
//				}
                Pair<Relationship, HGHandle> relHandlePair = findNextHop(graph, targetHandle, rels);
//				logger.info("OOOO  " + relHandlePair.getFirst());

                if (relHandlePair == null) {
//					logger.info("YYYYYYY");
                    break;
                }
                if (finalPath.contains(relHandlePair.getFirst())) {
//					logger.info("SSSSSSSSSSSSSSSS");
                    break;
                } else {
                    finalPath.add(relHandlePair.getFirst());
//					logger.info(relHandlePair.getSecond() + "  " + hyp.getRoot());
//					if (!((hyp.getType() == HyperedgeTypeEnum.Struct || hyp.getType() == HyperedgeTypeEnum.SecondLevel)
//							&& hyp.getRoot().equals(relHandlePair.getSecond()))) {
//					logger.info();

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
//				logger.info("adding" + atom);
            }

            path.put(current.getSecond(), current.getFirst());
            if (atom instanceof Atom && atom.equals(atm)) {
//                logger.info(atom.toString());
                breakable = true;
                param.setEmbeddingParent(parent);
            }
            // we found the atom no need to dig deeper
            if (breakable && atom instanceof Hyperedge) {
                break;
            }

//			    if(((Atom)atom).getName().equals("B_ID")) {
//			    	logger.info("XXXXXXXXXX");
//			    	break;
//			    	}
//			logger.info("Visiting atom " + atom + " pointed to by " + parent);
        }
//		logger.info(rels);
        ArrayList<Relationship> finalPath = new ArrayList<>();
        HGHandle targetHandle = atomHAndle;
        if (!rels.isEmpty() && !targetHandle.equals(hyp.getRoot())) {
            do {
//				if () {
//					break;
//				}
                Pair<Relationship, HGHandle> relHandlePair = findNextHop(graph, targetHandle, rels);
//				logger.info("OOOO  " + relHandlePair.getFirst());

                if (relHandlePair == null) {
//					logger.info("YYYYYYY");
                    break;
                }
                if (finalPath.contains(relHandlePair.getFirst())) {
//					logger.info("SSSSSSSSSSSSSSSS");
                    break;
                } else {
                    finalPath.add(relHandlePair.getFirst());
//					logger.info(relHandlePair.getSecond() + "  " + hyp.getRoot());
//					if (!((hyp.getType() == HyperedgeTypeEnum.Struct || hyp.getType() == HyperedgeTypeEnum.SecondLevel)
//							&& hyp.getRoot().equals(relHandlePair.getSecond()))) {
//					logger.info();

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
//			logger.info("ssss " + relationship);
//			logger.info(atomHAndle);
//			logger.info(relationship.getTargetAt(0)+"  " +relationship.getTargetAt(1));
//			logger.info(relationship.getTargetAt(0));
            int target = -1;
            if (relationship.getTargetAt(0).equals(atomHAndle))
                target = 0;
            else if (relationship.getTargetAt(1).equals(atomHAndle))
                target = 1;
//			logger.info(target);
            if (target != -1) {
                int other = target == 1 ? 0 : 1;
                Atom otherAtom = graph.get(relationship.getTargetAt(other));
                if (otherAtom.getType() == AtomTypeEnum.Class) {
                    relHandlePair = new Pair(relationship, relationship.getTargetAt(other));
                }
//				logger.info();

            }
        }
        return relHandlePair;
    }
}
