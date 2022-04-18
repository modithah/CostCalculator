package edu.upc.essi.catalog.IO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.CSVRow;
import edu.upc.essi.catalog.loaders.LoadGraph;
import edu.upc.essi.catalog.ops.Graphoperations;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class FileReaderWriter {

	public static List<CSVRow> readFromCSV(String filename) {
		List<CSVRow> list = null;
		CsvToBean<CSVRow> bb;
		try {
			bb = new CsvToBeanBuilder<CSVRow>(new FileReader("C:\\Users\\Moditha\\Desktop\\test.csv"))
					.withSeparator(',').withIgnoreLeadingWhiteSpace(true).withType(CSVRow.class).build();

			list = bb.parse();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public static void writeToCSV(List<CSVRow> list, String filename) {

		try {
			FileWriter writer = new FileWriter(Const.DESIGN_LOCATION + filename + ".csv");
			CSVWriter csvWriter = new CSVWriter(writer);

			ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
			mappingStrategy.setType(CSVRow.class);

			StatefulBeanToCsvBuilder<CSVRow> builder = new StatefulBeanToCsvBuilder(csvWriter);
			StatefulBeanToCsv beanWriter = builder.withMappingStrategy(mappingStrategy).build();

			beanWriter.write(list);
			writer.close();
		} catch (CsvDataTypeMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvRequiredFieldEmptyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<Pair<Double, ArrayList<Atom>>> getWorkload(HyperGraph graph, String file) {

		ArrayList<Pair<Double, ArrayList<Atom>>> allQ = new ArrayList<>();

		JSONObject jo = null;
		try {
			jo = new JSONObject(new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8));
			JSONArray queries = jo.getJSONArray("queries");
			for (int i = 0; i < queries.length(); i++) {
				ArrayList<Atom> query = new ArrayList<>();
				JSONObject qjson = queries.getJSONObject(i);
				//				System.out.println(qjson.getString("selectName")+"  "+qjson.getDouble("frequency"));
				JSONArray projections=qjson.getJSONArray("projections");
				query.add(graph.get(Graphoperations.getAtomByName(graph, qjson.getString("selectName"))));
				for (int j = 0; j < projections.length(); j++) {
					query.add(graph.get(Graphoperations.getAtomByName(graph, projections.getString(j))));
				}
				Pair<Double, ArrayList<Atom>> p = new Pair<Double, ArrayList<Atom>>(qjson.getDouble("frequency"), query);
				allQ.add(p);
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return allQ;
	}

//	public static void main(String[] args) {
//		HyperGraph graph = new HyperGraph("C:\\Users\\Moditha\\Desktop\\jobs");
//		LoadGraph.LoadBaseFromJSONFile("data/schemas/rubis/rubis.json",graph);
//		List<Pair<Double, ArrayList<Atom>>> wl = getWorkload(graph, "C:\\Users\\Moditha\\Documents\\PhD\\SVN\\Code\\CostCalculator\\CostCalculator\\data\\schemas\\rubis\\workload.json");
//		System.out.println(wl);
//	}

}
