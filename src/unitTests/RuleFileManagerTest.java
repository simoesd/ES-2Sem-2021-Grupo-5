package unitTests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized.Parameters;

import rules.Condition;
import rules.Rule;
import rules.RuleFileManager;

class RuleFileManagerTest {
    
    @BeforeEach
    void setUp()
    {
        
    }

    @Test
    void testWriteEntry() {
        fail("Not yet implemented");
    }
    
    @Parameters
    public static Stream<Arguments> testReadRulesInfo() {
        HashMap<String, List<Rule>> readTestNoError = new HashMap<>();
        
        String timestamp = "2021/05/06 19:47:59";
        
        String ruleName = "is_God_Class";
        
        LinkedList<Condition> conditions = new LinkedList<>();
        conditions.add(new Condition("LOC_METHOD", Condition.GREATER_THAN, 10));
        conditions.add(new Condition("CYCLO_METHOD", Condition.GREATER_THAN_EQUAL, 15));
        
        LinkedList<Integer> logicOperators = new LinkedList<>();
        logicOperators.add(Rule.AND);
        
        boolean isClassRule = true;
        
        Rule rule = new Rule(ruleName, conditions, logicOperators, isClassRule);
        
        List<Rule> rules = new LinkedList<>();
        rules.add(rule);
        
        readTestNoError.put(timestamp, rules);
        
//        RuleFileManager.writeEntry(rules, "readTestNoError.rul");
        
        return Stream.of(
            //file with no errors
            Arguments.of("readTestNoError.rul", readTestNoError),
            //empty file
            Arguments.of("readTestEmpty.rul", new HashMap<String, List<Rule>>())
        );
    }
    

    @ParameterizedTest
    @MethodSource("testReadRulesInfo")
    void testReadRulesNoException(String filePath, HashMap<String, List<Rule>> expectedResult) {
        HashMap<String, List<Rule>> receivedResult = RuleFileManager.readRules(filePath);
        assertAll("Read Rules Result:",
                //checks if expected and received maps have the same size
                () -> {assertEquals(expectedResult.size(), receivedResult.size());},
                //checks if each key in the expected map exists in the received map, and the value it corresponds to is the same
                () -> {expectedResult.forEach((x,y) -> receivedResult.get(x).equals(y));}
                );
    }
    

    @Test
    void testClearHistory() {
        String filePath = "deleteTest.rul";
        File file = new File(filePath);
        RuleFileManager.clearHistory(filePath);
        assertEquals(false, file.exists());
    }

}
