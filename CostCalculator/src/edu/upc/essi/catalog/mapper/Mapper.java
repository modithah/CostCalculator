package edu.upc.essi.catalog.mapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.AdjacencyList;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import edu.upc.essi.catalog.estocada.CreateGraph;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.query.QueryGenerator;

public class Mapper {
	
static final Logger logger = Logger.getLogger(CreateGraph.class);

static void getAllAttributes() {
	
}

public static void main(String[] args) throws FileNotFoundException {
	//System.out.println("Hello World");
	final Model model = ModelFactory.createDefaultModel();
        
        try {
        	//System.out.println("Hello before file");
           model.read(new FileInputStream("emp.owl"), null, "TURTLE");
           //model.read(new FileInputStream(args[1]), null, "TURTLE");
           // System.out.println("Hello file");
        } catch (Exception e) {
        	// System.out.println("Hello Exception");
        }
      
     // create map to store
        Map<String, String> map = new HashMap<String, String>();
        Map<String, List<String>> map2 = new HashMap<>(); 
             
        try {
			FileUtils.cleanDirectory(new File(Const.HG_LOCATION));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HyperGraph graph = new HyperGraph(Const.HG_LOCATION);
		ArrayList<Atom> atoms = new ArrayList<>();
		ArrayList<HGHandle> atomHandles = new ArrayList<>();
		Table<Atom, Atom, HGHandle> relHandles = HashBasedTable.create();
        
        StmtIterator iter;
        Statement stmt;
        Property predicate;
        Resource subject;
        RDFNode obj;
        iter = model.listStatements();
        ArrayList<String> list;
        
        while (iter.hasNext())
            {
            stmt =  (Statement) iter.next();
            subject = stmt.getSubject();
            predicate = stmt.getPredicate();
            obj = stmt.getObject();
            int idStr = subject.getURI().lastIndexOf('/');
            
           
           if(predicate.getLocalName().equals("domain"))
            	{
            	String[] parts = obj.toString().split("/");           	
            	if(map2.containsKey((parts[parts.length-1]))){
            	    // if the key has already been used,
            	    // we'll just grab the array list and add the value to it
            	    list = (ArrayList<String>) map2.get(parts[parts.length-1]);
            	    list.add(subject.getURI().substring(idStr+1));
            	    
            	    
            	    
            	} else {
            	    // if the key hasn't been used yet,
            	    // we'll create a new ArrayList<String> object, add the value
            	    // and put it in the array list with the new key
            	    list = new ArrayList<String>();
            	    list.add(subject.getURI().substring(idStr+1));
            	    map2.put(parts[parts.length-1], list);
            	    
            	}
            	
            	}
            //}
            
            }
           	      
        Set<String> keys = map2.keySet();
        logger.info("Creating Atoms");
        for (String key : keys) {
        	List<Atom> listAtoms = new ArrayList();
        	Atom atom = new Atom(key);
     		atoms.add(atom);
     		atomHandles.add(graph.add(atom));
     		 int flag=0;
     		 List<String> allAttributes = map2.get(key);
        	 for(String m:map2.get(key))
        	 {
        	    String mp="has"+m;
        		Atom sname = new Atom(m, String.class.getName());
        		listAtoms.add(sname);
          		atoms.add(sname);
          		atomHandles.add(graph.add(sname));
          		try {
           			logger.info("Creating Relationships");
           			relHandles.put(atom, sname, graph.add(new Relationship("hasName",
        					atomHandles.get(atoms.indexOf(atom)), atomHandles.get(atoms.indexOf(sname)))));
           		                 	
//        	 System.out.println(key+" "+ mp+" "+m);
        	 //System.out.println(concepts.indexOf(key)+"---"+attributes.indexOf(m));
        	 
          		
     		} catch (Exception e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     			// logger.error(e.toString());
     		}
        	 
        }
        	 int len = (allAttributes.size()*2)+1;
//        	 System.out.println("i am here" + len);
        	 HGHandle stationSecondHandle = null;
        	 HGHandle[] allRel = new HGHandle[len];
        	 allRel[0] = atomHandles.get(atoms.indexOf(atom));
        	 int i = 1;
        	 for(Atom a : listAtoms) {
        		
//        		 System.out.println("i am here" + atoms.indexOf(a));
        		 allRel[i] = atomHandles.get(atoms.indexOf(a));
        		 i++;
        		 allRel[i] = relHandles.get(atom, a);
        		 i++;
        	 }
        	 
        	 
//        	 stationSecondHandle = graph.add(new Hyperedge(graph,"Station", HyperedgeTypeEnum.SecondLevel,
//   					allRel));
//        	 
// 			HGHandle stationFirstHandle = graph
//  					.add(new Hyperedge(graph,"Station", HyperedgeTypeEnum.FirstLevel, stationSecondHandle));
////  			
// 			graph.add(new Hyperedge(graph,"PostgrSQL", HyperedgeTypeEnum.Database_Rel,stationFirstHandle));
// 			
// 		
// 			
//			HGHandle docFirstLevel = graph
//					.add(new Hyperedge(graph,"metros-trams", HyperedgeTypeEnum.FirstLevel, stationSecondHandle));
//			graph.add(new Hyperedge(graph,"MongoDB", HyperedgeTypeEnum.Database_Doc, docFirstLevel));
// 	 
          		logger.info("Relationships Created Succesfully !");
    	
	}
        List<String> atomss = Graphoperations.getAllAtoms(graph);

		if (atoms.isEmpty()) {
//			System.out.println("No data available Creating the Graph");
			CreateGraph.main(null);
			try {
				Thread.sleep(2000);
//				atomss = Graphoperations.getAllAtoms();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

//		System.out.println("The following atoms are available. data in k-v stores cannot be prsented in a query (bid,bname)");
//		System.out.println(atomss);
    	QueryGenerator g = new QueryGenerator();
		Scanner scanner = new Scanner(System.in);
//		System.out.print(
//				"Enter the atom names you want to query seperated by commas (type exit to quit): ");

		String input = scanner.next();
		while (!input.equals("exit")) {
//			System.out.println("------Queries for selected atoms------");

			AdjacencyList mapp= Graphoperations.makeHashmap(input.split(","));
 
			Set<Element> keyss = mapp.getMap().keySet();

			for (Element element : keyss) {
				if (element instanceof Hyperedge) {
					if (((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Doc)
							|| ((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Rel)
							|| ((Hyperedge) element).getType().equals(HyperedgeTypeEnum.Database_Col)) {
						g.CreateQueryFromMap(element, "", mapp, ((Hyperedge) element).getType());
					}
				}
			}
			input = scanner.next();
		}

}
}
