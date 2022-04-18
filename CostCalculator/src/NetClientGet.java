


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.generators.GenerateRandomDesign;
import edu.upc.essi.catalog.ops.Graphoperations;
import org.apache.commons.io.FileUtils;
import org.hypergraphdb.HGIndex;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.storage.BAtoHandle;
import org.json.JSONException;
import org.json.JSONObject;

public class NetClientGet {
	private static final String IDX_NAME = "subgraph.index";
	private static final String REVIDX_NAME = "revsubgraph.index";
	// http://localhost:8080/RESTfulExample/json/product/get
	public static void main(String[] args)  {

		try {
			HyperGraph g = GenerateRandomDesign.get();
			Graphoperations.printDesign(g);
//			HGIndex<HGPersistentHandle, HGPersistentHandle> y = g.getStore().getIndex(IDX_NAME);
//			y.close();
//			Graphoperations.unindex(g);
			g.close();
			g.open(g.getLocation());
			Graphoperations.printDesign(g);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		Graphoperations.addAtomtoGraph(g, new Relationship());
//		Graphoperations.addSetHyperedgetoGraph(g, "Test");
//		Thread.sleep(5000);
//		logger.info(g.getIndexManager().toString());
		;
//		g.close();
//		g.getStore().getIndex("subgraph.index").close();
//		g.getStore().getIndex("revsubgraph.index").close();

//		logger.info("sss");

//		FileUtils.cleanDirectory(new File(Const.HG_LOCATION_BOOK));

	}

}

