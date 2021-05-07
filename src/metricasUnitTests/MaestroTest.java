package metricasUnitTests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import metricas.Maestro;

class MaestroTest {

	
	@Test
	void resultTest() {
		
	}
	
	@Test
	void exportResultsTest() {
		
	}
	
	@Test
	void createHeaderExcelTest() {
		
	}
	
	@Test
	void writeExcelTest() {
		
	}
	
	@Test
	void openFolderTest() {
		
	}
		
	@Test
	void testCutAbsolutePath() {
		Maestro m = new Maestro("E:\\E_Cenas\\Git\\ES-2Sem-2021-Grupo-5"); //Change path to your project
		String path1 = "E:\\E_Cenas\\Git\\ES-2Sem-2021-Grupo-5\\src\\teste\\com\\cenas\\metricas";
		String path2 = "E:\\E_Cenas\\Git\\ES-2Sem-2021-Grupo-5\\src\\metricas";
		assertAll("Maestro tests",
				() -> {assertEquals("teste.com.cenas/metricas", m.cutAbsolutePath(path1));},
				() -> {assertEquals("defaultPackage/metricas", m.cutAbsolutePath(path2));});
	}
	

}
