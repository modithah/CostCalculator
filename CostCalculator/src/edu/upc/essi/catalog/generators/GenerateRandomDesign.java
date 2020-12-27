package edu.upc.essi.catalog.generators;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.apache.commons.io.FileUtils;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;
import org.hypergraphdb.HGQuery.hg;

import com.c05mic.generictree.Node;
import com.c05mic.generictree.Tree;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateRandomDesign {

	public GenerateRandomDesign() {
		// TODO Auto-generated constructor stub
	}

	final double pSet = 0.5;
	final static double skip = 0;
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
	public static HyperGraph get() throws IOException {
		// TODO Auto-generated method stub
		File serverDir = new File(Const.HG_LOCATION_BOOK );
//		FileUtils.cleanDirectory(serverDir);
		HyperGraph graph = new HyperGraph(serverDir.getAbsolutePath()+ File.separator + UUID.randomUUID().toString());
//		LoadGraph.LoadBaseFromJSONFile("data/schemas/tpcc-sample-3EntityRels.json",graph);
		LoadGraph.LoadBaseFromJSONFile("data/schemas/rubis/rubis.json",graph);
		Atom dummyAtom = new Atom();
		Relationship dummyRel = new Relationship();
		RelStructure dummyRelStr = new RelStructure(dummyRel, null);
		ArrayList<Atom> allAtoms = new ArrayList<>();
		ArrayList<Relationship> allRels = new ArrayList<>();
		ArrayList<Relationship> usedRels = new ArrayList<>();
		ArrayList<Relationship> IteratingRels = new ArrayList<>();
		ArrayList<Relationship> reification = new ArrayList<>();
		Table<Atom, Atom, RelStructure> structures = HashBasedTable.create();
		ArrayList<LinkedList<RelStructure>> paths = new ArrayList<>();
		ArrayList<Tree<RelStructure>> components = new ArrayList<>();
		Random rand = new Random();

		allAtoms.addAll(Graphoperations.getClassAtomList(graph));
		allRels.addAll(Graphoperations.getClassRelList(graph));

//		logger.info("FFFFFFFFFFF" + allAtoms.get(0));
//		Graphoperations.getAtomClassRelList(graph,graph.getHandle(allAtoms.get(0))).forEach(r -> {
//			logger.info(r);
//		});
//		logger.info("Xxxxxxxx");
//		allRels.forEach(r -> {
//			logger.info(r);
//		});

//		Graphoperations.getAttributeRelsofClass(graph, graph.getHandle(allAtoms.get(1))).forEach(r -> {
//			logger.info(graph.get(r).toString());
//		});
//
//		Graphoperations.getAttributesClass(graph, graph.getHandle(allAtoms.get(1))).forEach(r -> {
//			logger.info(graph.get(r).toString());
//		});

		// pick a random relationship first
		Relationship firstPick = allRels.remove(rand.nextInt(allRels.size()));
		usedRels.add(firstPick);
//		logger.info("BEGINING SSSSSSS");
		boolean refer = rand.nextBoolean();
//		for (int i = 0; i < 10; i++) {

		int firstChoice = rand.nextInt(3);
//		logger.info("first choice" + firstChoice);

		chooseOperation(graph, dummyAtom, dummyRelStr, reification, paths, firstPick, refer, firstChoice, allRels,
				components);
		List<Relationship> edges = Graphoperations.getAtomClassRelList(graph,
				firstPick.getTargetAt(firstChoice == 2 ? rand.nextInt(2) : firstChoice));
		edges.removeAll(usedRels);
//		logger.info("END SSSSSSS");
//		logger.info(paths);
		while (!(allRels.isEmpty() && edges.isEmpty())) {
			Relationship newPick = null;
			if (!edges.isEmpty()) {
				newPick = edges.remove(rand.nextInt(edges.size()));
			} else {
				newPick = allRels.remove(rand.nextInt(allRels.size()));
			}
			allRels.remove(newPick);

			int newChoice = rand.nextInt(2);
//			logger.info("second choice" + newChoice);
			refer = rand.nextBoolean();
			int root = newChoice;

			chooseOperation(graph, dummyAtom, dummyRelStr, reification, paths, newPick, refer, newChoice, allRels,
					components);
			edges = Graphoperations.getAtomClassRelList(graph, newPick.getTargetAt(newChoice == 2 ? rand.nextInt(2) : newChoice));
			usedRels.add(newPick);
			edges.removeAll(usedRels);

		}

//		logger.info(paths);
//		for (Tree<RelStructure> tree : components) {
//			tree.getPostOrderTraversal().forEach(r -> {
//				logger.info(r);
//			});
//		}

		for (Tree<RelStructure> tree : components) {
			dooperation(tree.getRoot(), graph, allAtoms);
		}

		for (Atom atm : allAtoms) {
			List<HGHandle> elms = new ArrayList<>();
			HGHandle rootHandle = graph.getHandle(atm);
			elms.add(rootHandle);
			elms.addAll(Graphoperations.getAttributesClass(graph, rootHandle));
			elms.addAll(Graphoperations.getAttributeRelsofClass(graph, rootHandle));
			try {
				Graphoperations.addHyperedgetoGraph(graph, atm.getName() + "Struct", HyperedgeTypeEnum.Struct,
						elms.toArray(new HGHandle[elms.size()]));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		finalyzeGraph(graph);

		return graph;
	}

	private static void chooseOperation(HyperGraph graph, Atom dummyAtom, RelStructure dummyRelStr,
			ArrayList<Relationship> reification, ArrayList<LinkedList<RelStructure>> structures, Relationship rel,
			boolean refer, int opChoice, ArrayList<Relationship> allRels, ArrayList<Tree<RelStructure>> components) {

		switch (opChoice) {
		case 0: // use first and put the next inside
//			if (structures.contains(graph.get(rel.getTargetAt(0)), graph.get(rel.getTargetAt(1)))) {
//				allRels.add(rel);
//			} else {
//				structures.put(graph.get(rel.getTargetAt(0)), graph.get(rel.getTargetAt(1)),
//						new RelStructure(rel, refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest));
//			}
			if (!isRootInPAth(structures, graph.get(rel.getTargetAt(0)), graph.get(rel.getTargetAt(1)), rel,
					refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest, components, allRels)) {
				LinkedList<RelStructure> rels = new LinkedList<>();

				Node<RelStructure> root = new Node<RelStructure>(
						new RelStructure(null, OperationTypeEnum.Both_Nest, null, graph.get(rel.getTargetAt(0))));
				root.addChild(new Node<RelStructure>(
						new RelStructure(rel, refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest,
								graph.get(rel.getTargetAt(0)), graph.get(rel.getTargetAt(1)))));
				Tree<RelStructure> tree = new Tree<>(root);
				rels.add(new RelStructure(rel, refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest,
						graph.get(rel.getTargetAt(0)), graph.get(rel.getTargetAt(1))));
				components.add(tree);
//				logger.info(rels);

				structures.add(rels);

			}
			break;
		case 1: // use second and put the next inside
//			if (structures.contains(graph.get(rel.getTargetAt(1)), graph.get(rel.getTargetAt(0)))) {
//				allRels.add(rel);
//			} else {
//			structures.put(graph.get(rel.getTargetAt(1)), graph.get(rel.getTargetAt(0)),
//					new RelStructure(rel, refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest));
//			}
			if (!isRootInPAth(structures, graph.get(rel.getTargetAt(1)), graph.get(rel.getTargetAt(0)), rel,
					refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest, components, allRels)) {
				LinkedList<RelStructure> rels = new LinkedList<>();

				Node<RelStructure> root = new Node<RelStructure>(
						new RelStructure(null, OperationTypeEnum.Both_Nest, null, graph.get(rel.getTargetAt(1))));
				root.addChild(new Node<RelStructure>(new Node<RelStructure>(
						new RelStructure(rel, refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest,
								graph.get(rel.getTargetAt(1)), graph.get(rel.getTargetAt(0))))));
				Tree<RelStructure> tree = new Tree<>(root);

				rels.add(new RelStructure(rel, refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest,
						graph.get(rel.getTargetAt(1)), graph.get(rel.getTargetAt(0))));
				components.add(tree);
				structures.add(rels);
			}
			break;

		case 2: // put refs on both ends
			if (!isRootInPAth(structures, graph.get(rel.getTargetAt(0)), graph.get(rel.getTargetAt(1)), rel,
					refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest, components, allRels)) {
				LinkedList<RelStructure> rels = new LinkedList<>();
				rels.add(new RelStructure(rel, refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest,
						graph.get(rel.getTargetAt(1)), graph.get(rel.getTargetAt(0))));
				structures.add(rels);
				Node<RelStructure> root = new Node<RelStructure>(
						new RelStructure(null, OperationTypeEnum.Both_Nest, null, graph.get(rel.getTargetAt(0))));
				root.addChild(new Node<RelStructure>(
						new RelStructure(rel, refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest,
								graph.get(rel.getTargetAt(0)), graph.get(rel.getTargetAt(1)))));
				Tree<RelStructure> tree = new Tree<>(root);
				components.add(tree);
			}
			if (!isRootInPAth(structures, graph.get(rel.getTargetAt(1)), graph.get(rel.getTargetAt(0)), rel,
					refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest, components, allRels)) {
				LinkedList<RelStructure> rels = new LinkedList<>();
				rels.add(new RelStructure(rel, refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest,
						graph.get(rel.getTargetAt(0)), graph.get(rel.getTargetAt(1))));
				structures.add(rels);
				Node<RelStructure> root = new Node<RelStructure>(
						new RelStructure(null, OperationTypeEnum.Both_Nest, null, graph.get(rel.getTargetAt(1))));
				root.addChild(new Node<RelStructure>(new Node<RelStructure>(
						new RelStructure(rel, refer ? OperationTypeEnum.Refer : OperationTypeEnum.Nest,
								graph.get(rel.getTargetAt(1)), graph.get(rel.getTargetAt(0))))));
				Tree<RelStructure> tree = new Tree<>(root);
				components.add(tree);
			}
			break;

		case 3: // reification
//			structures.put(graph.get(rel.getTargetAt(0)), dummyAtom, dummyRelStr);
//			structures.put(graph.get(rel.getTargetAt(1)), dummyAtom, dummyRelStr);
//			reification.add(rel);
			break;

		default:
			break;
		}

//		logger.info("XXXXXXXXXXXXXXXXXXXXXXXXXX");
//		for (Tree<RelStructure> tree : components) {
//			logger.info("--------------");
//			tree.getPostOrderTraversal().forEach(r -> {
//				logger.info(r);
//			});
//		}
//		logger.info("SSSSSSSSSSSS");
	}

	private static boolean isRootInPAth(ArrayList<LinkedList<RelStructure>> path, Atom root, Atom child,
			Relationship rel, OperationTypeEnum op, ArrayList<Tree<RelStructure>> components,
			ArrayList<Relationship> allRels) {
		boolean ret = false;

		ArrayList<LinkedList<RelStructure>> toAdd = new ArrayList<>();
		for (LinkedList<RelStructure> linkedList : path) {
//			if (linkedList.getLast().to == root) {
//				ret = true;
//				linkedList.add(new RelStructure(rel, op, root, child));
//			}
			if (linkedList.stream().anyMatch(l -> l.to == root && l.from != child)) {
//				logger.info("yessssssssssssssss");
				ret = true;
				LinkedList<RelStructure> list = new LinkedList<>();
				boolean found = false;
				boolean update = false;
				for (RelStructure relStructure : linkedList) {
					if (relStructure.to == root) {
						if (linkedList.getLast() == relStructure) { // if its the last element only update the current
																	// list
							update = true;
						}
						list.add(relStructure);
						found = true;
						break;
					} else if (!found) {
						list.add(relStructure);
					}
				}
				if (update) {
//					logger.info("updateing");
					linkedList.add(new RelStructure(rel, op, root, child));
				} else {
					list.add(new RelStructure(rel, op, root, child));
					toAdd.add(list);
				}
			}
		}

		for (Tree<RelStructure> tree : components) {
			if (tree.getPostOrderTraversal().stream()
					.anyMatch(l -> l.getData().to == root && l.getData().from != child)) {
//				logger.info("yessssssssssssssss");
				ret = true;

				for (Node<RelStructure> node : tree.getPostOrderTraversal()) {

//					logger.info(node.getData().to +"->"+ node.getData().from);
//				logger.info( node.getChildren().isEmpty() || node.getChildren().stream().anyMatch(c -> c.getData().rel != rel));
				
					if (node.getData().to == root && node.getData().from != child
							&& (node.getChildren().isEmpty() || !node.getChildren().stream().anyMatch(c -> c.getData().rel == rel))) {
//						logger.info("found node");
						node.addChild(new Node<RelStructure>(new RelStructure(rel, op, root, child)));
						if (node.getChildren().size() == 1 && Math.random() < skip) {
							RelStructure data = node.getData();
							data.op = OperationTypeEnum.RefSkip;
							node.setData(data);
							allRels.add(rel);
						}
					}
				}

				break;
			}

		}
		return ret;

	}

	private static ArrayList<HGHandle> dooperation(Node<RelStructure> node, HyperGraph graph,
			ArrayList<Atom> allAtoms) {
//		logger.info(node.getData().from+"->"+node.getData().to);
		ArrayList<HGHandle> handles = new ArrayList<>();
		for (Node<RelStructure> child : node.getChildren()) {
			handles.addAll(dooperation(child, graph, allAtoms));
		}

		switch (node.getData().op) {
		case Refer:
			handles.add(graph.getHandle(node.getData().to));
			handles.add(graph.getHandle(node.getData().rel));
			break;
		case RefSkip:
			handles.add(graph.getHandle(node.getData().rel));
			break;
		case Nest:
			List<HGHandle> elms = new ArrayList<>();
			HGHandle rootHandle = graph.getHandle(node.getData().to);
			elms.add(rootHandle);
			elms.addAll(Graphoperations.getAttributesClass(graph, rootHandle));
			elms.addAll(Graphoperations.getAttributeRelsofClass(graph, rootHandle));
			elms.addAll(handles);
			handles.clear();
			try {
//				handles.add();
//				handles.add(graph.getHandle(node.getData().rel));

				String name=node.getData().to.getName()+UUID.randomUUID().toString();

				handles.add(Graphoperations.addHyperedgetoGraph(graph, name+ "Struct",
						HyperedgeTypeEnum.Struct, elms.toArray(new HGHandle[elms.size()])));
				
				HGHandle x=Graphoperations.addSetHyperedgetoGraph(graph, name + "Set",
						node.getData().rel, handles.toArray(new HGHandle[handles.size()]));
				handles.clear();
				handles.add(x);
				allAtoms.remove(node.getData().to);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		
//		logger.info(node.getData().from+"->"+node.getData().to);
//		for (HGHandle hgHandle : handles) {
//			logger.info(graph.get(hgHandle).toString());
//		}

		if (node.getParent() == null) {
			try {
				HGHandle rootHandle = graph.getHandle(node.getData().to);
				handles.add(0,rootHandle);
				handles.addAll(Graphoperations.getAttributesClass(graph, rootHandle));
				handles.addAll(Graphoperations.getAttributeRelsofClass(graph, rootHandle));
				Graphoperations.addHyperedgetoGraph(graph, node.getData().to.getName()+UUID.randomUUID().toString() + "Struct",
						HyperedgeTypeEnum.Struct, handles.toArray(new HGHandle[handles.size()]));
				allAtoms.remove(node.getData().to);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return handles;
	}

	private static void finalyzeGraph(HyperGraph graph) {
//		logger.info("end");
		List<HGHandle> firstLevels = new ArrayList<>();
		List<Hyperedge> structhyps = graph
				.getAll(hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.Struct)));

		// wrap structs without sets around sets and make them top level

		for (Hyperedge hyperedge : structhyps) {
			HGHandle handle = graph.getHandle(hyperedge);
//			logger.info(hyperedge.getName()+" ---- "+hyperedge.getParents().size());
//			HGSearchResult<Object> x = graph.find(hg.contains(handle));


			if (hyperedge.getParents().size()>0) {
//				logger.info("not empty");
//				logger.info(graph.get((HGHandle) x.next()).toString());

			} else {
//				logger.info("empty");
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
//			logger.info(hyperedge.getName()+"  "+hyperedge.getParents().size());
			HGHandle handle = graph.getHandle(hyperedge);
//			HGSearchResult<Object> x = graph.find(hg.contains(handle));
			if (hyperedge.getParents().size()>0) {
//				logger.info("not empty");
			} else {
//				logger.info("empty");
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
