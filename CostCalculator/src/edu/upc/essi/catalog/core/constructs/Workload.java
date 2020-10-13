package edu.upc.essi.catalog.core.constructs;

import edu.upc.essi.catalog.ops.Graphoperations;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;

import java.util.ArrayList;

public class Workload {

    public static ArrayList<Pair<Double, ArrayList<Atom>>> getWorkload(HyperGraph graph) {
        ArrayList<Atom> query = new ArrayList<>();
        query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
        query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
        query.add(graph.get(Graphoperations.getAtomByName(graph, "P_ID")));
        Pair<Double, ArrayList<Atom>> p = new Pair<Double, ArrayList<Atom>>(0.5, query);

        ArrayList<Atom> query2 = new ArrayList<>();
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "A_ID")));
//			query.add(graph.get(Graphoperations.getAtomByName(graph, "B_ID")));
        query2.add(graph.get(Graphoperations.getAtomByName(graph, "P_ID")));
        Pair<Double, ArrayList<Atom>> p1 = new Pair<Double, ArrayList<Atom>>(0.5, query2);

        ArrayList<Pair<Double, ArrayList<Atom>>> allQ = new ArrayList<>();
        allQ.add(p);
        allQ.add(p1);
        return allQ;
    }

}
