import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSet;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPlainLink;
import org.hypergraphdb.HGQuery.hg;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.AdjacencyList;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.cost.CostGenerator;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.estocada.CreateGraph;
import edu.upc.essi.catalog.estocada.CreateGraph2;
import edu.upc.essi.catalog.loaders.LoadGraph;
import edu.upc.essi.catalog.ops.CostOperations;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.SchemaOperations;
import edu.upc.essi.catalog.query.QueryGenerator;

public class run2 {

	public run2() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> atoms = Graphoperations.getAllAtoms();

		if (atoms.isEmpty()) {
			System.out.println("No data available Creating the Graph");
			CreateGraph2.main(null);
			try {
				Thread.sleep(2000);
				atoms = Graphoperations.getAllAtoms();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		System.out.println(
				"The following atoms are available. data in k-v stores cannot be prsented in a query (bid,bname)");
		// System.out.println(atoms);
		CostGenerator c = new CostGenerator();
		QueryGenerator q = new QueryGenerator();
//		Scanner scanner = new Scanner(System.in);
//		System.out.print(
//				"Enter the atom names you want to query seperated by commas (type exit to quit): ");

		String input = "A_ID,AB_ID,B_ID,A_NAME,B_NAME";
//		String input = scanner.next();

//		while (!input.equals("exit")) {
//			System.out.println("------Queries for selected atoms------");
//
		AdjacencyList map = Graphoperations.makeHashmap(input.split(","));

		System.out.println(map);
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
		Set<Element> keys = map.getMap().keySet();

		for (Element element : keys) {
			if (element instanceof Hyperedge) {
				if (((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Doc)
						|| ((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Rel)
						|| ((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Col)) {
					q.CreateSchemaFromMap(element, "", map, ((Hyperedge) element).getType());
					c.CreateCostFromMap(element, "", map, ((Hyperedge) element).getType());
//				LoadGraph.SaveDesignToCSV((Hyperedge) element, "booksample_design1");
				}
			}
		}

		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//
		HGHandle atm = Graphoperations.getAtomByName(graph, "A_ID");
		HGHandle atm2 = Graphoperations.getAtomByName(graph, "AB_ID");
		ArrayList<HGHandle> p = Graphoperations.getParentHyperesdeofAtom(graph, atm);

		SchemaOperations.makeReference(graph, atm, atm2,
				Graphoperations.getRelationshipByNameAtoms(graph, "hasAB_ID", atm, atm2), p.get(0));

		map = Graphoperations.makeHashmap(input.split(","));

		keys = map.getMap().keySet();

		for (Element element : keys) {
			if (element instanceof Hyperedge) {
				if (((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Doc)
						|| ((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Rel)
						|| ((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Col)) {
					q.CreateSchemaFromMap(element, "", map, ((Hyperedge) element).getType());
					c.CreateCostFromMap(element, "", map, ((Hyperedge) element).getType());
					LoadGraph.SaveDesignToCSV((Hyperedge) element, "booksample_design2");
				}
			}
		}

//		graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//		HGHandle atm3 = Graphoperations.getAtomByName(graph, "B_ID");
//		HGHandle atm4 = Graphoperations.getAtomByName(graph, "AB_ID");
//		p = Graphoperations.getParentHyperesdeofAtom(graph, atm3);
//
//		SchemaOperations.makeReference(graph, atm3, atm4,
//				Graphoperations.getRelationshipByNameAtoms(graph, "hasAB_ID", atm3, atm4), p.get(0));
//
//		map = Graphoperations.makeHashmap(input.split(","));
//
//		keys = map.getMap().keySet();
//
//		for (Element element : keys) {
//			if (element instanceof Hyperedge) {
//				if (((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Doc)
//						|| ((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Rel)
//						|| ((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Col)) {
//					q.CreateSchemaFromMap(element, "", map, ((Hyperedge) element).getType());
//					c.CreateCostFromMap(element, "", map, ((Hyperedge) element).getType());
//					LoadGraph.SaveDesignToCSV((Hyperedge) element, "booksample_design3");
//				}
//			}
//		}
//
//		graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//		graph.close();

//			input = scanner.next();
//		}

//		System.out.println(g.CreateQuery(Graphoperations.getDBHyperedgebyType(HyperedgeTypeEnum.Database_Doc), ""));

//		HyperGraph graph = Const.graph;
//		HGHandle r = hg.findOne(graph, hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.FirstLevel)));
//		CostOperations c = new CostOperations();
//
//		

		// c.CalculateCounts(r);

//
//		graph.getIncidenceSet(handle);

		// System.out.println((Hyperedge) hg.getOne(graph,
		// hg.and(hg.type(Hyperedge.class), hg.eq("type",
		// HyperedgeTypeEnum.FirstLevel))));
//

//		IncidenceSet incidence = graph.getIncidenceSet(r);
//		
//		for (HGHandle hgHandle : incidence) {
//			System.out.println((Hyperedge)graph.get(hgHandle));
//		}
//		HGHandle at=hg.findOne(graph, hg.and(hg.type(Atom.class), hg.eq("name", "bid")));
//		HGHandle at2=hg.findOne(graph, hg.and(hg.type(Atom.class), hg.eq("name", "pages")));
//		System.out.println(at);
//		List<Object> y = hg.getAll(graph, hg.and(hg.type(Relationship.class), hg.orderedLink(at2,at)));
//		
//		for (Object object : y) {
//			System.out.println(object);
//		}
	}

}