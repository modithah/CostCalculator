import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.CSVRow;
import edu.upc.essi.catalog.loaders.LoadGraph;
import edu.upc.essi.catalog.ops.Graphoperations;

public class pathTest {

	// TODO Auto-generated method stub
	public static void main(String[] args) throws IllegalStateException, FileNotFoundException {
//		LoadGraph.LoadBaseFromJSONFile("C:\\Users\\Moditha\\Documents\\PhD\\SVN\\Schemas\\demo\\booksample.json");
//		Graphoperations.changeDir("C:\\hyper\\test\\3");
		CsvToBean<CSVRow> bb = new CsvToBeanBuilder<CSVRow>(
				new FileReader(Const.DESIGN_LOCATION + "books-nested.csv")).withSeparator(',')
						.withIgnoreLeadingWhiteSpace(true).withType(CSVRow.class).build();

//		List<CSVRow> list = bb.parse();
//		LoadGraph.LoadDesignFromCSV2(list, "design1");
//		Graphoperations.changeDir("C:\\hyper\\test\\2");
//		CsvToBean<CSVRow> bb1 = new CsvToBeanBuilder<CSVRow>(
//				new FileReader(Const.DESIGN_LOCATION + "books-nested.csv")).withSeparator(',')
//						.withIgnoreLeadingWhiteSpace(true).withType(CSVRow.class).build();
//
//		List<CSVRow> list1 = bb1.parse();
//		LoadGraph.LoadDesignFromCSV(list1, "design1");
//		LoadGraph.LoadDesignFromCSV(list, "design 2");
	}
}
