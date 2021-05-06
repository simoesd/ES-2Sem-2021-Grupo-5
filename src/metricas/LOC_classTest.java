package metricas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.Counter;

class LOC_classTest {//TODO atualizar listas
	
	LOC_class loc_class;

	@BeforeEach
	void setUp() throws Exception {
		Maestro maestro = new Maestro(System.getProperty("user.dir"));
		maestro.startMetricCounters();
		loc_class=maestro.getLOC_class();
	}

	@Test
	void test() {
		MetricRegistryToTest mRegistry = new MetricRegistryToTest();

		Counter counter = new Counter();
		counter.inc(66);

		mRegistry.addCounter("GUI/ConditionGUI/", counter);

		Counter counter2 = new Counter();
		counter2.inc(583);

		mRegistry.addCounter("GUI/MainWindow/", counter2);

		Counter counter3 = new Counter();
		counter3.inc(161);

		mRegistry.addCounter("GUI/RuleGUI/", counter3);
		

		Assertions.assertFalse(loc_class.getCounters().isEmpty());

//		Assertions.assertEquals(mRegistry.getCounters().firstKey(),nom_class.getCounters().firstKey());

		for (int i = 0; i < mRegistry.getCounters().values().toArray().length; i++) {

			Long expectedCounter = ((Counter) mRegistry.getCounters().values().toArray()[i]).getCount();
			Long actualCounter = ((Counter) loc_class.getCounters().values().toArray()[i]).getCount();

			Assertions.assertEquals(expectedCounter, actualCounter);

			String expectedString = mRegistry.getCounters().keySet().toArray()[i].toString();
			String actualString = loc_class.getCounters().keySet().toArray()[i].toString();

			Assertions.assertEquals(expectedString, actualString);

		}
	}

}
