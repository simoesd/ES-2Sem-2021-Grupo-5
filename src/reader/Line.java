package reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

public class Line {
	private int id;
	private String pkg, cls, method;

    public HashMap<String, String> metrics = new HashMap<>();
	
	public Line() {
		
	}

	public void setValues(Iterator<Cell> columnNames, Iterator<Cell> cellIterator) {
		Cell cell = cellIterator.next();
		Cell columnCell = columnNames.next();
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				break;
			default:
    			id = (int)cell.getNumericCellValue();
    			cell = cellIterator.next();
    			columnNames.next();
    			pkg = cell.getStringCellValue() ;
    			cell = cellIterator.next();
    			columnNames.next();
    			cls = cell.getStringCellValue();
    			cell = cellIterator.next();
    			columnNames.next();
    			method = cell.getStringCellValue();
    			
    			String cellValue;
    			
			do {
			    cell = cellIterator.next();
			    columnCell = columnNames.next();
			    switch (cell.getCellType())
                {
                    case Cell.CELL_TYPE_BOOLEAN:
                        cellValue = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        cellValue = String.valueOf((int)cell.getNumericCellValue());
                        break;
                    default:
                        cellValue = cell.getStringCellValue();
                        break;
                }
                metrics.put(columnCell.getStringCellValue(), cellValue);
			} while (cellIterator.hasNext());
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
	    String[] identifierColumns = {Integer.toString(id), pkg, cls, method};
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
