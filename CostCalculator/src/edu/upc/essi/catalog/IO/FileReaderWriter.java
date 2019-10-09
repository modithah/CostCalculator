package edu.upc.essi.catalog.IO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.CSVRow;

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
}
