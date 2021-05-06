package unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.LinkedHashMap;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized.Parameters;

import reader.Line;
import rules.Condition;

class ConditionTest {

    
    @Parameters
    public static Stream<Arguments> testInfoNoError() {
        return Stream.of(
        //testes sem erros
            Arguments.of("LOC_METHOD", Condition.GREATER_THAN, 15, new String[] {"LOC_METHOD", "15"}, false),
            Arguments.of("LOC_METHOD", Condition.GREATER_THAN, 15, new String[] {"LOC_METHOD", "16"}, true),
            Arguments.of("LOC_METHOD", Condition.GREATER_THAN_EQUAL, 15, new String[] {"LOC_METHOD", "14"}, false),
            Arguments.of("LOC_METHOD", Condition.GREATER_THAN_EQUAL, 15, new String[] {"LOC_METHOD", "15"}, true),
            Arguments.of("LOC_METHOD", Condition.LESS_THAN, 15, new String[] {"LOC_METHOD", "15"}, false),
            Arguments.of("LOC_METHOD", Condition.LESS_THAN, 15, new String[] {"LOC_METHOD", "14"}, true),
            Arguments.of("LOC_METHOD", Condition.LESS_THAN_EQUAL, 15, new String[] {"LOC_METHOD", "15"}, true),
            Arguments.of("LOC_METHOD", Condition.LESS_THAN_EQUAL, 15, new String[] {"LOC_METHOD", "16"}, false)
        );
    }
    
    @ParameterizedTest
    @MethodSource("testInfoNoError")
    void testEvaluateConditionError(String conditionMetric, int conditionOperator, int conditionValue, String[] lineInfo, boolean expectedResult) {
        Condition condition = new Condition(conditionMetric, conditionOperator, conditionValue);
        LinkedHashMap<String, String> metrics = new LinkedHashMap<>();
        metrics.put(lineInfo[0], lineInfo[1]);
        Line line = new Line(0, "package", "class", "method", metrics);
        assertEquals(expectedResult, condition.evaluateCondition(line));
    }
    
    
    
    
    @Parameters
    public static Stream<Arguments> testInfoError() {
        return Stream.of(
        //testes com erros
            //nome da métrica da condição errado
            Arguments.of("abc", Condition.GREATER_THAN_EQUAL, 15, new String[] {"LOC_METHOD", "15"}, NumberFormatException.class),
            //operador inválido
            Arguments.of("LOC_METHOD", 4, 15, new String[] {"LOC_METHOD", "15"}, IllegalArgumentException.class),
            //valor da métrica da linha inválida
            Arguments.of("LOC_METHOD", Condition.GREATER_THAN_EQUAL, 15, new String[] {"LOC_METHOD", "not int"}, NumberFormatException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("testInfoError")
    void testEvaluateConditionError(String conditionMetric, int conditionOperator, int conditionValue, String[] lineInfo, Class<Exception> expectedResult) {
        Condition condition = new Condition(conditionMetric, conditionOperator, conditionValue);
        LinkedHashMap<String, String> metrics = new LinkedHashMap<>();
        metrics.put(lineInfo[0], lineInfo[1]);
        Line line = new Line(0, "package", "class", "method", metrics);
        assertThrows(expectedResult, () -> {condition.evaluateCondition(line);});
    }

}
