package edu.upc.essi.catalog.ops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSet;
import org.hypergraphdb.atom.HGAtomSet;
import org.hypergraphdb.util.Pair;

import com.c05mic.generictree.Node;
import com.c05mic.generictree.Tree;

import org.hypergraphdb.HGQuery.hg;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.RelStructure;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.AtomTypeEnum;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.enums.OperationTypeEnum;

public final class CostOperations {

    public static double CalculateCounts(HGHandle hyperedgehandle) {

//		2("Handle is sss" + hyperedgehandle);

        HyperGraph graph = Const.graph;
//		IncidenceSet incidence = graph.getIncidenceSet(hyperedgehandle);

//        HGSearchResult<Object> incidence = graph.find(hg.contains(hyperedgehandle));

        
        
        Hyperedge hyperedge = graph.get(hyperedgehandle);

        Iterator<HGHandle> incidence = hyperedge.getParents().iterator();
        
//		logger.info(hyperedge);
        double count = 0;
        Iterator<HGHandle> iter = hyperedge.findAll().iterator();// findall(hg.anyHandle());
        HGHandle parentAtomHandle, childAtomHandle = null;
        String childName = "";
        HashMap<Atom, HGHandle> candidateAtoms = new HashMap<>();
        ArrayList<HGHandle> relationships = new ArrayList<>();

        // go throuhg all the second level/ structs (ideally it should be one)
        while (iter.hasNext()) {
            HGHandle hgHandle = iter.next();
            Hyperedge secondlevel = graph.get(hgHandle); // got the struct/second level
//			logger.info("XXXXXXXXXX" + secondlevel);
            Iterator<HGHandle> seconditer = secondlevel.findAll().iterator();
            while (seconditer.hasNext()) {
                HGHandle hgHandle2 = (HGHandle) seconditer.next();

                Object a = graph.get(hgHandle2);

                if (a.getClass().equals(Atom.class) && ((Atom) a).getType() == AtomTypeEnum.Class) {

                    candidateAtoms.put((Atom) a, hgHandle2);
//					logger.info("child " + ((Atom) a).getName());
                }

                if (a.getClass().equals(Relationship.class)) {
                    relationships.add(hgHandle2);
                }
            }

            ArrayList<Atom> atoms = new ArrayList<>();
            candidateAtoms.keySet().iterator().forEachRemaining(atoms::add);

            if (atoms.size() == 1) {
//				logger.info("OOOOOOOONEEEEEEEEE");
                count = (atoms.get(0)).getCount(); // find the class atom
                childAtomHandle = candidateAtoms.get(atoms.get(0));
                childName = atoms.get(0).getName();
            } else {
//				logger.info("MMMMMMMMMMMMMMMMMOOOOOOOOOOOORRRRRREEEEEEEEEE");
                for (int i = 0; i < atoms.size(); i++) {
                    ArrayList<Boolean> evals = new ArrayList<>();
                    Atom atom1 = atoms.get(i);
                    for (int j = 0; j < atoms.size(); j++) {
                        if (i != j) {
                            Atom atom2 = atoms.get(j);
                            HGHandle rel = Graphoperations.getRelationshipByNameAtoms(graph, "has" + atom2.getName(),
                                    candidateAtoms.get(atom1), candidateAtoms.get(atom2));
//							logger.info("sssss" + rel);
                            if (rel != null && relationships.contains(rel)) {
                                evals.add(true);
                            } else
                                break;
                        }
                    }
                    if (evals.size() == atoms.size() - 1) {
                        if (!evals.contains(false)) {
                            count = (atom1).getCount(); // find the root atom
                            childAtomHandle = candidateAtoms.get(atom1);
                            childName = atom1.getName();
                        }
                    }
                }
            }
        }

//		logger.info("UUUUUUUUUUUUUUUUUUUUUUUUUUUUU" + hyperedge.getType());
        switch (hyperedge.getType()) {

            // In first level it contains a second level that has a class that has the count
            case FirstLevel:

//			logger.info("setting count  " + count);
//			hyperedge.setCount(count);

                break;

            // in set it has a struct that has a class atom with count
            case Set:
                // need to find the parent struct / second level

//			for (HGHandle hgHandle : incidence) {

                while (incidence.hasNext()) {
                    // print here to see the parents

                    Object hyper = graph.get((HGHandle) incidence.next());
                    if (hyper.getClass().equals(Hyperedge.class)
                            && (((Hyperedge) hyper).getType() == HyperedgeTypeEnum.SecondLevel || // found the second level
                            // or
                            // struct parent
                            ((Hyperedge) hyper).getType() == HyperedgeTypeEnum.Struct)) {
                        // check the class atom of the parent
                        Hyperedge secondlevel = (Hyperedge) hyper;
//					logger.info("LLLLLLLLLLLL" + hyper);
                        Iterator<HGHandle> iteratorOfParent = secondlevel.findAll().iterator();
                        while (iteratorOfParent.hasNext()) {
                            HGHandle hgHandle2 = (HGHandle) iteratorOfParent.next();
                            Object atomobject = graph.get(hgHandle2);
                            // this is to find the atom
                            if (atomobject.getClass().equals(Atom.class)
                                    && ((Atom) atomobject).getType() == AtomTypeEnum.Class) {
//							logger.info("---------------------------------" + ((Atom) atomobject).getName());
                                HGHandle rel = Graphoperations.getRelationshipByNameAtoms(graph, "has" + childName,
                                        hgHandle2, childAtomHandle);

                                Relationship relationship = graph.get(rel);

//							logger.info("SSSSSSSSSSSSSSSSSSSSSSSSSSS         " + relationship.getMultiplicity());
//									hg.getOne(graph,
//									hg.and(hg.type(Relationship.class), hg.orderedLink(hgHandle2, childAtomHandle))); // find
                                // the

                                count = relationship.getMultiplicity();
                                hyperedge.setCount(count);
                            }
                        }
                    }
                }

                break;
            default:
                break;
        }
        graph.update(hyperedge);
        return count;
    }

