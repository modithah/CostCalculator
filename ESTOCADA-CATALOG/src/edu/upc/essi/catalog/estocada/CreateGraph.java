package edu.upc.essi.catalog.estocada;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.indexing.ByPartIndexer;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Relationship;

public class CreateGraph {

	static final Logger logger = Logger.getLogger(CreateGraph.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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

		// HGHandle labeledType =
		// graph.getTypeSystem().getTypeHandle(Relationship.class);
		// graph.getIndexManager().register(new ByPartIndexer<Relationship>(labeledType,
		// "Relationship"));

		// --------------- Creating Atoms ---------------//
		// Station
		logger.info("Creating Atoms");

		Atom station = new Atom("Station");
		atoms.add(station);
		atomHandles.add(graph.add(station));
		Atom sname = new Atom("Sname", String.class.getName());
		atoms.add(sname);
		atomHandles.add(graph.add(sname));
		Atom spos = new Atom("pos", Integer.class.getName());
		atoms.add(spos);
		atomHandles.add(graph.add(spos));

		// Line

		Atom line = new Atom("Line");
		atoms.add(line);
		atomHandles.add(graph.add(line));
		Atom lname = new Atom("lname");
		atoms.add(lname);
		atomHandles.add(graph.add(lname));

		// Bus
		Atom bus = new Atom("Bus");
		atoms.add(bus);
		atomHandles.add(graph.add(bus));
		Atom bname = new Atom("bname");
		atoms.add(bname);
		atomHandles.add(graph.add(bname));

		// Train
		Atom train = new Atom("Train");
		atoms.add(train);
		atomHandles.add(graph.add(train));
		Atom rname = new Atom("rname");
		atoms.add(rname);
		atomHandles.add(graph.add(rname));

		// Tram
		Atom tram = new Atom("Tram");
		atoms.add(tram);
		atomHandles.add(graph.add(tram));
		Atom tname = new Atom("tname");
		atoms.add(tname);
		atomHandles.add(graph.add(tname));

		// Metro
		Atom metro = new Atom("Metro");
		atoms.add(metro);
		atomHandles.add(graph.add(metro));
		Atom mname = new Atom("mname");
		atoms.add(mname);
		atomHandles.add(graph.add(mname));

		logger.info("Atoms Created Succesfully !");

		//
		// --------------- Creating Relationsips ---------------//
		// Station
		try {
			logger.info("Creating Relationships");
			// Station
			relHandles.put(station, sname, graph.add(new Relationship("hasName",
					atomHandles.get(atoms.indexOf(station)), atomHandles.get(atoms.indexOf(sname)))));
			relHandles.put(station, spos, graph.add(new Relationship("hasPosition",
					atomHandles.get(atoms.indexOf(station)), atomHandles.get(atoms.indexOf(spos)))));
			// Line
			relHandles.put(line, lname, graph.add(new Relationship("hasName", atomHandles.get(atoms.indexOf(line)),
					atomHandles.get(atoms.indexOf(lname)))));
			// Bus
			relHandles.put(bus, bname, graph.add(new Relationship("hasName", atomHandles.get(atoms.indexOf(bus)),
					atomHandles.get(atoms.indexOf(bname)))));
			// Train
			relHandles.put(train, rname, graph.add(new Relationship("hasName", atomHandles.get(atoms.indexOf(train)),
					atomHandles.get(atoms.indexOf(rname)))));
			// Tram
			relHandles.put(tram, tname, graph.add(new Relationship("hasName", atomHandles.get(atoms.indexOf(tram)),
					atomHandles.get(atoms.indexOf(mname)))));
			// Metro
			relHandles.put(metro, tname, graph.add(new Relationship("hasName", atomHandles.get(atoms.indexOf(metro)),
					atomHandles.get(atoms.indexOf(mname)))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.toString());
		}

		logger.info("Relationships Created Succesfully !");
		//
	}

	private static HGHandle getRelationHandle(Atom a1, Atom a2, Table t) {
		HGHandle handle = null;
		Object o = t.get(a1, a2);
		if (o == null)
			handle = (HGHandle) t.get(a2, a1);
		else
			handle = (HGHandle) t.get(a1, a2);
		return handle;
	}

}
