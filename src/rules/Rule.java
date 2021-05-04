package rules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import GUI.RuleGUI;
import reader.Line;

public class Rule implements Serializable{
    public String ruleName;
    public boolean isClassRule;
    
    public LinkedList<Condition> conditions = new LinkedList<>();
    public LinkedList<Integer> logicOperators = new LinkedList<>();
    
    public static final int AND = 0;
    public static final int OR = 1;

    
    public Rule(String ruleName, Condition condition, boolean isClassRule)
    {
        this.ruleName = ruleName;
        this.conditions.add(condition);
        this.isClassRule = isClassRule;
    }
    
    public Rule(String ruleName, String metric, int thresholdOperator, int thresholdValue, boolean isClassRule)
    {
        this.ruleName = ruleName;
        this.conditions.add(new Condition(metric, thresholdOperator, thresholdValue));
        this.isClassRule = isClassRule;
    }
    
    public Rule(String ruleName, LinkedList<Condition> conditions, LinkedList<Integer> logicOperators, boolean isClassRule)
    {
        if (logicOperators.size() != conditions.size() - 1)
            throw new IllegalArgumentException();
        this.ruleName = ruleName;
        this.conditions = conditions;
        this.logicOperators = logicOperators;
        this.isClassRule = isClassRule;
    }
    
    public void addCondition(Condition condition, Integer logicOperator)
    {
        this.conditions.add(condition);
        this.logicOperators.add(logicOperator);
    }
    
    public void addConditions(List<Condition> conditions, List<Integer> logicOperators) throws IllegalArgumentException
    {
        if (logicOperators.size() != conditions.size() - 1)
            throw new IllegalArgumentException();
        this.conditions.addAll(conditions);
        this.logicOperators.addAll(logicOperators);
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
    
    public boolean compareConditions(boolean firstConditionValue, int logicOperator, boolean secondConditionValue)
    {
        boolean result = false;
        switch (logicOperator) {
            case AND:
                result = firstConditionValue && secondConditionValue;
                break;
            case OR:
                result = firstConditionValue || secondConditionValue;
                break;
        }
        return result;
    }
    
    
}