package edu.upc.essi.catalog.ops;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGQuery.hg;

import com.google.common.collect.Table;

import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.AdjacencyList;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.AtomTypeEnum;
import edu.upc.essi.catalog.enums.CardinalityEnum;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;

public final class Graphoperations {

//	static HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//
//	static {
//		graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//	}
private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    public Graphoperations() {
        // TODO Auto-generated constructor stub
//		graph = new HyperGraph(Const.HG_LOCATION_BOOK);
    }

    /**
     * Returns all hyperedges of a specific type
     *
     * @param type
     * @return
     */
    public static Hyperedge getDBHyperedgebyType(HyperGraph graph, HyperedgeTypeEnum type) {
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
        List<Object> r = hg.getAll(graph, hg.and(hg.type(Hyperedge.class), hg.eq("type", type)));
//		graph.close();
        return (Hyperedge) r.get(0);
    }


//	public static Element getElementbyHandle(HGHandle handle) {
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//		Element el = graph.get(handle);
////		graph.close();
//		return el;
//	}

    public static HyperGraph makeGraphCopy(HyperGraph source) {
        //String source = Const.HG_LOCATION_BOOK;
        File srcDir = new File(source.getStore().getDatabaseLocation());
        //String destination = Const.HG_LOCATION_BASE + foldername;
        File destDir = new File(source.getStore().getDatabaseLocation() + File.separator + UUID.randomUUID().toString());
        try {
            //logger.info(destDir);
//			source.close();
            for (File file : srcDir.listFiles()) {
                if(!file.isDirectory()){
                    FileUtils.copyToDirectory(file,destDir);
                }
            }
//            FileUtils.copyDirectory(srcDir, destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HyperGraph(destDir.getAbsolutePath());
    }

    public static HyperGraph revertGraph(HyperGraph graph, String foldername) {
        HyperGraph returnGraph = null;
        graph.close();
        logger.info(String.valueOf(graph.isOpen()));
        String source = Const.HG_LOCATION_BASE + foldername;
        File srcDir = new File(source);

        String destination = Const.HG_LOCATION_BOOK;

        File destDir = new File(destination);

        try {
            FileUtils.cleanDirectory(destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileUtils.copyDirectory(srcDir, destDir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        returnGraph = new HyperGraph(destination);
        return returnGraph;
    }

    /**
     * Legacy code for a single atom
     *
     * @param atomName
     * @return
     */
    public static AdjacencyList makeHashmap(String atomName) {
        AdjacencyList l = new AdjacencyList();
        HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
        HGHandle atom = hg.findOne(graph, hg.and(hg.type(Atom.class), hg.eq("name", atomName)));

        IncidenceSet incidence = graph.getIncidenceSet(atom);

        PriorityQueue<Pair<HGHandle, HGHandle>> q = new PriorityQueue<Pair<HGHandle, HGHandle>>();

        for (HGHandle hgHandle : incidence) {
            q.add(Pair.of(hgHandle, atom));
        }

        while (!q.isEmpty()) {
            Pair<HGHandle, HGHandle> tmp = q.poll();
            Object parent = graph.get(tmp.getLeft());
            Object child = graph.get(tmp.getRight());
            if (parent instanceof Hyperedge) {
                l.AddToSet((Element) parent, (Element) child);
                for (HGHandle hgHandle : graph.getIncidenceSet(tmp.getLeft())) {
                    q.add(Pair.of(hgHandle, tmp.getLeft()));
                }
            }
        }
        graph.close();
        return l;
    }

    /**
     * Algorithm 1 in the paper
     *
     * @param atomNames - queried atoms
     * @return adjacency list of hyperedges corresponding to the queries
     */
    public static AdjacencyList makeHashmap(String... atomNames) {
        AdjacencyList l = new AdjacencyList();
        HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
        PriorityQueue<Pair<HGHandle, HGHandle>> q = new PriorityQueue<Pair<HGHandle, HGHandle>>();

//		logger.info(graph);
        for (String atomName : atomNames) {

            HGHandle atom = hg.findOne(graph, hg.and(hg.type(Atom.class), hg.eq("name", atomName)));
            if (atom != null) {
//				logger.info(atomName);
//                HGSearchResult<Object> y = graph.find(hg.contains(atom));
                Iterator<HGHandle> y = ((Element) graph.get(atom)).getParents().iterator();
                while (y.hasNext()) {
                    HGHandle type = (HGHandle) y.next();
//					logger.info(type);
                    q.add(Pair.of(type, atom));
                }

//				IncidenceSet incidence = graph.getIncidenceSet(atom);
//				for (HGHandle hgHandle : incidence) {
//					q.add(Pair.of(hgHandle, atom));
//				}
            }
        }
//		logger.info("xxxx");
        while (!q.isEmpty()) {
            Pair<HGHandle, HGHandle> tmp = q.poll();
            Object parent = graph.get(tmp.getLeft());
            Object child = graph.get(tmp.getRight());
            if (parent instanceof Hyperedge) {

//				logger.info("addding"+ (Element) parent +"- >"+ (Element) child);
//				logger.info(l.getMap().keySet());
                l.AddToSet((Element) parent, (Element) child);

//                HGSearchResult<Object> y = graph.find(hg.contains(tmp.getLeft()));
                Iterator<HGHandle> y = ((Hyperedge) parent).getParents().iterator();
//				logger.info(graph.get(tmp.getLeft()) + "sssssssssss");
                while (y.hasNext()) {
                    HGHandle type = (HGHandle) y.next();
//					logger.info(graph.get(type).toString());
                    q.add(Pair.of(type, tmp.getLeft()));
                }

//				for (HGHandle hgHandle : graph.getIncidenceSet(tmp.getLeft())) {
//					q.add(Pair.of(hgHandle, tmp.getLeft()));
//				}
            }
        }
//		graph.close();
        return l;
    }

    /**
     * @return list of all atom names in the catalog
     */
    public static List<String> getAllAtoms(HyperGraph graph) {
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
        List<Atom> atoms = graph.getAll(hg.type(Atom.class));
//		graph.close();
        return atoms.stream().sorted().map(Atom::getName).collect(Collectors.toList());
    }

    public static List<Atom> getAtomList(HyperGraph graph) {
//		 = new HyperGraph(Const.HG_LOCATION_BOOK);
        return graph.getAll(hg.type(Atom.class));
    }

    public static List<Atom> getClassAtomList(HyperGraph graph) {
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
        return graph.getAll((hg.and(hg.type(Atom.class), hg.eq("type", AtomTypeEnum.Class))));
    }

    public static List<Relationship> getRelList(HyperGraph graph) {
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
        return graph.getAll(hg.type(Relationship.class));
    }

    public static List<Relationship> getClassRelList(HyperGraph graph) {
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
        List<Relationship> rels = graph.getAll(hg.type(Relationship.class));

        rels.removeIf(r -> !(((Atom) graph.get(r.getTargetAt(0))).getType() == AtomTypeEnum.Class
                && ((Atom) graph.get(r.getTargetAt(1))).getType() == AtomTypeEnum.Class)

        );

        return rels;
    }

    public static List<Relationship> getAtomClassRelList(HyperGraph graph, HGHandle atomHanlde) {
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
        List<Relationship> rels = graph.getAll(hg.and(hg.type(Relationship.class),
                hg.or(hg.orderedLink(atomHanlde, hg.anyHandle()), hg.orderedLink(hg.anyHandle(), atomHanlde))));

//		logger.info(rels.size());
        rels.removeIf(r -> !(((Atom) graph.get(r.getTargetAt(0))).getType() == AtomTypeEnum.Class
                && ((Atom) graph.get(r.getTargetAt(1))).getType() == AtomTypeEnum.Class));

        return rels;
    }

    public static List<Hyperedge> getAllFirstLevels(HyperGraph graph) {
        List<Hyperedge> hyperedges = new ArrayList<>();
        try {
            hyperedges = graph
                    .getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.FirstLevel)));
//			graph.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return hyperedges;
    }

    public static List<Hyperedge> getAllDesigns(HyperGraph graph) {
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
        List<Hyperedge> hyperedges = graph
                .getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.Design)));
//		graph.close();
        return hyperedges;
    }

    public static List<Hyperedge> GetFirstLevelsOfDesign(HyperGraph graph, Hyperedge design) {
        List<Hyperedge> l = new ArrayList<>();
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//		HGHandle designHandle = graph.getHandle(design);
        design.findAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.FirstLevel))).iterator()
                .forEachRemaining(d -> {
                    l.add(graph.get(d));
                });
        return l;
    }

    public static void addElementToHyperedge(HyperGraph graph, HGHandle parent, HGHandle child) {
        Hyperedge parentel = (Hyperedge) getElementbyHandle(graph, parent);
        parentel.add(child);
        graph.update(parentel);
    }

    public static HGHandle getHyperedgebyNameType(HyperGraph graph, String name, HyperedgeTypeEnum type) {
        return hg.findOne(graph, hg.and(hg.type(Hyperedge.class), hg.eq("name", name), hg.eq("type", type)));
    }

