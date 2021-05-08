package unitTests;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.Counter;

import metricas.CYCLO_method;
import metricas.LOC_class;
import metricas.LOC_method;
import metricas.Maestro;

class LOC_classTest {
	LOC_class loc_class;

	@BeforeEach
	void setUp() throws Exception {
		loc_class = new LOC_class();
		loc_class.counter = new Counter();
		loc_class.counter("DiretorioTeste");

	}

	@Test
	void testApplyMetricFilter() {
		
		loc_class.applyMetricFilter("");
		loc_class.applyMetricFilter("package");
		loc_class.applyMetricFilter("import");
		Assertions.assertEquals((long) 0, loc_class.counter.getCount());
	}

	@Test
	void testFilterCode() {
		loc_class.filterCode(new File(System.getProperty("user.dir") + "//metricsUnitTestsUtilityFolder//src//metricsUnitTests//LOC_class.java"));

		Assertions.assertEquals((long) 41, loc_class.counter.getCount());
	}
	
	@Test
	void testExtractMetrics() {
		Maestro maestro = new Maestro(System.getProperty("user.dir") + "/metricsUnitTestsUtilityFolder");
		maestro.openFolder(System.getProperty("user.dir") + "/metricsUnitTestsUtilityFolder");
		loc_class = new LOC_class(maestro, "unitTest");
		maestro.addMetric(loc_class);
		loc_class.extractMetrics();
		LOC_class loc_class_expected = new LOC_class();
		Counter counter1 = loc_class_expected.counter("metricsUnitTests/LOC_class/");
		counter1.inc(41);
		
		Assertions.assertArrayEquals(loc_class_expected.getCounters().keySet().toArray(), loc_class.getCounters().keySet().toArray());
		Assertions.assertTrue(helpers.HelperMethods.compareSortedMapCounters(loc_class_expected.getCounters(), loc_class.getCounters()));
		
		
	}
}