    public static double CalculateCounts(HGHandle hyperedgehandle, HGHandle childHandle) {

        double multiplier = 1.0;
        HyperGraph graph = Const.graph;
        Hyperedge hyperedge = graph.get(hyperedgehandle);
        if (hyperedge.getType() == HyperedgeTypeEnum.SecondLevel) {
            multiplier = ((Atom) graph.get(hyperedge.getRoot())).getCount();
        } else if (hyperedge.getType() == HyperedgeTypeEnum.Set) {
            HGHandle parent = hyperedge.getIncidenceSet().first(); // Set will always have one parent
            Hyperedge parentHyperedge = graph.get(parent);
            Hyperedge childhyperedge = graph.get(childHandle);
            Relationship relationship = (Relationship) parentHyperedge
                    .findOne(hg.and(hg.orderedLink(hg.anyHandle(), childhyperedge.getRoot())));
            multiplier = relationship.getMultiplicity();
        }

        return multiplier;
    }

    public static Pair<Double, HashMap<Atom, Double>> CalculateSize(HyperGraph graph, Element e) {
//        logger.info("--"+e.toString());
        double size = 0.0;
        HashMap<Atom, Double> map = new HashMap<>();
        if (e instanceof Atom) {
            size = ((Atom) e).getSize();
            map.put((Atom) e, 1.0);
        } else if (e instanceof Hyperedge) {
            switch (((Hyperedge) e).getType()) {
                case FirstLevel:
                    break;
                case SecondLevel:
                    break;
                case Set:
                    HGHandle parentHandle = e.getParents().get(0);
//                            Graphoperations.getParentHyperedges(graph, graph.getHandle(e)).get(0);
                    Hyperedge parent = ((Hyperedge) graph.get(parentHandle));
                    size = ((Hyperedge) e).getName().length()
                            * CalculateMultiplier(graph, parent, graph.get(parent.getRoot()));
                    break;
                case Struct:
//				logger.info(Graphoperations.getParentHyperedges(graph, graph.getHandle(e)));
                    List<HGHandle> parents = e.getParents();

                    if (((Hyperedge) graph.get(parents.get(0)))
                            .getType() == HyperedgeTypeEnum.Set) { // embedded inside a set (always have 1 parent)
                        // name is added by the parent set
                    } else { // embedded set
                        HGHandle pHandle = e.getParents().get(0);
//                                Graphoperations.getParentHyperedges(graph, graph.getHandle(e)).get(0);
                        Hyperedge p = ((Hyperedge) graph.get(pHandle));
                        size = ((Hyperedge) e).getName().length() * CalculateMultiplier(graph, p, graph.get(p.getRoot()));
                    }
                    break;
                default:
                    break;
            }
//			logger.info("OOOOOOOOO" + e);
            for (HGHandle childHandle : ((Hyperedge) e).findAll()) {
                Element child = graph.get(childHandle);
//				logger.info("Child is ---" + child);
                double multiplier = CalculateMultiplier(graph, (Hyperedge) e, child);
                Pair<Double, HashMap<Atom, Double>> result = CalculateSize(graph, child);
//				logger.info("size before" + size);
                size = size + result.getFirst() * multiplier;
//				logger.info(child + "          " + result.getFirst());
//				logger.info("size            " + size);
//				logger.info(e + "multiplXXXX         " + multiplier);
//				logger.info("existing = "+map);logger.info(e+"multipl         "+multiplier);
//				logger.info("existing = " + map);
//				logger.info("new = " + result.getSecond());


                // TODO: hetetogeneous collections
//				if (((Hyperedge) e).getType() == HyperedgeTypeEnum.FirstLevel) {
//					for (Atom key : result.getSecond().keySet()) {
//						logger.info("putting  " + key + " before " + map.get(key) + "getting "
//								+ result.getSecond().get(key));
//					}
//					logger.info("Size is " +size);
//				}

                for (Atom key : result.getSecond().keySet()) {
                    map.put(key, result.getSecond().get(key) * multiplier);
                }

            }
        }
        Pair<Double, HashMap<Atom, Double>> p = new Pair<Double, HashMap<Atom, Double>>(size, map);
        return p;
    }

