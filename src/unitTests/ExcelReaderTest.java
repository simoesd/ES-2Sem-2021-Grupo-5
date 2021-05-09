package unitTests;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reader.ExcelReader;
import reader.Line;

class ExcelReaderTest {
    
    LinkedHashMap<String, String> metrics;
    
    @BeforeEach
    void setUp()
    {
        metrics = new LinkedHashMap<>();
        metrics.put("LOC_CLASS", "583");
        metrics.put("LOC_METHOD", "3");
        metrics.put("CYCLO_METHOD", "0");
        metrics.put("WMC_CLASS", "38");
        metrics.put("NOM_CLASS", "13");
        metrics.put("is_God_Class", "TRUE");
        metrics.put("Random", "pois");
    }
    
    @Test
    void readExcelFileTest() {
        
        ArrayList<Line> lines = ExcelReader.readExcelFile("unitTestFiles\\testeLine.xlsx");
        
        Assertions.assertAll("Line1 atributes",
            () -> {assertEquals(1, lines.get(0).getMethodID());},
            () -> {assertEquals("GUI", lines.get(0).getPkg());},
            () -> {assertEquals("MainWindow", lines.get(0).getCls());},
            () -> {assertEquals("MainWindow()", lines.get(0).getMethod());},
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
            () -> {assertEquals("MainWindow()", lines.get(1).getMethod());},
            () -> {assertEquals("583", lines.get(1).getMetrics().get("LOC_CLASS"));},
            () -> {assertEquals("3", lines.get(1).getMetrics().get("LOC_METHOD"));},
            () -> {assertEquals("0", lines.get(1).getMetrics().get("CYCLO_METHOD"));},
            () -> {assertEquals("38", lines.get(1).getMetrics().get("WMC_CLASS"));},
            () -> {assertEquals("13", lines.get(1).getMetrics().get("NOM_CLASS"));},
            () -> {assertEquals("true", lines.get(1).getMetrics().get("is_God_Class"));},
            () -> {assertEquals("pois", lines.get(1).getMetrics().get("Random"));},
            
            () -> {assertNull(ExcelReader.readExcelFile("egtsrfdsg"));});
    }
	
    @Test
    void getProjectStatsTest() {
        ArrayList<Line> lines = new ArrayList<>();
        
        lines.add(new Line(1, "GUI", "MainWindow", "MainWindow1()", metrics));
        lines.add(new Line(2, "GUI", "MainWindow", "checkValidRule()", metrics));
        lines.add(new Line(3, "GUI", "RuleGUI", "RuleGUI()", metrics));
        
        assertAll("getRuleName Test:",
                () -> {assertEquals(1, ExcelReader.getProjectStats(lines)[0]);}, // One package expected "GUI"
                () -> {assertEquals(2, ExcelReader.getProjectStats(lines)[1]);}, // Two classes expected "MainWindow" and "RuleGUI"
                () -> {assertEquals(3, ExcelReader.getProjectStats(lines)[2]);}, // Three methods expected "MainWindow()", "checkValidRule()" and "RuleGUI()"
                () -> {assertEquals(583*2, ExcelReader.getProjectStats(lines)[3]);}); // Number or lines of code of both classes is 583*2 
    }
    
	@Test
	void testCompareCodeSmells() {
		String comparisonFilePath = "unitTestAux.xlsx";
		ArrayList<Line> lines = new ArrayList<>();
		LinkedHashMap<String, String> metrics1 = new LinkedHashMap<>();
		LinkedHashMap<String, String> metrics2 = new LinkedHashMap<>();
		LinkedHashMap<String, String> metrics3 = new LinkedHashMap<>();
		LinkedHashMap<String, String> metrics4 = new LinkedHashMap<>();
		
		metrics1.put("NOM_class", "4");
		metrics1.put("LOC_class", "4");
		metrics1.put("WMC_class", "4");
		metrics1.put("LOC_method", "4");
		metrics1.put("CYCLO_method", "4");
		metrics1.put("is_God_Class", "TRUE");
		metrics1.put("is_Long_Method", "FALSE");
		metrics1.put("fake_Rule", "TRUE");
		lines.add(new Line(1, "pck", "cls", "mth(int)", metrics1));
		
		metrics2.put("NOM_class", "4");
		metrics2.put("LOC_class", "4");
		metrics2.put("WMC_class", "4");
		metrics2.put("LOC_method", "4");
		metrics2.put("CYCLO_method", "4");
		metrics2.put("is_God_Class", "FALSE");
		metrics2.put("is_Long_Method", "TRUE");
		metrics2.put("fake_Rule", "TRUE");
		lines.add(new Line(2, "pck", "cls", "mth(int,int)", metrics2));
		
		metrics3.put("NOM_class", "4");
		metrics3.put("LOC_class", "4");
		metrics3.put("WMC_class", "4");
		metrics3.put("LOC_method", "4");
		metrics3.put("CYCLO_method", "4");
		metrics3.put("is_God_Class", "FALSE");
		metrics3.put("is_Long_Method", "TRUE");
		metrics3.put("fake_Rule", "FALSE");
		lines.add(new Line(3, "pck", "cls", "mth(int,String)", metrics3));
		
		metrics4.put("NOM_class", "4");
		metrics4.put("LOC_class", "4");
		metrics4.put("WMC_class", "4");
		metrics4.put("LOC_method", "4");
		metrics4.put("CYCLO_method", "4");
		metrics4.put("is_God_Class", "FALSE");
		metrics4.put("is_Long_Method", "TRUE");
		metrics4.put("fake_Rule", "FALSE");
		lines.add(new Line(4, "pck", "cls", "mth(int,Integer)", metrics4));
		
		String[][] expectedResults = {{"1", "VP", "VN", "N/A"}, {"2", "FN", "FP", "N/A"}, {"3", "N/A", "N/A", "N/A"}, {"4", "N/A", "VP", "N/A"}};
		
		String[][] result = ExcelReader.compareCodeSmells(lines, comparisonFilePath);
		
		assertArrayEquals(ExcelReader.compareCodeSmells(lines, comparisonFilePath), expectedResults);
	}
	
}
