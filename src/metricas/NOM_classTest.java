/**
 * 
 */
package metricas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.Counter;

class NOM_classTest { //TODO atualizar listas

	private NOM_class nom_class;

	@BeforeEach
	void setUp() throws Exception {
		Maestro maestro = new Maestro(System.getProperty("user.dir"));
		maestro.startMetricCounters();
		nom_class = maestro.getNOM_class();
	}

	@Test
	void test() {
		MetricRegistryToTest mRegistry = new MetricRegistryToTest();

//		Counter counter = new Counter();
//		counter.inc(8);
//
//		mRegistry.addCounter("GUI/ConditionGUI/", counter);
//
//		Counter counter2 = new Counter();
//		counter2.inc(13);
//
//		mRegistry.addCounter("GUI/MainWindow/", counter2);
//
//		Counter counter3 = new Counter();
//		counter3.inc(2);
//
//		mRegistry.addCounter("GUI/RuleGUI/", counter3);

		String[] counterNames = { "GUI/ConditionGUI/", "GUI/MainWindow/", "GUI/RuleGUI/", "GUI/WrapLayout/",
				"metricas/CYCLO_method/", "metricas/LOC_class/", "metricas/LOC_classTest/", "metricas/LOC_method/",
				"metricas/Maestro/", "metricas/MetricRegistryToTest/", "metricas/Metrica/", "metricas/NOM_class/",
				"metricas/NOM_classTest/", "metricas/WMC_class/", "metricas/WMC_classTest/", "reader/ExcelReader/",
				"reader/Line/", "rules/Condition/", "rules/Rule/" };

		int[] counterValues = { 8, 13, 2, 7, 3, 4, 2, 3, 18, 7, 11, 3, 2, 3, 2, 1, 6, 1, 1 };
		
		mRegistry.addCounterList(counterNames, counterValues);
		Assertions.assertFalse(nom_class.getCounters().isEmpty());

		for (int i = 0; i < mRegistry.getCounters().values().toArray().length; i++) {
			
			String expectedString = mRegistry.getCounters().keySet().toArray()[i].toString();
			String actualString = nom_class.getCounters().keySet().toArray()[i].toString();

			Assertions.assertEquals(expectedString, actualString);

			Long expectedCounter = ((Counter) mRegistry.getCounters().values().toArray()[i]).getCount();
			Long actualCounter = ((Counter) nom_class.getCounters().values().toArray()[i]).getCount();

			Assertions.assertEquals(expectedCounter, actualCounter);



		}

	}

}