    private static double CalculateMultiplier(HyperGraph graph, Hyperedge e, Element child) {
        double multiplier = 1.0;

//		logger.info(e + "----->" + child);
        if (!(child instanceof Relationship)) {

            if (e.getType() == HyperedgeTypeEnum.SecondLevel && child instanceof Atom) {
                Atom root = (Atom) graph.get(e.getRoot());
                List<HGHandle> attributes = Graphoperations.getAttributesClass(graph, e.getRoot());
                if (attributes.contains(graph.getHandle(child)) || root == child) {
//                    logger.info("FFFFFFFFFFFFF");

                    multiplier = root.getCount();
                } else { // find the path to the root
//logger.info("OOOOOOOOOOOOOOOOOOOOOO" + e.getName());
                    ArrayList<RelStructure> relationships = makeRelStructures(graph, e);
//					logger.info(relationships);
                    Tree<RelStructure> tree = makeTree(graph, e, relationships);
//                    ArrayList<Node<RelStructure>> y = tree.getPreOrderTraversal();
//                    y.forEach(x->{
//                        logger.info(x.getData().from+"-->"+x.getData().to);
//                    });

                    multiplier = getMultiplierForLeaf(graph, child, tree) * root.getCount();
                }

            } else if (e.getType() == HyperedgeTypeEnum.Set) {
                Hyperedge parentStruct = ((Hyperedge) graph
                        .get(Graphoperations.getParentHyperedges(graph, graph.getHandle(e)).get(0)));
//				logger.info("making for" + parentStruct);
                ArrayList<RelStructure> parentRels = makeRelStructures(graph, parentStruct);

//				logger.info(parentRels);

                Tree<RelStructure> tree = makeTree(graph, parentStruct, parentRels);
                ArrayList<RelStructure> setRels = new ArrayList<>();
                e.getAll(hg.type(Relationship.class)).stream().forEach(o -> {
                    setRels.add(getEmptyRelStructForRelationship(graph, (Relationship) o));
                });

                addRelationshipToTree(setRels, tree);
                multiplier = getMultiplierForLeaf(graph, child, tree);
                if (parentStruct.getType() == HyperedgeTypeEnum.SecondLevel) {
                    multiplier = multiplier * ((Atom) graph.get(parentStruct.getRoot())).getCount();
                }

            } else if ((e.getType() == HyperedgeTypeEnum.Struct || e.getType() == HyperedgeTypeEnum.SecondLevel)
                    && child instanceof Hyperedge && ((Hyperedge) child).getType() == HyperedgeTypeEnum.Struct) {

                ArrayList<RelStructure> relationships = makeRelStructures(graph, e);
                Tree<RelStructure> tree = makeTree(graph, e, relationships);

                multiplier = getMultiplierForLeaf(graph, child, tree);

                if (e.getType() == HyperedgeTypeEnum.SecondLevel) {
                    multiplier = multiplier * ((Atom) graph.get(e.getRoot())).getCount();
                }

//				logger.info("MMMMMMMMMM" + multiplier);
            }

//			else if ((e.getType() == HyperedgeTypeEnum.Struct || e.getType() == HyperedgeTypeEnum.SecondLevel) && child instanceof Hyperedge
//					&& ((Hyperedge) child).getType() == HyperedgeTypeEnum.Set) {
//				logger.info("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
//				multiplier =
//			}
        }
        return multiplier;
    }

