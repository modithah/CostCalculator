import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGLink;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.algorithms.GraphClassics;
import org.hypergraphdb.algorithms.HGBreadthFirstTraversal;
import org.hypergraphdb.algorithms.HGDepthFirstTraversal;
import org.hypergraphdb.algorithms.SimpleALGenerator;
import org.hypergraphdb.util.Pair;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.CSVRow;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.loaders.LoadGraph;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;
import edu.upc.essi.catalog.util.TargetSetALGenerator;

public class saveTest {

	// TODO Auto-generated method stub
	public static void main(String[] args) throws IllegalStateException, FileNotFoundException {

		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
		HGHandle root = Graphoperations.getHyperedgebyNameType("AUTHOR", HyperedgeTypeEnum.FirstLevel);// Graphoperations.getAtomByName(graph,
																								// "A_ID");
		HGHandle target = Graphoperations.getAtomByName(graph, "B_ID");
//		System.out.println(GraphClassics.dijkstra(root, target, new SimpleALGenerator(graph)));

		Transformations.findRelPath(graph, graph.get(root), graph.get(target));

	}
}
