package readerUnitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reader.Line;

class LineTest {
	
	Line line;
	
	@BeforeEach
	void setup() {
		LinkedHashMap<String, String> metrics = new LinkedHashMap<>();
		metrics.put("LOC_CLASS", "583");
		metrics.put("LOC_METHOD", "3");
		metrics.put("CYCLO_METHOD", "0");
		metrics.put("WMC_CLASS", "38");
		metrics.put("NOM_CLASS", "13");
		
		line = new Line(1, "GUI", "MainWindow", "MainWindow()", metrics);
	}
	
	@Test
	void toArrayTest() {
		String[] sArray = {"1", "GUI", "MainWindow", "MainWindow()", "583", "3", "0", "38", "13"};
		String[] fArray = line.toArray();
		
		Assertions.assertArrayEquals(sArray, fArray);
	}
	
	@Test
	void setValuesTest() {
		try {
			File excel = new File("testeLine.xlsx"); 
	    	FileInputStream fis = new FileInputStream(excel); 
	    	XSSFWorkbook excelWorkbook = new XSSFWorkbook(fis); 
	    	XSSFSheet excelSheet = excelWorkbook.getSheetAt(0); 
	    	Iterator<Row> rowIterator = excelSheet.iterator();
	    	
	    	LinkedHashMap<String, String> metrics = new LinkedHashMap<>();
	    	metrics.put("LOC_CLASS", "583");
	    	metrics.put("LOC_METHOD", "3");
	    	metrics.put("CYCLO_METHOD", "0");
	    	metrics.put("WMC_CLASS", "38");
	    	metrics.put("NOM_CLASS", "13");
	    	metrics.put("is_God_Class", "TRUE");
	    	metrics.put("Random", "pois");
	    	 
			Row columns = rowIterator.next();
			Iterator<Cell> columnIterator = columns.cellIterator();
			
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			Line line1 = new Line();
			line1.setValues(columnIterator, cellIterator);
			
			Row row2 = rowIterator.next();
			Iterator<Cell> cellIterator2 = row2.cellIterator();
			Line line2 = new Line();
			System.out.println("bla");
			Iterator<Cell> columnIterator2 = columns.cellIterator();
			line2.setValues(columnIterator2, cellIterator2);
			
			System.out.println("blo");
			
			excelWorkbook.close();
			
			Assertions.assertAll("Line1 atributes",
					() -> {assertEquals(1, line1.getMethodID());},
					() -> {assertEquals("GUI", line1.getPkg());},
					() -> {assertEquals("MainWindow", line1.getCls());},
					() -> {assertEquals("MainWindow()", line1.getMethé());},
					() -> {assertEquals("583", line1.getMetrics().get("LOC_CLASS"));},
					() -> {assertEquals("3", line1.getMetrics().get("LOC_METHOD"));},
					() -> {assertEquals("0", line1.getMetrics().get("CYCLO_METHOD"));},
					() -> {assertEquals("38", line1.getMetrics().get("WMC_CLASS"));},
					() -> {assertEquals("13", line1.getMetrics().get("NOM_CLASS"));},
					() -> {assertEquals("true", line1.getMetrics().get("is_God_Class"));},
					() -> {assertEquals("pois", line1.getMetrics().get("Random"));});
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void metricsToArrayTest() {
		String[] sArray = {"583", "3", "0", "38", "13"};
		String[] fArray = line.metricsToArray();
		
		Assertions.assertArrayEquals(sArray, fArray);
    }
	
	@Test
	void getColumnNamesTest() {
		String[] sArray = {"MethodID", "Package", "Class", "Method", "LOC_CLASS", "LOC_METHOD", "CYCLO_METHOD", "WMC_CLASS", "NOM_CLASS"};
		String[] fArray = line.getColumnNames();
		
		Assertions.assertArrayEquals(sArray, fArray);
	}
	
	@Test
	void getMetricNamesTest() {
		String[] sArray = {"LOC_CLASS", "LOC_METHOD", "CYCLO_METHOD", "WMC_CLASS", "NOM_CLASS"};
		String[] fArray = line.getMetricNames();
		
		Assertions.assertArrayEquals(sArray, fArray);
    }
	
	@Test
	void getCaseInsensitiveMetricTest() {
		Assertions.assertEquals("0", line.getCaseInsensitiveMetric("cyClo_method"));
		Assertions.assertEquals(null, line.getCaseInsensitiveMetric("cyClo_methodas"));
	}

}
