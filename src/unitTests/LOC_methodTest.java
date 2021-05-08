package unitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.Counter;

import metricas.LOC_method;

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

}
