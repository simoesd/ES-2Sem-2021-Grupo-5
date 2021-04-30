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
    
    public LinkedList<Condition> conditions = new LinkedList<>();
    public LinkedList<Integer> logicOperators = new LinkedList<>();
    
    public static final int AND = 0;
    public static final int OR = 1;

    
    public static void main(String[] args) { //TODO main temporário
        Line line = new Line(0, "123", "123", "123");
        line.addMetric("NOM_class", "29");
        line.addMetric("LOC_class", "1371");
        line.addMetric("WMC_class", "328");
        line.addMetric("LOC_method", "3");
        line.addMetric("CYCLO_method", "1");
        
        ArrayList<Rule> rules = new ArrayList<>();
        
        Condition condition1 = new Condition("LOC_method", Condition.GREATER_THAN, 50);
        Condition condition2 = new Condition("CYCLO_method", Condition.GREATER_THAN, 10);
        Condition condition3 = new Condition("WMC_class", Condition.GREATER_THAN, 50);
        Condition condition4 = new Condition("NOM_class", Condition.GREATER_THAN, 10);
        Rule long_method = new Rule("long_method", condition1);
        long_method.addCondition(condition2, AND);
        Rule god_class = new Rule("god_class", condition3);
        god_class.addCondition(condition4, OR);
        
        rules.add(long_method);
        rules.add(god_class);
        RuleFileManager.writeEntry(rules);
        
//        Map.Entry<String, List<Rule>> entry = RuleFileManager.readRules().entrySet().iterator().next();
//        
//        List<Rule> rules = entry.getValue();
        
//        for (Rule rule: rules) {
//            boolean ruleValue = rule.evaluateRule(line);
//            line.addMetric(rule.ruleName, Boolean.toString(ruleValue));
//        }
//        Set<String> keys = line.getMetrics().keySet();
//        for (Map.Entry<String, String> entry2: line.getMetrics().entrySet())
//        {
//            System.out.println(entry2.getKey() + ": "+ entry2.getValue());
//        }
        
    }
    
    public Rule(String ruleName, Condition condition)
    {
        this.ruleName = ruleName;
        this.conditions.add(condition);
    }
    
    public Rule(String ruleName, String metric, int thresholdOperator, int thresholdValue)
    {
        this.ruleName = ruleName;
        this.conditions.add(new Condition(metric, thresholdOperator, thresholdValue));
    }
    
    public Rule(String ruleName, LinkedList<Condition> conditions, LinkedList<Integer> logicOperators)
    {
        if (logicOperators.size() != conditions.size() - 1)
            throw new IllegalArgumentException();
        this.ruleName = ruleName;
        this.conditions = conditions;
        this.logicOperators = logicOperators;
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
//    
//    public RuleGUI generateRuleGUI(JPanel panel)
//    {
//        RuleGUI ruleGUI = new RuleGUI(panel, conditions.get(0).isClassCondition());
//        List<JComboBox<String>> logicOpGUI = new LinkedList<>();
//        for (Integer logicOp: logicOperators)
//        {
//            ruleGUI.addNewLogicOperator(logicOp);
//        }
//        conditions.forEach(x -> ruleGUI.addCondition(x.generateConditionGUI()));
//        return ruleGUI;
//    }
//    
    
}
