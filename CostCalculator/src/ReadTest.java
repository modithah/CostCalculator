import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.CSVRow;
import edu.upc.essi.catalog.loaders.LoadGraph;

public class ReadTest {
	public static void main(String[] args) throws IllegalStateException, FileNotFoundException {
		LoadGraph.LoadBaseFromJSON("C:\\Users\\Moditha\\Documents\\PhD\\SVN\\Schemas\\booksample.json");
		CsvToBean<CSVRow> bb = new CsvToBeanBuilder<CSVRow>(new FileReader(Const.DESIGN_LOCATION+"booksample_design3.csv"))
				.withSeparator(',').withIgnoreLeadingWhiteSpace(true).withType(CSVRow.class).build();

		List<CSVRow> list = bb.parse();
		LoadGraph.LoadDesignFromCSV(list);
	}
}
