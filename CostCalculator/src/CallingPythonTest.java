import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;

import antlr.collections.List;
import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallingPythonTest {
static int count =0;
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
	public CallingPythonTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		 ProcessBuilder processBuilder = new ProcessBuilder("python", "C:\\Users\\Moditha\\Documents\\PhD\\SVN\\Code\\CostCalculator\\CostCalculator\\data\\python\\solve.py");
//		    processBuilder.redirectErrorStream(true);
//		 
//		    try {
//				Process process = processBuilder.start();
//				long start = System.nanoTime();
//				InputStream stream = process.getInputStream();
////				logger.info(process.waitFor());
////				String y = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
////				logger.info(y);
////				StringWriter writer = new StringWriter();
////		        IOUtils.copy(stream, writer, StandardCharsets.UTF_8);
////		        logger.info(writer.toString());
//		        logger.info((System.nanoTime() - start)/1E9);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
		    
//		    List<String> results = readProcessOutput(process.getInputStream());
//		 
//		    assertThat("Results should not be empty", results, is(not(empty())));
//		    assertThat("Results should contain output of script: ", results, hasItem(
//		      containsString("Hello Baeldung Readers!!")));
//		 
//		    int exitCode = process.waitFor();
//		    assertEquals("No errors should be detected", 0, exitCode);
		
		
//		HyperGraph graph = new HyperGraph(Const.HG_LOCATION_BOOK);
//		Transformations.getCostMinMax(graph, getWorkload(graph));
//		
//		logger.info(Boolean.parseBoolean("FalSe"));
//		List<Boolean> list= new ArrayList<>();
//		B
//		list.add("hekko");
//		los
////		Atom entry = atomlist.get(0);
		
		logger.info(String.valueOf(1/0));
		
	}
	
	static void print(String s){
	logger.info(s);
	s+=" ";
	}
	private static ArrayList<Pair<Double, ArrayList<Atom>>> getWorkload(HyperGraph graph) {
		ArrayList<Atom> query = new ArrayList<>();
		query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
		query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
		query.add(graph.get(Graphoperations.getAtomByName(graph, "P_ID")));
		Pair<Double, ArrayList<Atom>> p = new Pair<Double, ArrayList<Atom>>(0.5, query);

//		ArrayList<Atom> query2 = new ArrayList<>();
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
//		query2.add(graph.get(Graphoperations.getAtomByName(graph, "P_ID")));
//		Pair<Double, ArrayList<Atom>> p1 = new Pair<Double, ArrayList<Atom>>(0.5, query2);

		ArrayList<Pair<Double, ArrayList<Atom>>> allQ = new ArrayList<>();
		allQ.add(p);
//		allQ.add(p1);
		return allQ;
	}

}