    private static double getMultiplierForLeaf(HyperGraph graph, Element leafChild, Tree<RelStructure> tree) {
        double multiplier = 1.0;
//        logger.info("#####" + leafChild.getName());
        ArrayList<ArrayList<Node<RelStructure>>> paths = tree.getPathsFromRootToAnyLeaf();
        boolean pathFound = false;
        Atom leafAtom = leafChild instanceof Hyperedge ? graph.get(((Hyperedge) leafChild).getRoot())
                : (Atom) leafChild;
        for (ArrayList<Node<RelStructure>> path : paths) {
            if (path.get(path.size() - 1).getData().to == leafAtom) {
                pathFound = true;
                for (Node<RelStructure> node : path) {
//                    logger.info(node.getData().from + "-->" + node.getData().to + "  " + node.getData().mult);
                    HashMap<Atom, Double> map = node.getData().mult;
                    if (map != null) {
                        multiplier = multiplier * map.get(node.getData().to);
                    }
                }
            }
        }
        if (!pathFound) {
            for (ArrayList<Node<RelStructure>> path : paths) {
                for (Node<RelStructure> node : path) {
                    if (node.getData().to == leafAtom) {
                        pathFound = true;
                    }
                    if (pathFound) {
                        HashMap<Atom, Double> map = node.getData().mult;
                        if (map != null) {
                            multiplier = multiplier * map.get(node.getData().to);
                        }
                    }
                }
                if (pathFound)
                    break;

            }
        }
        return multiplier;
    }

    private static ArrayList<RelStructure> makeRelStructures(HyperGraph graph, Hyperedge hyperedge) {
//		=graph.g
        hyperedge = graph.get(graph.findOne(hg.eq(hyperedge)));
//		hyperedge.print(0);
        List<HGHandle> allRelationships = hyperedge.findAll(hg.type(Relationship.class));

        Iterator<HGHandle> seconditer = hyperedge.findAll(hg.type(Relationship.class)).iterator();
        ArrayList<RelStructure> relationships = new ArrayList<RelStructure>();
        while (seconditer.hasNext()) {
            HGHandle hgHandle2 = (HGHandle) seconditer.next();
            Relationship r = graph.get(hgHandle2);
//			logger.info("XXXXXXXXXXX" + r);
            Atom elm1 = graph.get(r.getTargetAt(0));
            Atom elm2 = graph.get(r.getTargetAt(1));

            if (elm1.getType() == AtomTypeEnum.Class && elm2.getType() == AtomTypeEnum.Class) {
//				logger.info(elm1+"----"+elm2);
                RelStructure st = getEmptyRelStructForRelationship(graph, r);
                relationships.add(st);
//				logger.info(relationships);
            }
        }


//		allRelationships.stream().forEach(x -> {
//			Relationship r=graph.get(x);
//			logger.info(r.getName());
//			Atom elm1 = graph.get(r.getTargetAt(0));
//			Atom elm2 = graph.get(r.getTargetAt(1));
//			if (elm1.getType() == AtomTypeEnum.Class && elm2.getType() == AtomTypeEnum.Class) {
//				RelStructure st = getEmptyRelStructForRelationship(graph, r);
//				relationships.add(st);
//			}
//		});
        return relationships;
    }

    private static RelStructure getEmptyRelStructForRelationship(HyperGraph graph, Relationship r) {
        Atom elm1 = graph.get(r.getTargetAt(0));
        Atom elm2 = graph.get(r.getTargetAt(1));

        ArrayList<String> relorder = new ArrayList<>();
        relorder.add(elm1.getName());
        relorder.add(elm2.getName());
        Collections.sort(relorder);
        HashMap<Atom, Double> map = new HashMap<>();
        map.put(elm1, r.getMultiplicities()[relorder.indexOf(elm1.getName())]);
        map.put(elm2, r.getMultiplicities()[relorder.indexOf(elm2.getName())]);

        RelStructure st = new RelStructure(r, null);
        st.mult = map;
//		logger.info("----------------------------------------------");
//		logger.info(map);
        return st;
    }

    private static Tree<RelStructure> makeTree(HyperGraph graph, Hyperedge parent,
                                               ArrayList<RelStructure> relationships) {
//		logger.info("makeTree");
        Node<RelStructure> root = new Node<RelStructure>(
                new RelStructure(null, null, null, graph.get(parent.getRoot())));
        Tree<RelStructure> tree = new Tree<>(root);

        addRelationshipToTree(relationships, tree);
        return tree;
    }

    private static void addRelationshipToTree(ArrayList<RelStructure> relationships, Tree<RelStructure> tree) {
//		logger.info(tree.getRoot().getData().to);
//		logger.info(relationships.size());
        while (!relationships.isEmpty()) {

            RelStructure rel = relationships.remove(0);
            boolean found = false;
            for (Node<RelStructure> node : tree.getPostOrderTraversal()) {
//				logger.info(node.getData().to);
//				logger.info(rel.mult.keySet().contains(node.getData().to));
                if (rel.mult.keySet().contains(node.getData().to)) {
                    rel.from = node.getData().to;
                    for (Atom a : rel.mult.keySet()) {
                        if (a != node.getData().to) {
                            rel.to = a;
                        }
                    }
                    node.addChild(new Node<RelStructure>(rel));
                    found = true;
//					logger.info("added " + relationships.size());
//					break;
                } else {

                }
            }
            if (!found) {
                relationships.add(rel);
            }
        }
    }

}
