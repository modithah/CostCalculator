import java.util.Iterator;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;

import edu.upc.essi.catalog.core.constructs.Atom;
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
		System.out.println(g.CreateQuery(Graphoperations.getDBHyperedgebyType(HyperedgeTypeEnum.Database_Rel), ""));
	}

}
