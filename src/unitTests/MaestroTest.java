package unitTests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metricas.CYCLO_method;
import metricas.LOC_class;
import metricas.LOC_method;
import metricas.Maestro;
import metricas.Metrica;
import metricas.NOM_class;
import metricas.WMC_class;
import rules.Condition;
import rules.Rule;

class MaestroTest {

	String projectDir;
	Maestro maestro;
	String dir;
	
	@BeforeEach
	void setup() {
		projectDir = System.getProperty("user.dir");
		maestro = new Maestro(projectDir);
		dir = projectDir + "\\unitTestsUtilityFolder";
		ArrayList<Metrica> metrics = new ArrayList<>();
		metrics.add(new LOC_class());
		metrics.add(new LOC_method());
		metrics.add(new CYCLO_method());
		metrics.add(new WMC_class());
		metrics.add(new NOM_class());
		
		for(Metrica m: metrics) {
			if(m.isClassMetric)
				m.setCounter(m.counter("teste/file/"));
			else
				m.setCounter(m.counter("teste/file/metodo/"));
			
			for(int i = 0; i < (int)(Math.random()*10); i++) {
				m.getCounter().inc();
			}
		}
		
		metrics.forEach(m -> maestro.addMetric(m));
		
		String ruleName = "is_God_Class";

        LinkedList<Condition> conditions = new LinkedList<>();
        conditions.add(new Condition("LOC_METHOD", Condition.GREATER_THAN, 10));
        conditions.add(new Condition("CYCLO_METHOD", Condition.GREATER_THAN_EQUAL, 15));

        LinkedList<Integer> logicOperators = new LinkedList<>();
        logicOperators.add(Rule.AND);

        boolean isClassRule = true;

        Rule rule = new Rule(ruleName, conditions, logicOperators, isClassRule);
        
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule("cenas", conditions, logicOperators, isClassRule));
        rules.add(new Rule("coisas", conditions, logicOperators, isClassRule));
		
		maestro.addRule(rule);
		maestro.addRules(rules);
	}
	
	
	@Test
	void resultTest() {
		
	}
	
	@Test
	void exportResultsTest() {
		
	}
	
	@Test
	void createExcelFileTest() {
		assertAll("createExcelFileTest",
				() -> {assertEquals(projectDir + "\\ES-2Sem-2021-Grupo-5_metricas.xlsx", maestro.createExcelFile());},
				() -> {maestro.setProjectDirectory("non-existent-directory"); assertEquals("", maestro.createExcelFile());});
	}
	
	@Test
	void createHeaderExcelTest() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		
		try {
			maestro.createHeaderExcel(sheet);
			Iterator<Row> rowIterator = sheet.iterator();
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.iterator();
			workbook.close();
			
			assertAll("createHeaderExcelTest",
					() -> {assertEquals("MethodID", cellIterator.next().getStringCellValue());},
					() -> {assertEquals("Package", cellIterator.next().getStringCellValue());},
					() -> {assertEquals("Class", cellIterator.next().getStringCellValue());},
					() -> {assertEquals("Method", cellIterator.next().getStringCellValue());});
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	void writeExcelTest() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		
		String[] line = {"GUI", "TRUE", "4"};
		
		try {
			maestro.writeExcel(sheet, line);
			Iterator<Row> rowIterator = sheet.iterator();
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.iterator();
			workbook.close();
			
			assertAll("writeExcelTest",
					() -> {assertEquals("GUI", cellIterator.next().getStringCellValue());},
					() -> {assertTrue(cellIterator.next().getBooleanCellValue());},
					() -> {assertEquals(4, cellIterator.next().getNumericCellValue());});
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	void openFolderTest() {
		maestro.openFolder(dir);
		assertEquals(dir + "\\file.java", maestro.getFilesInDirectory().get(0).getAbsolutePath());
	}
	
	
	@Test
	void testCutAbsolutePath() {
		String path1 = projectDir + "\\src\\teste\\com\\cenas\\metricas"; // these paths don't actually exist but that's ok
		String path2 = projectDir + "\\src\\metricas";
		assertAll("Maestro tests",
				() -> {assertEquals("teste.com.cenas/metricas", maestro.cutAbsolutePath(path1));},
				() -> {assertEquals("defaultPackage/metricas", maestro.cutAbsolutePath(path2));});
	}
	

}
