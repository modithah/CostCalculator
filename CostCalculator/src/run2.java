import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Set;

import org.hypergraphdb.HyperGraph;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.AdjacencyList;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.cost.CostGenerator;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.estocada.CreateGraph2;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.query.QueryGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class run2 {

	public run2() {
		// TODO Auto-generated constructor stub
	}
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HyperGraph graph=new HyperGraph(Const.HG_LOCATION_BOOK);
		List<String> atoms = Graphoperations.getAllAtoms(graph);

		if (atoms.isEmpty()) {
			logger.info("No data available Creating the Graph");
			CreateGraph2.main(null);
			try {
				Thread.sleep(2000);
				atoms = Graphoperations.getAllAtoms(graph);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		logger.info(
				"The following atoms are available. data in k-v stores cannot be prsented in a query (bid,bname)");
		// logger.info(atoms);
		CostGenerator c = new CostGenerator();
		QueryGenerator q = new QueryGenerator();
//		Scanner scanner = new Scanner(System.in);
//		System.out.print(
//				"Enter the atom names you want to query seperated by commas (type exit to quit): ");

		String input = "A_ID,P_ID,B_ID,A_NAME,B_NAME,P_NAME";
//		String input = scanner.next();

//		while (!input.equals("exit")) {
//			logger.info("------Queries for selected atoms------");
//
		AdjacencyList map = Graphoperations.makeHashmap(input.split(","));

		logger.info(map.toString());
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
		Set<Element> keys = map.getMap().keySet();

		for (Element element : keys) {
			if (element instanceof Hyperedge) {
				if (((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Doc)
						|| ((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Rel)
						|| ((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Col)) {
					q.CreateSchemaFromMap(element, "", map, ((Hyperedge) element).getType());
					c.CreateCostFromMap(graph,element, "", map, ((Hyperedge) element).getType());
//				LoadGraph.SaveDesignToCSV((Hyperedge) element, "booksample_design1");
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

//		logger.info(g.CreateQuery(Graphoperations.getDBHyperedgebyType(HyperedgeTypeEnum.Database_Doc), ""));

//		HyperGraph graph = Const.graph;
//		HGHandle r = hg.findOne(graph, hg.and(hg.type(Hyperedge.class), hg.eq("type", HyperedgeTypeEnum.FirstLevel)));
//		CostOperations c = new CostOperations();
//
//		

		// c.CalculateCounts(r);

//
//		graph.getIncidenceSet(handle);

		// logger.info((Hyperedge) hg.getOne(graph,
		// hg.and(hg.type(Hyperedge.class), hg.eq("type",
		// HyperedgeTypeEnum.FirstLevel))));
//

//		IncidenceSet incidence = graph.getIncidenceSet(r);
//		
//		for (HGHandle hgHandle : incidence) {
//			logger.info((Hyperedge)graph.get(hgHandle));
//		}
//		HGHandle at=hg.findOne(graph, hg.and(hg.type(Atom.class), hg.eq("name", "bid")));
//		HGHandle at2=hg.findOne(graph, hg.and(hg.type(Atom.class), hg.eq("name", "pages")));
//		logger.info(at);
//		List<Object> y = hg.getAll(graph, hg.and(hg.type(Relationship.class), hg.orderedLink(at2,at)));
//		
//		for (Object object : y) {
//			logger.info(object);
//		}
	}

}
