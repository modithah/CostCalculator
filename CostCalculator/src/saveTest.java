import java.io.FileNotFoundException;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;

public class saveTest {

	// TODO Auto-generated method stub
	public static void main(String[] args) throws IllegalStateException, FileNotFoundException {

		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
		HGHandle root = Graphoperations.getHyperedgebyNameType(graph,"AUTHOR", HyperedgeTypeEnum.FirstLevel);// Graphoperations.getAtomByName(graph,
																								// "A_ID");
		HGHandle target = Graphoperations.getAtomByName(graph, "B_ID");
//		logger.info(GraphClassics.dijkstra(root, target, new SimpleALGenerator(graph)));

		Transformations.findRelPath(graph, graph.get(root), graph.get(target));

	}
}