//	public static HGHandle getHyperedgebyNameType(String name, HyperedgeTypeEnum type) {
//		return hg.findOne(Const.graph, hg.and(hg.type(Hyperedge.class), hg.eq("name", name), hg.eq("type", type)));
//	}

    public static HGHandle getAtomByName(HyperGraph graph, String name) {
        return hg.findOne(graph, hg.and(hg.type(Atom.class), hg.eq("name", name)));
    }

    public static HGHandle getRelationshipByNameAtoms(HyperGraph graph, String name, HGHandle atom1, HGHandle atom2) {
        return hg.findOne(graph, hg.and(hg.type(Relationship.class), hg.eq("IRI", name), hg.orderedLink(atom1, atom2)));
    }

    public static HGHandle getRelationshipByNameandSource(HyperGraph graph, String name, HGHandle source) {
        logger.info("source   " + source);
        return hg.findOne(graph, hg.and(hg.type(Relationship.class), hg.eq("IRI", name),
                hg.or(hg.orderedLink(source, hg.anyHandle()), hg.orderedLink(hg.anyHandle(), source))));
    }

    public static List<HGHandle> getAttributeRelsofClass(HyperGraph graph, HGHandle source) {

        List<HGHandle> handles = new ArrayList<>();
        List<Relationship> l = hg.getAll(graph,
                hg.and(hg.type(Relationship.class), hg.orderedLink(source, hg.anyHandle())));

        l.removeIf(r -> ((Atom) graph.get(r.getTargetAt(1))).getType() == AtomTypeEnum.Class);

        for (Relationship relationship : l) {
            handles.add(graph.getHandle(relationship));
        }

        return handles;
    }

    public static List<HGHandle> getAttributesClass(HyperGraph graph, HGHandle source) {

        List<HGHandle> handles = new ArrayList<>();
        List<Relationship> l = hg.getAll(graph,
                hg.and(hg.type(Relationship.class), hg.orderedLink(source, hg.anyHandle())));

        l.removeIf(r -> ((Atom) graph.get(r.getTargetAt(1))).getType() == AtomTypeEnum.Class);

        for (Relationship relationship : l) {
            handles.add(relationship.getTargetAt(1));
        }

//		logger.info(handles.size());
        return handles;
    }

