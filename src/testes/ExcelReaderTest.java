package testes;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import reader.ExcelReader;
import reader.Line;

class ExcelReaderTest {

	@Test
	void readExcelFileTest() {
		LinkedHashMap<String, String> metrics = new LinkedHashMap<>();
    	metrics.put("LOC_CLASS", "583");
    	metrics.put("LOC_METHOD", "3");
    	metrics.put("CYCLO_METHOD", "0");
    	metrics.put("WMC_CLASS", "38");
    	metrics.put("NOM_CLASS", "13");
    	metrics.put("is_God_Class", "TRUE");
    	metrics.put("Random", "pois");
		
//    	list.add(new Line(1, "GUI", "MainWindow", "MainWindow()", metrics));
//    	list.add(new Line(2, "GUI", "MainWindow", "MainWindow()", metrics));
    	
    	ArrayList<Line> list = ExcelReader.readExcelFile("testeLine.xlsx");
    	
    	Assertions.assertAll("Line1 atributes",
				() -> {assertEquals(1, list.get(0).getMethodID());},
				() -> {assertEquals("GUI", list.get(0).getPkg());},
				() -> {assertEquals("MainWindow", list.get(0).getCls());},
				() -> {assertEquals("MainWindow()", list.get(0).getMethé());},
				() -> {assertEquals("583", list.get(0).getMetrics().get("LOC_CLASS"));},
				() -> {assertEquals("3", list.get(0).getMetrics().get("LOC_METHOD"));},
				() -> {assertEquals("0", list.get(0).getMetrics().get("CYCLO_METHOD"));},
				() -> {assertEquals("38", list.get(0).getMetrics().get("WMC_CLASS"));},
				() -> {assertEquals("13", list.get(0).getMetrics().get("NOM_CLASS"));},
				() -> {assertEquals("true", list.get(0).getMetrics().get("is_God_Class"));},
				() -> {assertEquals("pois", list.get(0).getMetrics().get("Random"));},
				
				() -> {assertEquals(2, list.get(1).getMethodID());},
				() -> {assertEquals("GUI", list.get(1).getPkg());},
				() -> {assertEquals("MainWindow", list.get(1).getCls());},
				() -> {assertEquals("MainWindow()", list.get(1).getMethé());},
				() -> {assertEquals("583", list.get(1).getMetrics().get("LOC_CLASS"));},
				() -> {assertEquals("3", list.get(1).getMetrics().get("LOC_METHOD"));},
				() -> {assertEquals("0", list.get(1).getMetrics().get("CYCLO_METHOD"));},
				() -> {assertEquals("38", list.get(1).getMetrics().get("WMC_CLASS"));},
				() -> {assertEquals("13", list.get(1).getMetrics().get("NOM_CLASS"));},
				() -> {assertEquals("true", list.get(1).getMetrics().get("is_God_Class"));},
				() -> {assertEquals("pois", list.get(1).getMetrics().get("Random"));},
				
    			() -> {assertNull(ExcelReader.readExcelFile("egtsrfdsg"));});
	}

}
