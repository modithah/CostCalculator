import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.loaders.LoadGraph;
import edu.upc.essi.catalog.metadata.GenerateMetadata;
import edu.upc.essi.catalog.ops.Graphoperations;
import org.apache.commons.io.FileUtils;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

public class multiAtomCollection {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    public static void main(String[] args) {
//        File serverDir = new File(Const.HG_LOCATION_BOOK );
        try {
//            HyperGraph graph = getHyperGraph();
            HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
            GenerateMetadata metadataGen = new GenerateMetadata();
            metadataGen.setSizeandMultipliers(graph);
            Graphoperations.printDesign(graph);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static HyperGraph getHyperGraph() throws Exception {
        FileUtils.cleanDirectory(new File(Const.HG_LOCATION_BOOK ));
        HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
        LoadGraph.LoadBaseFromJSONFile("data/schemas/booksample2.json", graph);
        ArrayList<Atom> allAtoms = new ArrayList<>();
        ArrayList<Relationship> allRels = new ArrayList<>();
        allAtoms.addAll(Graphoperations.getClassAtomList(graph));
        allRels.addAll(Graphoperations.getClassRelList(graph));

        ArrayList<HGHandle> handles = new ArrayList<>();
        allAtoms.forEach(a -> {
            handles.add(graph.getHandle(a));
        });
        allRels.forEach(a -> {
            handles.add(graph.getHandle(a));
        });
        HGHandle structhandle = Graphoperations.addHyperedgetoGraph(graph, "Struct",
                HyperedgeTypeEnum.SecondLevel, handles.toArray(new HGHandle[handles.size()]));
        logger.info(graph.get(((Hyperedge)graph.get(structhandle)).getRoot()).toString());
        HGHandle set = Graphoperations.addHyperedgetoGraph(graph, "Set",
                HyperedgeTypeEnum.FirstLevel, structhandle);
        Graphoperations.addHyperedgetoGraph(graph, "design", HyperedgeTypeEnum.Database_Doc,
                set);
        return graph;
    }
}
