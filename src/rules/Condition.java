package rules;

import java.io.Serializable;

import helpers.HelperMethods;
import reader.Line;

public class Condition implements Serializable {
    

    private static final long serialVersionUID = 7122426817088652910L;
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

    public boolean evaluateCondition(Line line) throws IllegalArgumentException, NumberFormatException{
        boolean conditionValue = false;
        int metricValue = Integer.parseInt(HelperMethods.getCaseInsensitive(line.getMetrics(), metricToEvaluate));
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
            default:
                throw new IllegalArgumentException();
        }
        return conditionValue;
    }
    
    
    @Override
    public boolean equals(Object obj) { //implemented for unit tests
        boolean equals = false;
        if (obj instanceof Condition) {
            Condition condition = (Condition) obj;
            equals = (metricToEvaluate.equals(condition.metricToEvaluate)) && (thresholdOperator == condition.thresholdOperator) && (thresholdValue == condition.thresholdValue);
        }
        return equals;
    }
}
