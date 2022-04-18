package edu.upc.essi.catalog.estocada;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Relationship;
import edu.upc.essi.catalog.enums.CardinalityEnum;

public class CreateGraph2 {

	static final Logger logger = Logger.getLogger(CreateGraph2.class);

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

		Atom book = new Atom("bid", 300);
		book.setSize(3);
		atoms.add(book);
		atomHandles.add(graph.add(book));
		Atom bname = new Atom("bname", String.class.getName());
		bname.setSize(5);
		atoms.add(bname);
		atomHandles.add(graph.add(bname));
		Atom pages = new Atom("pages", Integer.class.getName());
		pages.setSize(10);
		atoms.add(pages);
		atomHandles.add(graph.add(pages));

		Atom author = new Atom("Aid", 50);
		author.setSize(3);
		atoms.add(author);
		atomHandles.add(graph.add(author));
		Atom aname = new Atom("aname", String.class.getName());
		aname.setSize(15);
		atoms.add(aname);
		atomHandles.add(graph.add(aname));

		logger.info("Atoms Created Succesfully !");

		//
		// --------------- Creating Relationships ---------------//
		//
		try {
			logger.info("Creating Relationships");
			// Station
			relHandles.put(book, bname, graph.add(new Relationship("hasName", atomHandles.get(atoms.indexOf(book)),
					atomHandles.get(atoms.indexOf(bname)))));
			relHandles.put(book, pages, graph.add(new Relationship("hasPages", atomHandles.get(atoms.indexOf(book)),
					atomHandles.get(atoms.indexOf(pages)))));

			relHandles.put(author, aname, graph.add(new Relationship("hasName", atomHandles.get(atoms.indexOf(author)),
					atomHandles.get(atoms.indexOf(aname)))));

			relHandles.put(author, book, graph.add(new Relationship("wroteBook", CardinalityEnum.ONE_TO_MANY, 10,
					atomHandles.get(atoms.indexOf(author)), atomHandles.get(atoms.indexOf(book)))));

			relHandles.put(book, author, graph.add(new Relationship("hasAuthor", CardinalityEnum.ONE_TO_MANY, 2,
					atomHandles.get(atoms.indexOf(book)), atomHandles.get(atoms.indexOf(author)))));
			logger.info("Relationships Created Succesfully !");

			// -----Document Store-------//

//			HGHandle authorStruct = graph.add(new Hyperedge(graph, "", HyperedgeTypeEnum.Struct,
//					atomHandles.get(atoms.indexOf(author)), atomHandles.get(atoms.indexOf(aname))));
//			HGHandle authorSet = graph.add(new Hyperedge(graph, "Authors", HyperedgeTypeEnum.Set, authorStruct));
//
//			HGHandle booksecondLevel = graph
//					.add(new Hyperedge(graph, "", HyperedgeTypeEnum.SecondLevel, atomHandles.get(atoms.indexOf(book)),
//							atomHandles.get(atoms.indexOf(bname)), atomHandles.get(atoms.indexOf(pages)), authorSet,
//							relHandles.get(book, bname), relHandles.get(book, pages), relHandles.get(book, author)));
//
//			HGHandle bookFirstLevel = graph
//					.add(new Hyperedge(graph, "Books", HyperedgeTypeEnum.FirstLevel, booksecondLevel));

//			HGHandle docSecondLevel = graph.add(new Hyperedge("metros-trams", HyperedgeTypeEnum.SecondLevel,
//					atomHandles.get(atoms.indexOf(metro)), atomHandles.get(atoms.indexOf(tname)),
//					atomHandles.get(atoms.indexOf(tram)), atomHandles.get(atoms.indexOf(mname)),
//					atomHandles.get(atoms.indexOf(line)), atomHandles.get(atoms.indexOf(lname)),
//					relHandles.get(line, lname), relHandles.get(metro, mname), relHandles.get(tram, tname),
//					relHandles.get(station, line), routeSet));
//			HGHandle docFirstLevel = graph
//					.add(new Hyperedge("metros-trams", HyperedgeTypeEnum.FirstLevel, docSecondLevel));
//
//			HGHandle bookStruct = graph.add(new Hyperedge(graph, "", HyperedgeTypeEnum.Struct,
//					atomHandles.get(atoms.indexOf(book)), atomHandles.get(atoms.indexOf(bname)),
//					atomHandles.get(atoms.indexOf(pages)), relHandles.get(book, bname), relHandles.get(book, pages)));
//
//			HGHandle bookSet = graph.add(new Hyperedge(graph, "Books", HyperedgeTypeEnum.Set, bookStruct));
//
//			HGHandle authorSecond = graph
//					.add(new Hyperedge(graph, "", HyperedgeTypeEnum.SecondLevel, atomHandles.get(atoms.indexOf(author)),
//							atomHandles.get(atoms.indexOf(aname)), bookSet, relHandles.get(author, book)));
//			HGHandle authorfirst = graph
//					.add(new Hyperedge(graph, "Authors", HyperedgeTypeEnum.FirstLevel, authorSecond));
//
//			graph.add(new Hyperedge(graph, "MongoDB", HyperedgeTypeEnum.Database_Doc, bookFirstLevel));
//			graph.add(new Hyperedge(graph, "MongoDB-2", HyperedgeTypeEnum.Database_Doc, authorfirst));

			graph.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// logger.error(e.toString());
		}

	}

	private static HGHandle getRelationHandle(Atom a1, Atom a2, Table t) {
		HGHandle handle = null;
		Object o = t.get(a1, a2);
		if (o.equals(null))
			handle = (HGHandle) t.get(a2, a1);
		else
			handle = (HGHandle) t.get(a1, a2);
		return handle;
	}

}