//	public static HGHandle getRelationshipByName(HyperGraph graph, String name) {
//		return hg.findOne(graph, hg.and(hg.type(Relationship.class), hg.eq("IRI", name)));
//	}

    public static List<HGHandle> getHyperedgesContainingAtoms(HyperGraph graph, HGHandle... atoms) {
        return hg.findAll(graph, hg.and(hg.type(Hyperedge.class), hg.link(atoms)));
    }

    public static HGHandle getFirstLevelHyperedgesContainingAtom(HyperGraph graph, HGHandle atom) {

//		HGHandle s = hg.findOne(graph, hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.SecondLevel),
//				hg.link(new HGHandle[] { atom })));

//        List<HGHandle> x = hg.findAll(graph, hg.and(hg.type(Hyperedge.class), hg.contains(atom)));
        List<HGHandle> x = ((Element) graph.get(atom)).getParents();
//        logger.info(x.size());
        return null;
    }

    public static List<HGHandle> getParentHyperedges(HyperGraph graph, HGHandle hyperedge) {

//		HGHandle s = hg.findOne(graph, hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.SecondLevel),
//				hg.link(new HGHandle[] { atom })));

//        List<HGHandle> x = hg.findAll(graph, hg.and(hg.type(Hyperedge.class), hg.contains(hyperedge)));
//		logger.info(x.size());

//        logger.info(graph.get(hyperedge).toString());
        List<HGHandle> x = ((Element) graph.get(hyperedge)).getParents();
//        logger.info(x.size());
        return x;
    }

    public static void makeRelation(HyperGraph graph, HashMap<String, HGHandle> atomHandles,
                                    Table<String, String, HGHandle> relHandles, String id, String keyn, int mult) throws Exception {

        HGHandle sda = graph
                .add(new Relationship("has" + keyn, mult > 1 ? CardinalityEnum.ONE_TO_MANY : CardinalityEnum.ONE_TO_ONE,
                        mult, atomHandles.get(id), atomHandles.get(keyn)));

//		logger.info(graph.get(sda).toString());
        relHandles.put(id, keyn, sda);
//		logger.info(relHandles);
//		logger.info(id + "->" + "has" + keyn);
    }

    public static void makeRelation(HyperGraph graph, HashMap<String, HGHandle> atomHandles,
                                    Table<String, String, HGHandle> relHandles, String id, String keyn, double[] multiplicity)
            throws Exception {

        HGHandle sda = graph
                .add(new Relationship(id + "and" + keyn, multiplicity, atomHandles.get(id), atomHandles.get(keyn)));

//		logger.info(graph.get(sda).toString());
        relHandles.put(id, keyn, sda);
//		logger.info(relHandles);
//		logger.info(id + "->" + "and" + keyn);
    }

    public static void makeRelationWithName(HyperGraph graph, HashMap<String, HGHandle> atomHandles,
                                            Table<String, String, HGHandle> relHandles, String id, String keyn, double multiplicity, String name)
            throws Exception {
        HGHandle sda = graph
                .add(new Relationship(name, multiplicity > 1 ? CardinalityEnum.ONE_TO_MANY : CardinalityEnum.ONE_TO_ONE,
                        multiplicity, atomHandles.get(id), atomHandles.get(keyn)));
//		logger.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		logger.info(sda);
//		logger.info(graph.get(sda).toString());

        relHandles.put(id, keyn, sda);
//		logger.info(id + "->" + "has" + keyn);
    }

    public static HGHandle addHyperedgetoGraph(HyperGraph graph, String name, HyperedgeTypeEnum type,
                                               HGHandle... targetSet) throws Exception {
        if (type == HyperedgeTypeEnum.Set) {
            throw new Exception("Plese use the set constructor");
        }
        HGHandle handle = graph.add(new Hyperedge());
        graph.replace(handle, new Hyperedge(graph, handle, name, type, targetSet));
        return handle;
    }

    public static HGHandle addSetHyperedgetoGraph(HyperGraph graph, String name, Relationship rel,
                                                  HGHandle... targetSet) throws Exception {

        if (targetSet.length > 1) {
            throw new Exception("Set can have only one struct");
        }
        HGHandle handle = graph.add(new Hyperedge());
        Hyperedge hyp = new Hyperedge(graph, handle, name, HyperedgeTypeEnum.Set, targetSet);
        hyp.add(graph.getHandle(rel));
        hyp.addToMap(targetSet[0], rel);
        graph.replace(handle, hyp);
        return handle;
    }

    public static HGHandle addSetHyperedgetoGraph(HyperGraph graph, String name, ArrayList<Relationship> rels,
                                                  HGHandle... targetSet) throws Exception {

        HGHandle handle = graph.add(new Hyperedge());
        List<HGHandle> list = Arrays.asList(targetSet);
//		for (HGHandle hgHandle : list) {
//			rels.forEach(r->{
//				if(list.contains(graph.getHandle(r))) {
//					
//				}
//			});
//		}

        Hyperedge hyp = new Hyperedge(graph, handle, name, HyperedgeTypeEnum.Set, targetSet);
//		hyp.add(graph.getHandle(rel));
        for (Relationship rel : rels) {
            hyp.add(graph.getHandle(rel));
            hyp.addToMap(targetSet[0], rel);
        }
        graph.replace(handle, hyp);
        return handle;
    }

    public static ArrayList<HGHandle> getParentHyperesdeofAtom(HyperGraph graph, HGHandle atom) {
        ArrayList<HGHandle> parents = new ArrayList<>();
//        HGSearchResult<Object> incidence = graph.find(hg.contains(atom));

        Iterator<HGHandle> incidence = ((Element) graph.get(atom)).getParents().iterator();
        while (incidence.hasNext()) {
            HGHandle hgHandle = (HGHandle) incidence.next();
            if (isRootofHyperedge(graph, atom, hgHandle))
                parents.add(hgHandle);
        }

        return parents;
    }

    public static boolean isRootofHyperedge(HyperGraph graph, HGHandle atom, HGHandle hyperedge) {
        boolean b = false;
        HashMap<Atom, HGHandle> candidateAtoms = new HashMap<>();
        ArrayList<HGHandle> relationships = new ArrayList<>();
        Object hyp = graph.get(hyperedge);
        Atom target = graph.get(atom);
        if (hyp.getClass() != Hyperedge.class) {
            return false;
        }

        Iterator<HGHandle> seconditer = ((Hyperedge) hyp).findAll().iterator();
        while (seconditer.hasNext()) {
            HGHandle hgHandle2 = (HGHandle) seconditer.next();

            Object a = graph.get(hgHandle2);

            if (a.getClass().equals(Atom.class) && ((Atom) a).getType() == AtomTypeEnum.Class) {

                candidateAtoms.put((Atom) a, hgHandle2);
//				logger.info("child " + ((Atom) a).getName());

            }

            if (a.getClass().equals(Relationship.class)) {
                relationships.add(hgHandle2);
            }
        }

        ArrayList<Atom> atoms = new ArrayList<>();
        candidateAtoms.keySet().iterator().forEachRemaining(atoms::add);

        if (atoms.size() == 1) {
            return true;
        } else {

            ArrayList<Boolean> evals = new ArrayList<>();

            for (int j = 0; j < atoms.size(); j++) {
                Atom atom2 = atoms.get(j);
                if (atom2 != target) {
                    HGHandle rel = Graphoperations.getRelationshipByNameAtoms(graph, "has" + atom2.getName(),
                            candidateAtoms.get(target), candidateAtoms.get(atom2));
                    if (rel != null && relationships.contains(rel)) {
                        evals.add(true);
                    } else
                        break;
                }
            }
            if (evals.size() == atoms.size() - 1) {
                if (!evals.contains(false)) {
                    return true;
                }
            }

        }
        return b;
    }

    public static boolean isRootofHyperedge(HyperGraph graph, HGHandle atom, Hyperedge hyp) {
        boolean b = false;
        HashMap<Atom, HGHandle> candidateAtoms = new HashMap<>();
        ArrayList<HGHandle> relationships = new ArrayList<>();
        Atom target = graph.get(atom);
        if (hyp.getClass() != Hyperedge.class) {
            return false;
        }
        if (target.getType() != AtomTypeEnum.Class) {
            return false;
        }

        Iterator<HGHandle> seconditer = ((Hyperedge) hyp).findAll().iterator();
        while (seconditer.hasNext()) {
            HGHandle hgHandle2 = (HGHandle) seconditer.next();

            Object a = graph.get(hgHandle2);

            if (a.getClass().equals(Atom.class) && ((Atom) a).getType() == AtomTypeEnum.Class) {

                candidateAtoms.put((Atom) a, hgHandle2);
//				logger.info("child " + ((Atom) a).getName());

            }

            if (a.getClass().equals(Relationship.class)) {
                relationships.add(hgHandle2);
            }
        }

        ArrayList<Atom> atoms = new ArrayList<>();
        candidateAtoms.keySet().iterator().forEachRemaining(atoms::add);

        if (atoms.size() == 1) {
            return true;
        } else {

            ArrayList<Boolean> evals = new ArrayList<>();

            for (int j = 0; j < atoms.size(); j++) {
                Atom atom2 = atoms.get(j);
                if (atom2 != target) {
                    HGHandle rel = Graphoperations.getRelationshipByNameAtoms(graph, "has" + atom2.getName(),
                            candidateAtoms.get(target), candidateAtoms.get(atom2));
                    if (rel != null && relationships.contains(rel)) {
                        evals.add(true);
                    } else
                        break;
                }
            }
            if (evals.size() == atoms.size() - 1) {
                if (!evals.contains(false)) {
                    return true;
                }
            }

        }
        return b;
    }

