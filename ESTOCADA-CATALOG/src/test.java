import java.util.Iterator;
import java.util.Set;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;

import edu.upc.essi.catalog.core.constructs.AdjacencyList;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.query.QueryGenerator;

public class test {

	public test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// HyperGraph graph = new HyperGraph("C:\\hyper\\estocada");
		// for (Object s : hg.getAll(graph, hg.type(Hyperedge.class)))
		// System.out.println(s);

		// Hyperedge e =
		// Graphoperations.getDBHyperedgebyType(HyperedgeTypeEnum.Database_Rel);
		// Iterator<HGHandle> x = e.iterator();
		//
		// while (x.hasNext()) {
		// System.out.println(Graphoperations.getElementbyHandle(x.next()));
		//
		// }

		QueryGenerator g = new QueryGenerator();
//
//		System.out.println("------Queries for all atoms------");
//		Iterator<HGHandle> x = ((Hyperedge) Graphoperations.getDBHyperedgebyType(HyperedgeTypeEnum.Database_Rel))
//				.iterator();
//		while (x.hasNext()) {
//			System.out.println(g.CreateQuery(Graphoperations.getElementbyHandle(x.next()), ""));
//		}
	//	System.out.println(g.CreateQuery(Graphoperations.getDBHyperedgebyType(HyperedgeTypeEnum.Database_Rel), ""));

		
		System.out.println("------Queries for selected atoms------");
		
		AdjacencyList map = Graphoperations.makeHashmap("lname","Lid");

		Set<Element> keys = map.getMap().keySet();

		for (Element element : keys) {
			if (element instanceof Hyperedge) {
				if (((Hyperedge) element).getType().equals(HyperedgeTypeEnum.FirstLevel)) {
					System.out.println(g.CreateQueryFromMap(element, "", map));
				}
			}
		}
	}

}
