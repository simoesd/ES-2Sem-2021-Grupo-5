package unitTests;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.Counter;

import metricas.CYCLO_method;
import metricas.LOC_class;
import metricas.LOC_method;
import metricas.MetricHandler;

class CYCLO_methodTest {

	CYCLO_method cyclo_method;

	@BeforeEach
	void setUp() throws Exception {

//		metricHandler.setProjectDirectory("//metricsUnitTestsUtilityFolder//");
		cyclo_method = new CYCLO_method();
		cyclo_method.counter = new Counter();
		cyclo_method.counter("DiretorioTeste");
	}

	@Test
	void testApplyMetricFilter() {
		cyclo_method.applyMetricFilter("for (int i = 0; i != charLine.length; i++) {\r\n"
				+ "if (!isMultiLineComment && charLine[i] == '{') {\r\n" + "handleOpenBracket();\r\n"
				+ "} else if (!isMultiLineComment && charLine[i] == '}') {\r\n" + "handleCloseBracket();\r\n"
				+ "} else {\r\n" + "if (incr > 0 && !isNonMethodBlock) // Linha dentro de um método\r\n"
				+ "addLine = true;\r\n" + " if (incr == 0) { // Linha fora de método\r\n"
				+ "betweenMethodsBuffer.push(line);\r\n" + "}\r\n" + "}\r\n" + "}");
		Assertions.assertEquals((long) 5, cyclo_method.counter.getCount());

	}

//	@Test
//	void testExtractMetrics(){
//		
//		cyclo_method = new CYCLO_method(metricHandler);
//		System.out.println(metricHandler.getFilesInDirectory());

//		metricHandler.addMetric(cyclo_method);
//		metricHandler.start();
//		System.out.println(cyclo_method.getCounters());
//		Assertions.assertEquals(cyclo_method_expected.getCounters(), cyclo_method.getCounters());
//	}

	@Test
	void testExtractMetrics() {
		MetricHandler metricHandler = new MetricHandler(System.getProperty("user.dir") + "/unitTestFiles/metrics");
		metricHandler.openFolder(System.getProperty("user.dir") + "/unitTestFiles/metrics");
		metricHandler.addMetric(new LOC_class());
		metricHandler.addMetric(new LOC_method());
		cyclo_method = new CYCLO_method(metricHandler, "unitTest");
		metricHandler.addMetric(cyclo_method);
		cyclo_method.extractMetrics();
		CYCLO_method cyclo_method_expected = new CYCLO_method();
		Counter counter1 = cyclo_method_expected.counter("metricsUnitTests/LOC_class/LOC_class()");
		counter1.inc(0);
		Counter counter2 = cyclo_method_expected.counter("metricsUnitTests/LOC_class/LOC_class(MetricHandler)");
		counter2.inc(0);
		Counter counter3 = cyclo_method_expected.counter("metricsUnitTests/LOC_class/extractMetrics()");
		counter3.inc(1);
		Counter counter4 = cyclo_method_expected.counter("metricsUnitTests/LOC_class/applyMetricFilter(String)");
		counter4.inc(1);
		Counter counter5 = cyclo_method_expected.counter("metricsUnitTests/LOC_class/filterCode(File)");
		counter5.inc(2);
		
		Assertions.assertArrayEquals(cyclo_method_expected.getCounters().keySet().toArray(), cyclo_method.getCounters().keySet().toArray());
		Assertions.assertTrue(helpers.HelperMethods.compareMapCounters(cyclo_method_expected.getCounters(), cyclo_method.getCounters()));
		
		
	}
	

	
	

}
