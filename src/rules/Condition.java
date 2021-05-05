package rules;

import java.io.Serializable;

import GUI.ConditionGUI;
import reader.Line;

public class Condition implements Serializable {
    
    public String metricToEvaluate;
    public int thresholdOperator;
    public int thresholdValue; 
    
    public static final int GREATER_THAN = 0;
    public static final int LESS_THAN = 1;
    public static final int GREATER_THAN_EQUAL = 2;
    public static final int LESS_THAN_EQUAL = 3;
    
    public Condition(String metricToEvaluate, int thresholdOperator, int thresholdValue)
    {
        this.metricToEvaluate = metricToEvaluate;
        this.thresholdOperator = thresholdOperator;
        this.thresholdValue = thresholdValue;
    }

    public boolean evaluateCondition(Line line) {
        int metricValue = Integer.parseInt(line.getMetrics().get(metricToEvaluate.toUpperCase()));
        boolean conditionValue = false;
        switch (thresholdOperator) {
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
        }
        return conditionValue;
    }
    
    
    public boolean isClassCondition()
    {
        return metricToEvaluate.toLowerCase().contains("class");
    }
}
