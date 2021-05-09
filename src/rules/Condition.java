package rules;

import java.io.Serializable;

import helpers.HelperMethods;
import reader.Line;

/**
 * Building block of {@code Rule}. Represents a logic expression where the value of a metric is
 * compared to a certain value, using the selected operation.
 */
public class Condition implements Serializable {
    

    private static final long serialVersionUID = 7122426817088652910L;
    /**
     * Name of the metric that will be evaluated in this condition
     */
	public String metricToEvaluate;
	/**
	 * Operator (greater/less than etc.) that will be used to compare {@code metricToEvaluate} with {@code thresholdValue}
	 */
    public int thresholdOperator;
    /**
     * Value with which {@code metricToEvaluate} will be compared
     */
    public int thresholdValue; 
    
    /**
     * Constant to be used as the greater than operator
     */
    public static final int GREATER_THAN = 0;
    /**
     * Constant to be used as the less than operator
     */
    public static final int LESS_THAN = 1;
    /**
     * Constant to be used as the greater than equal operator
     */
    public static final int GREATER_THAN_EQUAL = 2;
    /**
     * Constant to be used as the less than equal operator
     */
    public static final int LESS_THAN_EQUAL = 3;
    
    /**
     * Building block of {@code Rule}. Represents a logic expression where the value of a metric is compared to a certain value, using the selected operation.
     * For example, the condition "LOC_Class > 15", corresponds to the object {@code Condition("LOC_Class", GREATER_THAN, 15)}
     * 
     * @param metricToEvaluate name of the metric that will be evaluated in this condition
     * @param thresholdOperator operator (greater/less than etc.) that will be used to compare {@code metricToEvaluate} with {@code thresholdValue}
     * @param thresholdValue value with which {@code metricToEvaluate} will be compared
     * 
     * @since 1.0
     */
    public Condition(String metricToEvaluate, int thresholdOperator, int thresholdValue)
    {
        this.metricToEvaluate = metricToEvaluate;
        this.thresholdOperator = thresholdOperator;
        this.thresholdValue = thresholdValue;
    }

    /**
     * Evaluates the expression represented in the condition, utilizing the values present in the specified line.
     * The value for {@code metricToEvaluate} is obtained using {@code HelperMethods.getCaseInsensitive()}, a case insensitive equivalent to Map.get(K) for maps with String keys.
     * 
     * @param line line containing the a map with the value corresponding to the key {@code metricToEvaluate}
     * @throws NumberFormatException if {@code metricToEvaluate} is not a key in the specified line's map or if the value corresponding to {@code metricToEvaluate} cannot be parsed to an {@code int}
     * @throws IllegalArgumentException if the threshholdOperator in the condition is not valid i.e. not part of the constants
     * @return {@code true} if the condition evaluates to true if compared with the values in the specified line
     * @see HelperMethods#getCaseInsensitive(Map<String, V>, String)
     */
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
    
    /**
     * Compares the specified object with this condition. Returns {@code true} if the specified object is a condition with the same attributes as this condition.
     * Compares {@code metricToEvaluate}, {@code thresholdOperator} and {@code thresholdValue}
     * 
     * @param obj object to be compared for equality with this condition
     * @return {@code true} if the specified object is a condition with the same attributes as this condition.
     * @see Object#equals(Object)
     */
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
