package reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;

import helpers.HelperMethods;
import rules.Rule;


/**
 * The {@code Line} class represents a line from an excel table of Code Smells.<p>
 * Here is an example of how to initialize a Line:
 * <blockquote><pre>
 * Line l = new Line(1, "package", "class", "method", new LinkedHashMap&#60;String, String>());
 * </pre></blockquote>
 * This class includes methods that return or change the value of its attributes.
 * @since 1.0
 */	

public class Line {
	private int methodID;
	private String pkg, cls, method; //package, class and method names
	
	private LinkedHashMap<String, String> metrics = new LinkedHashMap<>();
    
	/**
	 * Creates a new Line instance that represents an empty line.
     */	
    public Line()
    {
        
    }
    
    /**
	 * Creates a new {@code Line} instance given a methodID int, package string, class string, method string and linkedhashmap of strings.
	 * @param methodID		an int standing for the line id
	 * @param pkg			a String standing for a java package
	 * @param cls			a String standing for a java class
	 * @param method		a String standing for a java method
	 * @param metrics		a LinkedHashMap of strings standing for a list of metrics
	 * @see LinkedHashMap
     */   
    public Line(int methodID, String pkg, String cls, String method, LinkedHashMap<String, String> metrics)
    {
        this.methodID = methodID;
        this.pkg = pkg;
        this.cls = cls;
        this.method = method;
        this.metrics = metrics;
    }

    /**
	 * Creates a new {@code Line} instance that represents an empty line.
	 * @param columnNameIterator		a Iterator of Cells standing for an excel line header
	 * @param metricValueIterator		a Iterator of Cells standing for an excel line entry
	 * @see Cell
	 * @see Iterator
     */
	public void setValues(Iterator<Cell> columnNameIterator, Iterator<Cell> metricValueIterator) {
		Cell valueCell = metricValueIterator.next();
		Cell columnNameCell = columnNameIterator.next();
		switch (valueCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				break;
			default:
    			methodID = (int)valueCell.getNumericCellValue();
    			valueCell = metricValueIterator.next();
    			columnNameIterator.next();
    			pkg = valueCell.getStringCellValue() ;
    			valueCell = metricValueIterator.next();
    			columnNameIterator.next();
    			cls = valueCell.getStringCellValue();
    			valueCell = metricValueIterator.next();
    			columnNameIterator.next();
    			method = valueCell.getStringCellValue();
    			String metricValue;
    			
				do {
				    valueCell = metricValueIterator.next();
				    columnNameCell = columnNameIterator.next();
				    switch (valueCell.getCellType())
	                {
	                    case Cell.CELL_TYPE_BOOLEAN:
	                        metricValue = String.valueOf(valueCell.getBooleanCellValue());
	                        break;
	                    case Cell.CELL_TYPE_NUMERIC:
	                        metricValue = String.valueOf((int)valueCell.getNumericCellValue());
	                        break;
	                    default:
	                        metricValue = valueCell.getStringCellValue();
	                        break;
	                }
	                metrics.put(columnNameCell.getStringCellValue(), metricValue);
				} while (metricValueIterator.hasNext() && columnNameIterator.hasNext());
		}
	}		

	/**
	 * Returns the attribute method.
	 * @return the value of the attribute methodID
     */
	public int getMethodID() {
		return methodID;
	}
	
	/**
	 * Returns the attribute pkg.
	 * @return the value of the attribute pkg
     */
	public String getPkg() {
		return pkg;
	}
	
	/**
	 *Returns the attribute cls.
	 *@return the value of the attribute cls
     */
	public String getCls() {
		return cls;
	}
	
	/**
	 * Returns the attribute method.
	 * @return the value of the attribute pkg
     */
	public String getMethod()
	{
	    return method;
	}
	
	/**
	 * Returns the attribute metrics.
	 * @return the value of the attribute metrics
     */
	public LinkedHashMap<String, String> getMetrics()
	{
	    return metrics;
	}

	/**
	 * Returns a string array of class attribute names and metrics key names.
	 * @return a string array of class attribute names and metrics key names
     */
	public String[] getColumnNames()
	{
	    List<String> columnNames = new ArrayList<>();
	    String[] identifierColumns = {"MethodID", "Package", "Class", "Method"};
	    columnNames.addAll(Arrays.asList(identifierColumns));
	    columnNames.addAll(metrics.keySet());
        
	    return columnNames.toArray(new String[0]);
	}
	
	/**
	 * Returns a string array of metrics key names.
	 * @return a string array of metrics key names
     */
	public String[] getMetricNames() {
        List<String> columnValues = new ArrayList<>();
        columnValues.addAll(metrics.keySet());
        
        return columnValues.toArray(new String[0]);
    }
	
	/**
	 * Returns a string array of rule names from the linkedHashMap metrics.
	 * @return a string array of rule names from the linkedHashMap metrics
     */
	public String[] getRuleNames()
	{
	    LinkedList<String> ruleNames = new LinkedList<>();
	    for (Map.Entry<String, String> metric: metrics.entrySet())
	    {
	        try {
	            HelperMethods.customParseBoolean(metric.getValue());
	            ruleNames.add(metric.getKey());
	        } catch (IllegalArgumentException e) {
	        }
	    }
	    return ruleNames.toArray(new String[0]);
	}
	
	/**
	 * Returns a string array of class attribute values and metric mapped values.
	 * @return a string array of class attribute values and metric mapped values
     */
	public String[] toArray() {
	    List<String> columnValues = new ArrayList<>();
	    String[] identifierColumns = {Integer.toString(methodID), pkg, cls, method};
	    columnValues.addAll(Arrays.asList(identifierColumns));
	    columnValues.addAll(metrics.values());
	    
	    return columnValues.toArray(new String[0]);
	}
	
	/**
	 * Returns a string array of metric mapped values.
	 * @return a string array of metric mapped values
     */
	public String[] metricsToArray() {
        List<String> columnValues = new ArrayList<>();
        columnValues.addAll(metrics.values());
        
        return columnValues.toArray(new String[0]);
    }
	
	/**
	 * Appends the specified element to the linkedHashMap metrics.
	 * @param methodID		a String standing for a metric name
	 * @param methodID		a String standing for a metric value
     */
	public void addMetric(String metricName, String metric)
	{
	    metrics.put(metricName, metric);
	}
	
	/**
	 * Appends all the elements of the given hashMap to the linkedHashMap metrics.
	 * @param methodID		a HashMape of Strings standing for a list of metrics
	 * @see HashMap
     */
	public void addMetrics(HashMap<String, String> metrics)
	{
	    metrics.putAll(metrics);
	}
	
	/**
	 * Appends a Rule element to the linkedHashMap metrics.
	 * @param methodID		a Rule
	 * @see Rule
     */
	public void calculateRule(Rule rule)
	{
	    addMetric(rule.getRuleName(), Boolean.toString(rule.evaluateRule(this)));
	}
	
}
