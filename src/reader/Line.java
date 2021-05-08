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

public class Line {
	private int methodID;
	private String pkg, cls, method; //package, class and method names

	private LinkedHashMap<String, String> metrics = new LinkedHashMap<>();
    
    public Line()
    {
        
    }
    
//    public Line(int methodID, String pkg, String cls, String method)
//    {
//        this.methodID = methodID;
//        this.pkg = pkg;
//        this.cls = cls;
//        this.method = method;
//    }
    
    public Line(int methodID, String pkg, String cls, String method, LinkedHashMap<String, String> metrics)
    {
        this.methodID = methodID;
        this.pkg = pkg;
        this.cls = cls;
        this.method = method;
        this.metrics = metrics;
    }

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

	public String getPkg() {
		return pkg;
	}

	public String getCls() {
		return cls;
	}
	
	public LinkedHashMap<String, String> getMetrics()
	{
	    return metrics;
	}

	public String[] toArray() {
	    List<String> columnValues = new ArrayList<>();
	    String[] identifierColumns = {Integer.toString(methodID), pkg, cls, method};
	    columnValues.addAll(Arrays.asList(identifierColumns));
	    columnValues.addAll(metrics.values());
	    
	    return columnValues.toArray(new String[0]);
	}
	
	public String[] metricsToArray() {
        List<String> columnValues = new ArrayList<>();
        columnValues.addAll(metrics.values());
        
        return columnValues.toArray(new String[0]);
    }
	
	public String[] getColumnNames()
	{
	    List<String> columnNames = new ArrayList<>();
	    String[] identifierColumns = {"MethodID", "Package", "Class", "Method"};
	    columnNames.addAll(Arrays.asList(identifierColumns));
	    columnNames.addAll(getMetrics().keySet());
        
	    return columnNames.toArray(new String[0]);
	}
	
	public String[] getMetricNames() {
        List<String> columnValues = new ArrayList<>();
        columnValues.addAll(metrics.keySet());
        
        return columnValues.toArray(new String[0]);
    }
	
	public void addMetric(String metricName, String metric)
	{
	    metrics.put(metricName, metric);
	}
	
	public void addMetrics(HashMap<String, String> metrics)
	{
	    metrics.putAll(metrics);
	}
	
	public void calculateRule(Rule rule)
	{
	    addMetric(rule.getRuleName(), Boolean.toString(rule.evaluateRule(this)));
	}
	
	public String getMethod()
	{
	    return method;
	}
	
	public int getMethodID() {
		return methodID;
	}
	
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
}
