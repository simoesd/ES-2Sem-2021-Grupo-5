package rules;

import java.io.Serializable;
import java.util.LinkedList;

import reader.Line;

public class Rule implements Serializable{
    /**
	 * Represents a compound logic expression, composed of conditions and logic operators.
	 * 
	 * @see Condition
	 */
	private static final long serialVersionUID = -76936614853441537L;
	private String ruleName;
    private boolean isClassRule;
    
    private LinkedList<Condition> conditions = new LinkedList<>();
    private LinkedList<Integer> logicOperators = new LinkedList<>();
    
    public static final int AND = 0;
    public static final int OR = 1;

    /**
     * Creates rule with the specified {@code ruleName}, {@code conditions}, {@code logicOperators} and {@code isClassRule}.
     * 
     * @param ruleName String name of the rule to be created
     * @param conditions list of conditions that will make up the rule
     * @param logicOperators list of logicOperators that will be used to compare the specified conditions
     * @param isClassRule boolean with whether or not the rule to be created evaluates class metrics or not.
     * @throws IllegalArgumentException if the conditions and logic operators provided make up an invalid Rule. A valid rule with
     * n conditions must have n-1 logic operators.
     */
    public Rule(String ruleName, LinkedList<Condition> conditions, LinkedList<Integer> logicOperators, boolean isClassRule)
    {
        if (logicOperators.size() != conditions.size() - 1)
            throw new IllegalArgumentException();
        this.ruleName = ruleName;
        this.conditions = conditions;
        this.logicOperators = logicOperators;
        this.isClassRule = isClassRule;
    }
    
    
    /**
     * Evaluates the whole logic value of this rule, based on the values of the specified line. 
     * This value is obtained by alternating conditions and logic operators in {@code conditions} and {@code logicOperators}
     * 
     * @param lineToEvaluate the line that will be used to evaluate the value of each condition
     * @return {@code true} if the logic value of the rule is true.
     * 
     * @see Condition#evaluateCondition(Line)
     * @see #compareConditions(boolean, int, boolean)
     */
    public boolean evaluateRule(Line lineToEvaluate) {
        boolean result = conditions.get(0).evaluateCondition(lineToEvaluate);
        int i = 0;
        for (int logicOperator: logicOperators)
        {
            result = compareConditions(result, logicOperator, conditions.get(++i).evaluateCondition(lineToEvaluate));
        }
        return result;
    }
    
    /**
     * Compares the logic value of the specified condition values using the specified {@code logicOperator}
     * 
     * @param firstConditionValue first value to be used in the comparison. Will stand to the left of the specified operator
     * @param logicOperator integer that will be associated to a logic operator, which will be used to compare the two specified booleans
     * @param secondConditionValue second value to be used in the comparison. Will stand to the right of the specified operator
     * 
     * @throws IllegalArgumentException if the specified logic operator isn't valid i.e. not part of the accepted operators
     * @return {@code true} if the provided expression evaluates to true
     */
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
    
    /**
     * Returns the {@code condition} attribute of this rule.
     *
     * @return {@code logicOperators} attribute of this rule.
     */
    public LinkedList<Condition> getConditions()
    {
        return conditions;
    }
    
    /**
     * Returns the {@code logicOperators} attribute of this rule.
     *
     * @return {@code logicOperators} attribute of this rule.
     */
    public LinkedList<Integer> getLogicOperators()
    {
        return logicOperators;
    }
    
    /**
     * Returns the {@code ruleName} attribute of this rule.
     *
     * @return {@code ruleName} attribute of this rule.
     */
    public String getRuleName()
    {
        return ruleName;
    }
    
    /**
     * Returns the {@code isClassRule} attribute of this rule.
     *
     * @return {@code isClassRule} attribute of this rule.
     */
    public boolean isClassRule()
    {
        return isClassRule;
    }
    
    /**
     * Compares the specified object with this rule. Returns {@code true} if the specified object is a rule with the same attributes as this rule.
     * Compares {@code ruleName}, {@code isClassRule}, {@code conditions} and {@code logicOperators}
     * 
     * @param obj object to be compared for equality with this rule
     * @return {@code true} if the specified object is a rule with the same attributes as this rule.
     * @see Object#equals(Object)
     * @see Condition#equals(Object)
     */
    @Override
    public boolean equals(Object obj) { //implemented for unit tests
        boolean equals = false;
        if (obj instanceof Rule) {
            Rule rule = (Rule) obj;
            equals = (ruleName.equals(rule.ruleName)) && (isClassRule == rule.isClassRule) && (conditions.equals(rule.conditions) && (logicOperators.equals(rule.logicOperators)));
        }
        return equals;
    }
}