package unitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.Counter;

import metricas.CYCLO_method;
import metricas.LOC_class;
import metricas.LOC_method;
import metricas.MetricHandler;
import metricas.WMC_class;

class WMC_classTest {
	WMC_class wmc_class;

	@Test
	void testExtractMetrics() {
		MetricHandler metricHandler = new MetricHandler(System.getProperty("user.dir") + "/unitTestFiles/metrics");
		metricHandler.openFolder(System.getProperty("user.dir") + "/unitTestFiles/metrics");
		metricHandler.addMetric(new LOC_class());
		metricHandler.addMetric(new LOC_method());
		CYCLO_method cyclo_method = new CYCLO_method(metricHandler, "unitTest");
		metricHandler.addMetric(cyclo_method);
		wmc_class = new WMC_class(metricHandler, "unitTest");
		metricHandler.addMetric(wmc_class);
		cyclo_method.extractMetrics();
		wmc_class.extractMetrics();
		WMC_class wmc_class_expected = new WMC_class();
		Counter counter1 = wmc_class_expected.counter("metricsUnitTests/LOC_class/");
		counter1.inc(4);
		
		Assertions.assertArrayEquals(wmc_class_expected.getCounters().keySet().toArray(), wmc_class.getCounters().keySet().toArray());
		Assertions.assertTrue(helpers.HelperMethods.compareMapCounters(wmc_class_expected.getCounters(), wmc_class.getCounters()));
		
		
	}
}
