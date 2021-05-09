package unitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.Counter;

import metricas.CYCLO_method;
import metricas.LOC_class;
import metricas.LOC_method;
import metricas.MetricHandler;
import metricas.NOM_class;
import metricas.WMC_class;

class NOM_classTest {
	NOM_class nom_class;

	@Test
	void testExtractMetrics() {
		MetricHandler metricHandler = new MetricHandler(System.getProperty("user.dir") + "/unitTestFiles/metrics");
		metricHandler.openFolder(System.getProperty("user.dir") + "/unitTestFiles/metrics");
		nom_class = new NOM_class(metricHandler, "unitTest");
		metricHandler.addMetric(new LOC_class());
		metricHandler.addMetric(new LOC_method());
		CYCLO_method cyclo_method = new CYCLO_method(metricHandler, "unitTest");
		metricHandler.addMetric(cyclo_method);
		metricHandler.addMetric(new WMC_class());
		metricHandler.addMetric(nom_class);	
		cyclo_method.extractMetrics();
		nom_class.extractMetrics();
		NOM_class nom_class_expected = new NOM_class();
		Counter counter1 = nom_class_expected.counter("metricsUnitTests/LOC_class/");
		counter1.inc(5);
		
		Assertions.assertArrayEquals(nom_class_expected.getCounters().keySet().toArray(), nom_class.getCounters().keySet().toArray());
		Assertions.assertTrue(helpers.HelperMethods.compareMapCounters(nom_class_expected.getCounters(), nom_class.getCounters()));
		
		
	}
}
