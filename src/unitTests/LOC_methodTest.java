package unitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.Counter;

import metricas.LOC_class;
import metricas.LOC_method;
import metricas.MetricHandler;

class LOC_methodTest {

	LOC_method loc_method;
	
	@BeforeEach
	void setUp() throws Exception {
		loc_method = new LOC_method();
		loc_method.counter = new Counter();
		loc_method.counter("DiretorioTeste");	
	}

	@Test
	void testApplyMetricFilter() {
		loc_method.applyMetricFilter(""
				+ "{\r\n"
				+ "this.metric = metric;\r\n"
				+ "this.thresholdOperator = thresholdOperator;\r\n"
				+ "this.thresholdValue = thresholdValue;\r\n"
				+ "initializePanel();\r\n"
				+ "}\r\n"
				+ "");
		
		Assertions.assertEquals((long)6, loc_method.counter.getCount());
	}
	
	@Test
	void testExtractMetrics() {
		MetricHandler metricHandler = new MetricHandler(System.getProperty("user.dir") + "/unitTestFiles/metrics");
		metricHandler.openFolder(System.getProperty("user.dir") + "/unitTestFiles/metrics");
		metricHandler.addMetric(new LOC_class());
		loc_method = new LOC_method(metricHandler, "unitTest");
		metricHandler.addMetric(loc_method);
		loc_method.extractMetrics();
		LOC_method loc_method_expected = new LOC_method();
		Counter counter1 = loc_method_expected.counter("metricsUnitTests/LOC_class/LOC_class()");
		counter1.inc(4);
		Counter counter2 = loc_method_expected.counter("metricsUnitTests/LOC_class/LOC_class(MetricHandler)");
		counter2.inc(5);
		Counter counter3 = loc_method_expected.counter("metricsUnitTests/LOC_class/extractMetrics()");
		counter3.inc(9);
		Counter counter4 = loc_method_expected.counter("metricsUnitTests/LOC_class/applyMetricFilter(String)");
		counter4.inc(5);
		Counter counter5 = loc_method_expected.counter("metricsUnitTests/LOC_class/filterCode(File)");
		counter5.inc(13);
		
		Assertions.assertArrayEquals(loc_method_expected.getCounters().keySet().toArray(), loc_method.getCounters().keySet().toArray());
		Assertions.assertTrue(helpers.HelperMethods.compareMapCounters(loc_method_expected.getCounters(), loc_method.getCounters()));
	
		
	}
	

}
