package rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import reader.Line;

public class Rule {
    public String ruleName;
    
    public String firstMetric;              //
    public String firstThresholdOperator;   // These make up the first "condition" of the rule
    public int firstThresholdValue;         //
    
    public String logicOperator;
    
    public String secondMetric;              //
    public String secondThresholdOperator;   // These make up the second "condition" of the rule
    public int secondThresholdValue;             //
    
    public static final String LESS_THAN = "lt";
    public static final String LESS_THAN_EQUAL = "lte";
    public static final String GREATER_THAN = "gt";
    public static final String GREATER_THAN_EQUAL = "gte";
    
    public static final String AND = "AND";
    public static final String OR = "OR";

    
    public static void main(String[] args) { //TODO main temporário
        Line line = new Line();
        line.metrics.put("NOM_class", "29");
        line.metrics.put("LOC_class", "1371");
        line.metrics.put("WMC_class", "328");
        line.metrics.put("LOC_method", "3");
        line.metrics.put("CYCLO_method", "1");
        
        ArrayList<Rule> rules = new ArrayList<>();
        
        Rule long_method = new Rule("long_method", "LOC_method", "gt", 50, "AND", "CYCLO_method", "gt", 10);
        Rule god_class = new Rule("god_class", "WMC_class", "gt", 50, "OR", "NOM_class", "gt", 10);
        
        rules.add(long_method);
        rules.add(god_class);
        
        for (Rule rule: rules) {
            boolean ruleValue = rule.evaluateRule(line);
            line.metrics.put(rule.ruleName, Boolean.toString(ruleValue));
        }
        Set<String> keys = line.metrics.keySet();
        for (Map.Entry<String, String> entry: line.metrics.entrySet())
        {
            System.out.println(entry.getKey() + ": "+ entry.getValue());
        }
    }
    
    

    public Rule(String ruleName, String firstMetric, String firstThresholdOperator, int firstThresholdValue) {
        this.ruleName = ruleName;
        this.firstMetric = firstMetric;
        this.firstThresholdOperator = firstThresholdOperator;
        this.firstThresholdValue = firstThresholdValue;
    }

    public Rule(String ruleName, String firstMetric, String firstThresholdOperator, int firstThresholdValue, String logicOperator, String secondMetric, String secondThresholdOperator, int secondThresholdValue) {   
        this.ruleName = ruleName;
        this.firstMetric = firstMetric;
        this.firstThresholdOperator = firstThresholdOperator;
        this.firstThresholdValue = firstThresholdValue;
        this.logicOperator = logicOperator;
        this.secondMetric = secondMetric;
        this.secondThresholdOperator =  secondThresholdOperator;
        this.secondThresholdValue = secondThresholdValue;
    }
    
    
    
    public boolean evaluateRule(Line lineToEvaluate) {
        HashMap<String, String> metrics = lineToEvaluate.getMetrics();
        int firstMetricValue = Integer.parseInt(metrics.get(firstMetric));
        boolean firstConditionValue = evaluateCondition(firstMetricValue, firstThresholdValue);
        
        if (logicOperator.isEmpty())
            return firstConditionValue;
        
        int secondMetricValue = Integer.parseInt(metrics.get(secondMetric));
        boolean secondConditionValue = evaluateCondition(secondMetricValue, secondThresholdValue);
        
        boolean fullExpressionValue = false;
        switch (logicOperator) {
            case AND:
                fullExpressionValue = firstConditionValue && secondConditionValue;
                break;
            case OR:
                fullExpressionValue = firstConditionValue || secondConditionValue;
                break;
        }
        return fullExpressionValue;
    }
    
    public boolean evaluateCondition(int metricValue, int thresholdValue) {
        boolean conditionValue = false;
        switch (secondThresholdOperator) {
            case LESS_THAN:
                conditionValue = metricValue < thresholdValue;
                break;
            case LESS_THAN_EQUAL:
                conditionValue = metricValue <= thresholdValue;
                break;
            case GREATER_THAN:
                conditionValue = metricValue > thresholdValue;
                break;
            case GREATER_THAN_EQUAL:
                conditionValue = metricValue >= thresholdValue;
                break;
            default:
                break;
        }
        return conditionValue;
    }
    

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setFirstMetric(String firstMetric) {
        this.firstMetric = firstMetric;
    }

    public void setFirstThresholdOperator(String firstThresholdOperator) {
        this.firstThresholdOperator = firstThresholdOperator;
    }

    public void setFirstThresholdValue(int firstThresholdValue) {
        this.firstThresholdValue = firstThresholdValue;
    }

    public void setLogicOperator(String logicOperator) {
        this.logicOperator = logicOperator;
    }

    public void setSecondMetric(String secondMetric) {
        this.secondMetric = secondMetric;
    }
    
    public void setSecondThresholdOperator(String secondThresholdOperator) {
        this.secondThresholdOperator = secondThresholdOperator;
    }
    
    public void setSecondThresholdValue(int secondThresholdValue) {
        this.secondThresholdValue = secondThresholdValue;
    }
    
    
    
}