//	public static HGHandle findRootAtom(HyperGraph graph, ArrayList<HGHandle> atomlist) {
//		boolean b = false;
//		HashMap<Atom, HGHandle> candidateAtoms = new HashMap<>();
//		ArrayList<HGHandle> relationships = new ArrayList<>();
//
//		for (HGHandle hgHandle2 : atomlist) {
//			Object a = graph.get(hgHandle2);
//
//			if (a.getClass().equals(Atom.class) && ((Atom) a).getType() == AtomTypeEnum.Class) {
//
//				candidateAtoms.put((Atom) a, hgHandle2);
//
//			}
//
//			if (a.getClass().equals(Relationship.class)) {
//				relationships.add(hgHandle2);
//			}
//		}
//
//		ArrayList<Atom> atoms = new ArrayList<>();
//		candidateAtoms.keySet().iterator().forEachRemaining(atoms::add);
//
//		if (atoms.size() == 1) {
//			b = true;
//		} else {
//
//			ArrayList<Boolean> evals = new ArrayList<>();
//
//			for (int i = 0; i < atoms.size(); i++) {
//
//				Object target = atoms.get(i);
//
//				for (int j = 0; j < atoms.size(); j++) {
//					Atom atom2 = atoms.get(j);
//					if (i != j) {
//						HGHandle rel = Graphoperations.getRelationshipByNameAtoms(graph, "has" + atom2.getName(),
//								candidateAtoms.get(target), candidateAtoms.get(atom2));
//						if (rel != null) {
//							evals.add(true);
//						} else
//							break;
//					}
//				}
//				if (evals.size() == atoms.size() - 1) {
//					if (!evals.contains(false)) {
//						return true;
//					}
//				}
//			}
//
//		}
//	}

    public static HGHandle addAtomtoGraph(HyperGraph graph, Atom atm) {
        // TODO Auto-generated method stub

        return graph.add(atm);
    }

    public static void printDesign(HyperGraph graph2) {
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
        List<Hyperedge> hyperedges = graph2
                .getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.FirstLevel)));
        for (Hyperedge hyperedge : hyperedges) {
            hyperedge.print(0);
        }
    }

    public static JSONObject JSONDesign(HyperGraph graph2) {
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        try {
            List<Hyperedge> hyperedges = graph2
                    .getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.FirstLevel)));
            for (Hyperedge hyperedge : hyperedges) {
//                s.append(hyperedge.printToJSON());
                arr.put(hyperedge.printToJSON());
            }
            obj.put("collections",arr);
        } catch (Exception e) {
            // TODO: handle exception

            e.printStackTrace();
            logger.info("XXXXXXXXXX" + graph2);
        }

        return obj;
    }

    public static String stringDesign(HyperGraph graph2) {
        StringBuilder s = new StringBuilder();
        s.append(graph2.getLocation()+"\n");
        try {
            List<Hyperedge> hyperedges = graph2
                    .getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.FirstLevel)));
            for (Hyperedge hyperedge : hyperedges) {
                s.append(hyperedge.printToString(0));
            }
        } catch (Exception e) {
            // TODO: handle exception

            e.printStackTrace();
            logger.info("XXXXXXXXXX" + graph2);
        }

        return s.toString();
    }

