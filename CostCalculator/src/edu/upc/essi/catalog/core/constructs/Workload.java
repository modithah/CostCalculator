package edu.upc.essi.catalog.core.constructs;

import edu.upc.essi.catalog.ops.Graphoperations;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;

import java.util.ArrayList;

public class Workload {

    public static ArrayList<Pair<Double, ArrayList<Atom>>> getWorkload2(HyperGraph graph) {
        ArrayList<Atom> query = new ArrayList<>();
        query.add(graph.get(Graphoperations.getAtomByName(graph, "D_ID")));
        query.add(graph.get(Graphoperations.getAtomByName(graph, "W_ID")));
        query.add(graph.get(Graphoperations.getAtomByName(graph, "W_NAME")));
//        query.add(graph.get(Graphoperations.getAtomByName(graph, "P_ID")));
        Pair<Double, ArrayList<Atom>> p = new Pair<Double, ArrayList<Atom>>(0.2, query);

        ArrayList<Atom> query2 = new ArrayList<>();
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
        query2.add(graph.get(Graphoperations.getAtomByName(graph, "W_ID")));
        query2.add(graph.get(Graphoperations.getAtomByName(graph, "D_ID")));
        query2.add(graph.get(Graphoperations.getAtomByName(graph, "D_NAME")));

        Pair<Double, ArrayList<Atom>> p1 = new Pair<Double, ArrayList<Atom>>(0.2, query2);



        ArrayList<Atom> query3 = new ArrayList<>();
        query3.add(graph.get(Graphoperations.getAtomByName(graph, "D_ID")));
        query3.add(graph.get(Graphoperations.getAtomByName(graph, "D_NAME")));
//        query.add(graph.get(Graphoperations.getAtomByName(graph, "P_ID")));
        Pair<Double, ArrayList<Atom>> p3 = new Pair<Double, ArrayList<Atom>>(0.2, query3);

        ArrayList<Atom> query4 = new ArrayList<>();
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
        query4.add(graph.get(Graphoperations.getAtomByName(graph, "W_ID")));
        query4.add(graph.get(Graphoperations.getAtomByName(graph, "W_NAME")));

        Pair<Double, ArrayList<Atom>> p4 = new Pair<Double, ArrayList<Atom>>(0.2, query4);


        ArrayList<Atom> query5 = new ArrayList<>();
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
        query5.add(graph.get(Graphoperations.getAtomByName(graph, "D_ID")));
        query5.add(graph.get(Graphoperations.getAtomByName(graph, "D_NAME")));
        query5.add(graph.get(Graphoperations.getAtomByName(graph, "C_ID")));
        query5.add(graph.get(Graphoperations.getAtomByName(graph, "C_FIRST")));

        Pair<Double, ArrayList<Atom>> p5 = new Pair<Double, ArrayList<Atom>>(0.2, query5);



        ArrayList<Pair<Double, ArrayList<Atom>>> allQ = new ArrayList<>();
        allQ.add(p);
        allQ.add(p1);
        allQ.add(p3);
        allQ.add(p4);
        allQ.add(p5);
        return allQ;
    }

    public static ArrayList<Pair<Double, ArrayList<Atom>>> getWorkload(HyperGraph graph) {
        ArrayList<Atom> query = new ArrayList<>();
        query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
        query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
        query.add(graph.get(Graphoperations.getAtomByName(graph, "B_NAME")));
//        query.add(graph.get(Graphoperations.getAtomByName(graph, "P_ID")));
        Pair<Double, ArrayList<Atom>> p = new Pair<Double, ArrayList<Atom>>(0.25, query);

        ArrayList<Atom> query2 = new ArrayList<>();
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
        query2.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
        query2.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
        query2.add(graph.get(Graphoperations.getAtomByName(graph, "A_NAME")));
        
        Pair<Double, ArrayList<Atom>> p1 = new Pair<Double, ArrayList<Atom>>(0.25, query2);
        
        
        
        ArrayList<Atom> query3 = new ArrayList<>();
        query3.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
        query3.add(graph.get(Graphoperations.getAtomByName(graph, "A_NAME")));
//        query.add(graph.get(Graphoperations.getAtomByName(graph, "P_ID")));
        Pair<Double, ArrayList<Atom>> p3 = new Pair<Double, ArrayList<Atom>>(0.25, query3);

        ArrayList<Atom> query4 = new ArrayList<>();
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
        query4.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
        query4.add(graph.get(Graphoperations.getAtomByName(graph, "B_NAME")));
        
        Pair<Double, ArrayList<Atom>> p4 = new Pair<Double, ArrayList<Atom>>(0.25, query4);
        

        

        ArrayList<Pair<Double, ArrayList<Atom>>> allQ = new ArrayList<>();
        allQ.add(p);
        allQ.add(p1);
        allQ.add(p3);allQ.add(p4);
        return allQ;
    }

//   
}
