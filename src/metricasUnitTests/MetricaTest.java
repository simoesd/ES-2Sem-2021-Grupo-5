package metricasUnitTests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Stack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metricas.LOC_class;

class MetricaTest {
	
	LOC_class locClass;

	@BeforeEach
	void setUp() throws Exception {
		locClass = new LOC_class();
		
	}

//	@Test
//	void testFilterCode() {
//		File file = new File(System.getProperty("user.dir") + "//src//metricas//Metrica.java");
//	}

	
	@Test
	void testGetMethodName() {
		
		//Stack creation
		Stack<String> stack = new Stack<String> (); 
		stack.push("   ");
		stack.push("private int test;");
		stack.push("private String testString;");
		
		Stack<String> stack1 = new Stack<String> (); 
		stack1.push("   ");
		stack1.push("private int test;");
		
		Stack<String> stack2 = new Stack<String> (); 
		stack2.push("abstract void extractMetrics();");
		stack2.push("public String getMethodName(Metrica.LOC_class locClassTest, int i){"); 
	
		//Action	
		Assertions.assertAll("Nome Método",
				 () -> {assertEquals("", locClass.getMethodName(stack));},
				 () -> {assertEquals("",locClass.getMethodName(stack1));},
				 () -> {assertEquals("getMethodName(LOC_class,int)",locClass.getMethodName(stack2));} 
				);
		
	}

}
