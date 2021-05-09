package unitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.Counter;

import metricas.CYCLO_method;
import metricas.LOC_class;
import metricas.LOC_method;
import metricas.Maestro;
import metricas.NOM_class;
import metricas.WMC_class;

class NOM_classTest {
	NOM_class nom_class;

	@Test
	void testExtractMetrics() {
		Maestro maestro = new Maestro(System.getProperty("user.dir") + "/unitTestFiles/metrics");
		maestro.openFolder(System.getProperty("user.dir") + "/unitTestFiles/metrics");
		nom_class = new NOM_class(maestro, "unitTest");
		maestro.addMetric(new LOC_class());
		maestro.addMetric(new LOC_method());
		CYCLO_method cyclo_method = new CYCLO_method(maestro, "unitTest");
		maestro.addMetric(cyclo_method);
		maestro.addMetric(new WMC_class());
		maestro.addMetric(nom_class);	
		cyclo_method.extractMetrics();
		nom_class.extractMetrics();
		NOM_class nom_class_expected = new NOM_class();
		Counter counter1 = nom_class_expected.counter("metricsUnitTests/LOC_class/");
		counter1.inc(5);
		
		Assertions.assertArrayEquals(nom_class_expected.getCounters().keySet().toArray(), nom_class.getCounters().keySet().toArray());
		Assertions.assertTrue(helpers.HelperMethods.compareMapCounters(nom_class_expected.getCounters(), nom_class.getCounters()));
		
		
	}
}
