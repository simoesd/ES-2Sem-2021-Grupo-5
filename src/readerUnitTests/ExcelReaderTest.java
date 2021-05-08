package readerUnitTests;

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
		
    	
    	ArrayList<Line> lines = ExcelReader.readExcelFile("unitTestsUtilityFolder\\testeLine.xlsx");
    	
    	Assertions.assertAll("Line1 atributes",
				() -> {assertEquals(1, lines.get(0).getMethodID());},
				() -> {assertEquals("GUI", lines.get(0).getPkg());},
				() -> {assertEquals("MainWindow", lines.get(0).getCls());},
				() -> {assertEquals("MainWindow()", lines.get(0).getMethé());},
				() -> {assertEquals("583", lines.get(0).getMetrics().get("LOC_CLASS"));},
				() -> {assertEquals("3", lines.get(0).getMetrics().get("LOC_METHOD"));},
				() -> {assertEquals("0", lines.get(0).getMetrics().get("CYCLO_METHOD"));},
				() -> {assertEquals("38", lines.get(0).getMetrics().get("WMC_CLASS"));},
				() -> {assertEquals("13", lines.get(0).getMetrics().get("NOM_CLASS"));},
				() -> {assertEquals("true", lines.get(0).getMetrics().get("is_God_Class"));},
				() -> {assertEquals("pois", lines.get(0).getMetrics().get("Random"));},
				
				() -> {assertEquals(2, lines.get(1).getMethodID());},
				() -> {assertEquals("GUI", lines.get(1).getPkg());},
				() -> {assertEquals("MainWindow", lines.get(1).getCls());},
				() -> {assertEquals("MainWindow()", lines.get(1).getMethé());},
				() -> {assertEquals("583", lines.get(1).getMetrics().get("LOC_CLASS"));},
				() -> {assertEquals("3", lines.get(1).getMetrics().get("LOC_METHOD"));},
				() -> {assertEquals("0", lines.get(1).getMetrics().get("CYCLO_METHOD"));},
				() -> {assertEquals("38", lines.get(1).getMetrics().get("WMC_CLASS"));},
				() -> {assertEquals("13", lines.get(1).getMetrics().get("NOM_CLASS"));},
				() -> {assertEquals("true", lines.get(1).getMetrics().get("is_God_Class"));},
				() -> {assertEquals("pois", lines.get(1).getMetrics().get("Random"));},
				
    			() -> {assertNull(ExcelReader.readExcelFile("egtsrfdsg"));});
	}

}
