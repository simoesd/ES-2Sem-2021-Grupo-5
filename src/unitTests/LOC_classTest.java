package unitTests;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.Counter;

import metricas.LOC_class;

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
		loc_class.filterCode(new File(System.getProperty("user.dir") + "//src//metricas//LOC_class.java"));

		Assertions.assertEquals((long) 41, loc_class.counter.getCount());
	}
	
}
