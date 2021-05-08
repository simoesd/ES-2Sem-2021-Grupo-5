package metricasUnitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.Counter;

import metricas.CYCLO_method;

class CYCLO_methodTest {
	
	CYCLO_method cyclo_method;
	
	@BeforeEach
	void setUp() throws Exception {
		cyclo_method = new CYCLO_method();
		cyclo_method.counter = new Counter();
		cyclo_method.counter("DiretorioTeste");
	}

	@Test
	void testApplyMetricFilter() {
		cyclo_method.applyMetricFilter("for (int i = 0; i != charLine.length; i++) {\r\n"
				+ "if (!isMultiLineComment && charLine[i] == '{') {\r\n"
				+ "handleOpenBracket();\r\n"
				+ "} else if (!isMultiLineComment && charLine[i] == '}') {\r\n"
				+ "handleCloseBracket();\r\n"
				+ "} else {\r\n"
				+ "if (incr > 0 && !isNonMethodBlock) // Linha dentro de um método\r\n"
				+ "addLine = true;\r\n"
				+ " if (incr == 0) { // Linha fora de método\r\n"
				+ "betweenMethodsBuffer.push(line);\r\n"
				+ "}\r\n"
				+ "}\r\n"
				+ "}");
		Assertions.assertEquals((long)5, cyclo_method.counter.getCount());
		
	}

}
