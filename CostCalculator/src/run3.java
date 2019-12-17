import java.io.FileNotFoundException;
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
import edu.upc.essi.catalog.cost.CostGenerator2;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.estocada.CreateGraph;
import edu.upc.essi.catalog.estocada.CreateGraph2;
import edu.upc.essi.catalog.loaders.LoadGraph;
import edu.upc.essi.catalog.ops.CostOperations;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.SchemaOperations;
import edu.upc.essi.catalog.query.QueryGenerator;

public class run3 {

	public run3() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IllegalStateException, FileNotFoundException {
		// TODO Auto-generated method stub

		ReadTest.main(args);

		List<Hyperedge> designs = Graphoperations.getAllDesigns();

//		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels();
//		System.out.println(firstLevels);
		CostGenerator2 c = new CostGenerator2();

		for (Hyperedge hyp : designs) {
			
			System.out.println("============================"+ hyp.getName() + "==================");
			List<Hyperedge> firstLevels = Graphoperations.GetFirstLevelsOfDesign(hyp);
			for (Hyperedge hyperedge : firstLevels) {
				c.CalculateSize(hyperedge, HyperedgeTypeEnum.Database_Doc);
			}
			
		}

	}

}
