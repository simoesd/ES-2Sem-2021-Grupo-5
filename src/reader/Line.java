package reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

public class Line {
	private int methodID;
	private String pkg, cls, method; //package, class and method names

    public HashMap<String, String> metrics = new HashMap<>();

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
			} while (metricValueIterator.hasNext());
		}
	}		

	public String getPkg() {
		return pkg;
	}

	public String getCls() {
		return cls;
	}
	
	public HashMap<String, String> getMetrics()
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
	
	public String[] getColumnNames()
	{
	    List<String> columnNames = new ArrayList<>();
	    String[] identifierColumns = {"MethodID", "Package", "Class", "Method"};
	    columnNames.addAll(Arrays.asList(identifierColumns));
	    columnNames.addAll(getMetrics().keySet());
        
	    return columnNames.toArray(new String[0]);
	}

}