//	public static void printDesign() {
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//		logger.info("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
//		List<Hyperedge> hyperedges = graph
//				.getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.Database_Doc)));
//		for (Hyperedge hyperedge : hyperedges) {
//			hyperedge.print(0);
//		}
//	}

    public static void printDesign(String path, HyperGraph graph) {
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
        try {
            FileWriter myWriter = new FileWriter(path);

            if (graph != null) {

                List<Hyperedge> hyperedges = graph
                        .getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.Database_Doc)));

                for (Hyperedge hyperedge : hyperedges) {
                    hyperedge.print(myWriter, 0);
                }

                logger.info("Successfully wrote to the file.");
            }
            myWriter.close();
        } catch (IOException e) {
            logger.info("An error occurred.");
            e.printStackTrace();
        }

    }

    public static boolean isConsistant(HyperGraph graph) {
        Queue<Element> atoms = new LinkedBlockingQueue<>();

        atoms.addAll(graph.getAll(hg.type(Atom.class)));
        atoms.addAll(graph.getAll(hg.type(Relationship.class)));

        int loopcount = 0;

        Set<Element> set = new HashSet<>();
        set.addAll(atoms);

        do {
            Element current = atoms.poll();
            HGHandle currentHandle = graph.getHandle(current);
            Element currentObj = graph.get(currentHandle);
            if (currentObj instanceof Hyperedge
                    && ((Hyperedge) currentObj).getType() == HyperedgeTypeEnum.Database_Doc) {
                atoms.add(current);
            } else {
//                HGSearchResult<Object> parents = graph.find(hg.contains(currentHandle));
                Iterator<HGHandle> parents = currentObj.getParents().iterator();
                if (!parents.hasNext()) {
                    atoms.add(current);
                } else {

                    while (parents.hasNext()) {
                        Element parent = graph.get((HGHandle) parents.next());
                        if (!atoms.contains(parent))
                            atoms.add(parent);
                    }

                }
            }

            Set<Element> set2 = new HashSet<>();
            set2.addAll(atoms);

            if (set.equals(set2)) {
                loopcount++;
            } else {
                set = set2;
                loopcount = 0;
            }

        } while (loopcount < 100);

        atoms.stream().forEach((a -> {
//            logger.info(a);
        }));

        return atoms.size() == 1;
    }

    public static Element getElementbyHandle(HyperGraph graph, HGHandle relationshipByNameandSource) {
        // TODO Auto-generated method stub
        return graph.get(relationshipByNameandSource);
    }

    public static void unindex(HyperGraph graph) {
//        Stack<Pair<Element, Element>> stack = new Stack<>();
        Stack<Hyperedge> stack = new Stack<>();
        Queue<Hyperedge> queue = new LinkedList<>();
        List<Hyperedge> hyperedges = graph
                .getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.FirstLevel)));

        hyperedges.forEach(f -> {
            f.findAll().forEach(s -> {
                Element e = graph.get(s); // secondLevel
                if (e instanceof Hyperedge) {
                    queue.add((Hyperedge) e);
                }
            });
        });

        while (!queue.isEmpty()) {
            Hyperedge hyp = queue.poll();
            hyp.findAll().forEach(cand -> {
                Element candE = graph.get(cand); // inner elements
                if (candE instanceof Hyperedge){
                    queue.add((Hyperedge) candE);
                    stack.push((Hyperedge) candE);
                }
//                stack.push(Pair.of(hyp,candE));
            });
        }

//        while (!stack.isEmpty()){
//            Pair<Element, Element> pair = stack.pop();
//            Hyperedge hyp = (Hyperedge) pair.getLeft();
//            Element elm = pair.getRight();
//            HGHandle handle = graph.getPersistentHandle(graph.getHandle(elm));
//            logger.info( hyp.getName() +"--"+ elm.getName()+"");
//            boolean res =(hyp.remove(handle));
//            logger.info(res + "   " + stack.size());
//        }
//        logger.info("done");
        while (!stack.isEmpty()){
            Hyperedge hyp = stack.pop();
//hyp.removeAll();
        }

    }
}
