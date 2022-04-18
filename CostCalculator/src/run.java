import java.lang.invoke.MethodHandles;
import java.util.List;

import org.hypergraphdb.HyperGraph;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.estocada.CreateGraph;
import edu.upc.essi.catalog.ops.Graphoperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class run {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
	public run() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		Graphoperations.printDesign();

		logger.info("FFFFFFFF");
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
		logger.info(String.valueOf(Graphoperations.isConsistant(graph)));
		List<String> atoms = Graphoperations.getAllAtoms(graph);

		if (atoms.isEmpty()) {
			logger.info("No data available Creating the Graph");
			CreateGraph.main(null);
			try {
				Thread.sleep(2000);
				atoms = Graphoperations.getAllAtoms(graph);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

//		logger.info(
//				"The following atoms are available. data in k-v stores cannot be prsented in a query (bid,bname)");
//		logger.info(atoms);
//		QueryGenerator g = new QueryGenerator();
//		Scanner scanner = new Scanner(System.in);
//		System.out.print("Enter the atom names you want to query seperated by commas (type exit to quit): ");

//		String input = scanner.next();
//		while (!input.equals("exit")) {
//			logger.info("------Queries for selected atoms------");
//
////			String input1 = "A_ID,B_ID,AB_ID";
//			AdjacencyList map = Graphoperations.makeHashmap(input.split(","));
//			
//			Set<Element> keys = map.getMap().keySet();
//
//			logger.info(keys);
//			for (Element element : keys) {
//				if (element instanceof Hyperedge) {
////					if (((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Doc)
////							|| ((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Rel)
////							|| ((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Col)) {
////						g.CreateSchemaFromMap(element, "", map, ((Hyperedge) element).getType());
////					((Hyperedge) element).print();
////					}
//					logger.info(element);
//				}
//			}
//			input = scanner.next();
//		}

	}

}
