import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;

public class DepthandHeterogeniety {

	public DepthandHeterogeniety() {
		// TODO Auto-generated constructor stub

	}

	public static void main(String[] args) {
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
		System.out.println(CalculateDepth(graph));
		System.out.println(CalculateHeterogeniety(graph));
//		Graphoperations.printDesign();
	}

	private static double CalculateDepth(HyperGraph graph) {
		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph);
		double max = 0.0;
		Queue<Pair<Hyperedge, Double>> queue = new LinkedList<Pair<Hyperedge, Double>>();
		for (Hyperedge hyperedge : firstLevels) {
			queue.add(new Pair<Hyperedge, Double>(hyperedge, 1.0));
		}

		while (!queue.isEmpty()) {
			Pair<Hyperedge, Double> val = queue.poll();
			if (val.getSecond() > max) {
				max = val.getSecond();
			}
			val.getFirst().findAll().forEach(cand -> {
				Element candE = graph.get(cand); // inner elements
				if (candE instanceof Hyperedge && ((((Hyperedge) candE).getType() == HyperedgeTypeEnum.Struct))) {
					queue.add(new Pair<Hyperedge, Double>((Hyperedge) candE, val.getSecond() + 1));

				} else if (candE instanceof Hyperedge && ((((Hyperedge) candE).getType() == HyperedgeTypeEnum.Set)
						|| (((Hyperedge) candE).getType() == HyperedgeTypeEnum.SecondLevel))) {
					queue.add(new Pair<Hyperedge, Double>((Hyperedge) candE, val.getSecond()));
				}
			});
		}
		return max;
	}

	private static double CalculateHeterogeniety(HyperGraph graph) {
		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph);
		double max = 0.0;
		Queue<Hyperedge> queue = new LinkedList<Hyperedge>();
		for (Hyperedge hyperedge : firstLevels) {
			queue.add(hyperedge);
		}

		while (!queue.isEmpty()) {
			Hyperedge val = queue.poll();
			double local = 0;
			for (HGHandle handle : val.findAll()) {
				Element candE = graph.get(handle); // inner elements
				if (candE instanceof Hyperedge) {
					queue.add((Hyperedge) candE);
					if (((Hyperedge) candE).getType() == HyperedgeTypeEnum.Struct) {
						local = local + 1;
					}
				}
			}
			if (local > max) {
				max = local;
			}
		}
		return max;
	}

}
