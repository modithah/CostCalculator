import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import edu.upc.essi.catalog.core.constructs.CSVRow;

public class jsontest {
	public static void main(String[] args) throws JSONException, IOException {

		CsvToBean<CSVRow> bb = new CsvToBeanBuilder<CSVRow>(new FileReader("C:\\Users\\Moditha\\Desktop\\Employee.csv"))
				.withSeparator(',').withIgnoreLeadingWhiteSpace(true).withType(CSVRow.class).build();

		List<CSVRow> list = bb.parse();

//		System.out.println(bb.);
//		for (CSVRow csvRow : list) {
//			System.out.println(csvRow);
//		}

		list.stream().filter(x -> x.getType().equals("C_Atom")).forEach(y -> {
			System.out.println(y.getNode());
		});

//		FileWriter writer = new FileWriter("C:\\Users\\Moditha\\Desktop\\Employee.csv");
//		CSVWriter csvWriter = new CSVWriter(writer);
//
//		ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
//		mappingStrategy.setType(CSVRow.class);
//
//		// Arrange column name as provided in below array.
//
////		mappingStrategy.set;
//
//		// Createing StatefulBeanToCsv object
//		StatefulBeanToCsvBuilder<CSVRow> builder = new StatefulBeanToCsvBuilder(csvWriter);
//		StatefulBeanToCsv beanWriter = builder.withMappingStrategy(mappingStrategy).build();
//
//		// Write list to StatefulBeanToCsv object
//		try {
//			beanWriter.write(list);
//			writer.close();
//		} catch (CsvDataTypeMismatchException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (CsvRequiredFieldEmptyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// closing the writer object
//		
	}
}
