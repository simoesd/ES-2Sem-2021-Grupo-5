package unitTests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import reader.Line;
import rules.Condition;
import rules.Rule;

class RuleTest {

    private Rule rule;
    private Line line;
    
    @BeforeEach
    void setUp()
    {
        String ruleName = "is_God_Class";
        
        LinkedList<Condition> conditions = new LinkedList<>();
        conditions.add(new Condition("LOC_METHOD", Condition.GREATER_THAN, 10));
        conditions.add(new Condition("CYCLO_METHOD", Condition.GREATER_THAN_EQUAL, 15));
        
        LinkedList<Integer> logicOperators = new LinkedList<>();
        logicOperators.add(Rule.AND);
        
        boolean isClassRule = true;
        
        rule = new Rule(ruleName, conditions, logicOperators, isClassRule);
        
        LinkedHashMap<String, String> metrics = new LinkedHashMap<>();
        metrics.put("LOC_METHOD", "15");
        metrics.put("CYCLO_METHOD", "15");
        line = new Line(0, "package", "class", "method", metrics);
        
    }
    
    @Test
    void testRuleNoError() {
        String ruleName = "is_God_Class";
        
        LinkedList<Condition> conditions = new LinkedList<>();
        conditions.add(new Condition("LOC_METHOD", Condition.GREATER_THAN, 10));
        conditions.add(new Condition("CYCLO_METHOD", Condition.GREATER_THAN_EQUAL, 15));
        
        LinkedList<Integer> logicOperators = new LinkedList<>();
        logicOperators.add(Rule.AND);
        
        boolean isClassRule = true;
        
        Rule rule = new Rule(ruleName, conditions, logicOperators, isClassRule);
        
        assertAll("Rule atributes",
                () -> {assertEquals(ruleName, rule.getRuleName());},
                () -> {assertEquals(conditions, rule.getConditions());},
                () -> {assertEquals(logicOperators, rule.getLogicOperators());},
                () -> {assertEquals(isClassRule, rule.isClassRule());});
    }
    
    @Test
    void testRuleError() {
        assertThrows(IllegalArgumentException.class, () -> {new Rule("", new LinkedList<>(), new LinkedList<>(), false);});
    }

    @Test
    void testEvaluateRule() {
        assertEquals(true, rule.evaluateRule(line));
    }
    
    public static Stream<Arguments> testCompareInfoNoError() {
        return Stream.of(
        //testes sem erros
            Arguments.of(false, Rule.AND, false, false),
            Arguments.of(false, Rule.AND, true, false),
            Arguments.of(true, Rule.AND, false, false),
            Arguments.of(true, Rule.AND, true, true),
            Arguments.of(false, Rule.OR, false, false),
            Arguments.of(false, Rule.OR, true, true),
            Arguments.of(true, Rule.OR, false, true),
            Arguments.of(true, Rule.OR, true, true)
        );
    }

    @ParameterizedTest
    @MethodSource("testCompareInfoNoError")
    void testCompareConditionsNoError(boolean firstConditionValue, int logicOperator, boolean secondConditionValue, boolean expectedResult) {
        assertEquals(expectedResult, rule.compareConditions(firstConditionValue, logicOperator, secondConditionValue));
    }

    @Test
    void testCompareConditionsError() {
        assertThrows(IllegalArgumentException.class, () -> {rule.compareConditions(false, 2, false);});
    }
}
