package metricas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.Counter;

class WMC_classTest {//TODO atualizar listas
	WMC_class wmc_class;

	@BeforeEach
	void setUp() throws Exception {
		Maestro maestro = new Maestro(System.getProperty("user.dir"));
		maestro.startMetricCounters();
		maestro.getCYCLO_method().getThread().join();
		wmc_class=maestro.getWMC_class();
	}

	@Test
	void test() {
		MetricRegistryToTest mRegistry = new MetricRegistryToTest();

		Counter counter = new Counter();
		counter.inc(0);

		mRegistry.addCounter("GUI/ConditionGUI/", counter);

		Counter counter2 = new Counter();
		counter2.inc(38);

		mRegistry.addCounter("GUI/MainWindow/", counter2);

		Counter counter3 = new Counter();
		counter3.inc(1);

		mRegistry.addCounter("GUI/RuleGUI/", counter3);
		

		Assertions.assertFalse(wmc_class.getCounters().isEmpty());
		
		for (int i = 0; i < mRegistry.getCounters().values().toArray().length; i++) {

			Long expectedCounter = ((Counter) mRegistry.getCounters().values().toArray()[i]).getCount();
			Long actualCounter = ((Counter) wmc_class.getCounters().values().toArray()[i]).getCount();

			Assertions.assertEquals(expectedCounter, actualCounter);

			String expectedString = mRegistry.getCounters().keySet().toArray()[i].toString();
			String actualString = wmc_class.getCounters().keySet().toArray()[i].toString();

			Assertions.assertEquals(expectedString, actualString);

		}
	}

}
