package unitTests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reader.ExcelReader;
import reader.Line;

class LineTest {
	
	private LinkedHashMap<String, String> metrics;
	
	@BeforeEach
	void setUp() {
		metrics = new LinkedHashMap<>();
		metrics.put("LOC_CLASS", "583");
		metrics.put("LOC_METHOD", "3");
		metrics.put("CYCLO_METHOD", "0");
		metrics.put("WMC_CLASS", "38");
		metrics.put("NOM_CLASS", "13");
		metrics.put("is_god_class", "true");
		metrics.put("is_long_method", "false");
	}
	
	@Test
	void getRuleNameTest() {
		Line line = new Line(1, "GUI", "MainWindow", "MainWindow()", metrics);
		
		assertAll("getRuleName Test:",
				() -> {assertEquals("is_god_class", line.getRuleNames()[0]);},
				() -> {assertEquals("is_long_method", line.getRuleNames()[1]);});
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
}
