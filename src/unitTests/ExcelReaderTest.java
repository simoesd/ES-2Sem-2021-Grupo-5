package unitTests;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

import reader.ExcelReader;
import reader.Line;

class ExcelReaderTest {

//	, "NOM_class", "LOC_class", "WMC_class", , "LOC_method", "CYCLO_method", "is_Long_Method"
	
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
		
		for(int i = 0; i < result.length; i++) {
			for(int j = 0; j < result[0].length; j++) {
				System.out.println(result[i][j]);
			}
		}
		
		assertArrayEquals(ExcelReader.compareCodeSmells(lines, comparisonFilePath), expectedResults);
	}
	
}
