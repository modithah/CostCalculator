import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.hypergraphdb.HyperGraph;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.AdjacencyList;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.estocada.CreateGraph;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.query.QueryGenerator;

public class run {

	public run() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		Graphoperations.printDesign();

		System.out.println("FFFFFFFF");

		System.out.println(Graphoperations.isConsistant(new HyperGraph(Const.HG_LOCATION_BOOK)));
		List<String> atoms = Graphoperations.getAllAtoms();

		if (atoms.isEmpty()) {
			System.out.println("No data available Creating the Graph");
			CreateGraph.main(null);
			try {
				Thread.sleep(2000);
				atoms = Graphoperations.getAllAtoms();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

//		System.out.println(
//				"The following atoms are available. data in k-v stores cannot be prsented in a query (bid,bname)");
//		System.out.println(atoms);
//		QueryGenerator g = new QueryGenerator();
//		Scanner scanner = new Scanner(System.in);
//		System.out.print("Enter the atom names you want to query seperated by commas (type exit to quit): ");

//		String input = scanner.next();
//		while (!input.equals("exit")) {
//			System.out.println("------Queries for selected atoms------");
//
////			String input1 = "A_ID,B_ID,AB_ID";
//			AdjacencyList map = Graphoperations.makeHashmap(input.split(","));
//			
//			Set<Element> keys = map.getMap().keySet();
//
//			System.out.println(keys);
//			for (Element element : keys) {
//				if (element instanceof Hyperedge) {
////					if (((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Doc)
////							|| ((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Rel)
////							|| ((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Col)) {
////						g.CreateSchemaFromMap(element, "", map, ((Hyperedge) element).getType());
////					((Hyperedge) element).print();
////					}
//					System.out.println(element);
//				}
//			}
//			input = scanner.next();
//		}

	}

}
