package unitTests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.Counter;

import helpers.HelperMethods;
import metricas.CYCLO_method;

class HelperMethodsTest {

    @Test
    void testCustomParseBoolean() {
        assertAll("Custom Parse Boolean",
                () -> {assertEquals(true, HelperMethods.customParseBoolean("true"));},
                () -> {assertEquals(true, HelperMethods.customParseBoolean("true"));},
                () -> {assertEquals(false, HelperMethods.customParseBoolean("false"));},
                () -> {assertEquals(false, HelperMethods.customParseBoolean("False"));},
                () -> {assertThrows(IllegalArgumentException.class, () -> HelperMethods.customParseBoolean("not a boolean"));}
                );
    }

    @Test
    void testGetCaseInsensitive() {
        HashMap<String, Integer> map = new HashMap<>();
        String key1 = "sameCaseKey";
        int val1 = 0;
        String key2 = "differentCaseKey";
        int val2 = 1;
        String key3 = "notPartOfTheMap";
        
        map.put(key1, val1);
        map.put(key2, val2);
        assertAll("Get Case Insensitive Test",
                () -> {assertEquals(val1, HelperMethods.getCaseInsensitive(map, key1));},
                () -> {assertEquals(val2, HelperMethods.getCaseInsensitive(map, key2));},
                () -> {assertEquals(null, HelperMethods.getCaseInsensitive(map, key3));}
        );
    }

    @Test
    void testContainsKeyCaseInsensitive() {
        HashMap<String, Integer> map = new HashMap<>();
        String key1 = "sameCaseKey";
        int val1 = 0;
        String key2 = "differentCaseKey";
        int val2 = 1;
        String key3 = "notPartOfTheMap";
        
        map.put(key1, val1);
        map.put(key2, val2);
        assertAll("Get Case Insensitive Test",
                () -> {assertTrue(HelperMethods.containsKeyCaseInsensitive(map, key1));},
                () -> {assertTrue(HelperMethods.containsKeyCaseInsensitive(map, key2));},
                () -> {assertFalse(HelperMethods.containsKeyCaseInsensitive(map, key3));}
        );
    }
@Test
	
	void testCompareSortedMap(){
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
		
		CYCLO_method cyclo_method = new CYCLO_method();
		Counter counter6 = cyclo_method.counter("metricsUnitTests/LOC_class/LOC_class()");
		counter6.inc(0);
		Counter counter7 = cyclo_method.counter("metricsUnitTests/LOC_class/LOC_class(MetricHandler)");
		counter7.inc(0);
		Counter counter8 = cyclo_method.counter("metricsUnitTests/LOC_class/extractMetrics()");
		counter8.inc(1);
		Counter counter9 = cyclo_method.counter("metricsUnitTests/LOC_class/applyMetricFilter(String)");
		counter9.inc(1);
		Counter counter10 = cyclo_method.counter("metricsUnitTests/LOC_class/filterCode(File)");
		counter10.inc(2);
		
		Assertions.assertTrue(helpers.HelperMethods.compareMapCounters(cyclo_method_expected.getCounters(), cyclo_method.getCounters()));
		counter10.inc(90);
		Assertions.assertFalse(helpers.HelperMethods.compareMapCounters(cyclo_method_expected.getCounters(), cyclo_method.getCounters()));
	}

}
