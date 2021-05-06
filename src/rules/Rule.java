package rules;

import java.io.Serializable;
import java.util.LinkedList;

import reader.Line;

public class Rule implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -76936614853441537L;
	public String ruleName;
    public boolean isClassRule;
    
    public LinkedList<Condition> conditions = new LinkedList<>();
    public LinkedList<Integer> logicOperators = new LinkedList<>();
    
    public static final int AND = 0;
    public static final int OR = 1;

    
    public Rule(String ruleName, LinkedList<Condition> conditions, LinkedList<Integer> logicOperators, boolean isClassRule)
    {
        if (logicOperators.size() != conditions.size() - 1)
            throw new IllegalArgumentException();
        this.ruleName = ruleName;
        this.conditions = conditions;
        this.logicOperators = logicOperators;
        this.isClassRule = isClassRule;
    }
    
    public boolean evaluateRule(Line lineToEvaluate) {
        boolean result = conditions.get(0).evaluateCondition(lineToEvaluate);
        int i = 0;
        for (int logicOperator: logicOperators)
        {
            result = compareConditions(result, logicOperator, conditions.get(++i).evaluateCondition(lineToEvaluate));
        }
        return result;
    }
    
    public boolean compareConditions(boolean firstConditionValue, int logicOperator, boolean secondConditionValue) throws IllegalArgumentException
    {
        boolean result = false;
        switch (logicOperator) {
            case AND:
                result = firstConditionValue && secondConditionValue;
                break;
            case OR:
                result = firstConditionValue || secondConditionValue;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result;
    }
    
    
}