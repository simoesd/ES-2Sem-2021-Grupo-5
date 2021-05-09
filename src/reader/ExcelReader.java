package reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import helpers.HelperMethods;

/**
 * The {@code ExcelReader} class is used to to create and deal with an ArrayList&#60;Line> from an excel file path, and compare Code Smells given another excel file path<p>
 * @since 1.0
 */	

public class ExcelReader {
    
	/**
	 * Given an excel file path, returns an ArrayList of Lines from the excel table.
	 * @param fileToImport		a String standing for an excel file path
	 * @return an ArrayList of Lines from the excel file
	 * @see Line
	 * @see XSSFWorkbook
	 * @see XSSFSheet
	 * @see Cell
	 * @see Row
	 * @see Iterator
	 * @see FileInputStream
	 * @see File
     */
	public static ArrayList<Line> readExcelFile(String fileToImport) {
    	try { 
    		File excel = new File(fileToImport); 
	    	FileInputStream fis = new FileInputStream(excel); 
	    	XSSFWorkbook excelWorkbook = new XSSFWorkbook(fis); 
	    	XSSFSheet excelSheet = excelWorkbook.getSheetAt(0); 
	    	Iterator<Row> rowIterator = excelSheet.iterator();
	    	
	    	List<Cell> columnNames = getColumnNames(rowIterator);
	    	
	    	ArrayList<Line> lineList = new ArrayList<>();
	    	
	    	while (rowIterator.hasNext()){
	    		Row row = rowIterator.next();
	    		Iterator<Cell> cellIterator = row.cellIterator();
	    		Line line = new Line();
	    		if (cellIterator.hasNext())
	    			line.setValues(columnNames.listIterator(), cellIterator);   	
	    		else
	    			break;
	    		lineList.add(line);
	    	}
	    	
	    	excelWorkbook.close();
	    	return lineList;
    	} catch (IOException ie) { 
    		return null;
    	}
    }
	
	
	/**
	 * Given an Iterator of Rows, returns an List of Cells from the Row.
	 * @param itr		an Iterator of Rows
	 * @return an List of Cells from the Row
	 * @see Cell
	 * @see Row
	 * @see Iterator
     */
	private static List<Cell> getColumnNames(Iterator<Row> itr)
	{
	    Row columnNameRow = itr.next();
        List<Cell> columnNames = new ArrayList<>();
        for(int i = 0; i < columnNameRow.getPhysicalNumberOfCells(); i++)
        {
            columnNames.add(columnNameRow.getCell(i));
        }
        return columnNames;
	}
	
	/**
	 * Given an ArrayList of Lines, returns an int vector identifying the number of different Line.getPkg(), and Line.getCls(), the size of the list, and the sum of the Line.getMetrics().get("LOC_class").
	 * @param lines 		an ArrayList of Lines 
	 * @return an int vector 
	 * @see Line
	 * @see HelperMethods
	 * @see ArrayList
     */ 
    public static int[] getProjectStats(ArrayList<Line> lines) { 
        ArrayList<String> classNames = new ArrayList<>();
        ArrayList<String> packageNames = new ArrayList<>();
        int totalLinesOfCode = 0;

        for (Line line : lines) {
            if (!classNames.contains(line.getPkg() + ".." + line.getCls())) { //Double period used as a separator for being an invalid character combination in both package and class names
                classNames.add(line.getPkg() + ".." + line.getCls());
                totalLinesOfCode += Integer.parseInt(HelperMethods.getCaseInsensitive(line.getMetrics(), "loc_class"));
            }
            if (!packageNames.contains(line.getPkg()))
                packageNames.add(line.getPkg());
        }

        int[] projectData = { packageNames.size(), classNames.size(), lines.size(), totalLinesOfCode};
        return projectData;
    }
    
    /**
	 * Given an ArrayList of Lines and a file path, returns a String matrix, comparing the boolean value of each Rule on Line.getMetrics(). 
	 * If the boolean value is true-true, adds "VP" to the matrix, 
	 * else if true-false, adds "FP", 
	 * else if false-true, adds "FN", 
	 * else if false-false, adds "VN", 
	 * else, adds "NA". 
	 * @param lines						an ArrayList of Lines
	 * @param comparisonFilePath		a String standing for an excel file path
	 * @return a String matrix
	 * @see Line
	 * @see Rule
	 * @see ArrayList
	 * @see LinkedList
	 * @see HelperMethods
	 * @since 2.0
     */
    public static String[][] compareCodeSmells(ArrayList<Line> lines, String comparisonFilePath) 
    {
    	String[][] comparisonResults = new String[lines.size()][];
		String[] metricNames = lines.get(0).getMetricNames();
		ArrayList<Line> dataToEvaluateCodeSmells = readExcelFile(comparisonFilePath);
		int numberOfRules = lines.get(0).getRuleNames().length;
		for (int i = 0; i < lines.size(); i++)
		{
			//get corresponding line from dataToEvaluateCodeSmells
			Line correspondingLine = null;
			for (Line line: dataToEvaluateCodeSmells)
			{
				
				if (line.getPkg().toLowerCase().equals(lines.get(i).getPkg().toLowerCase()) && line.getCls().toLowerCase().equals(lines.get(i).getCls().toLowerCase()) &&  line.getMethod().toLowerCase().equals(lines.get(i).getMethod().toLowerCase()))
				{
					correspondingLine = line;
					break;
				}
			}
			
			LinkedList<String> resultTableEntry = new LinkedList<>();
			resultTableEntry.add(String.valueOf(i+1));
			String ruleEvaluation;
			
			if (correspondingLine != null)
			{
				for (int u = 0; u < lines.get(i).getMetrics().size(); u++)
				{
					try {
						boolean cellValue = HelperMethods.customParseBoolean(lines.get(i).metricsToArray()[u]);
						String ruleName = metricNames[u];
						
						try {
							if (HelperMethods.containsKeyCaseInsensitive(correspondingLine.getMetrics(), ruleName))
							{
								boolean dataToEvaluateRuleValue = HelperMethods.customParseBoolean(HelperMethods.getCaseInsensitive(correspondingLine.getMetrics(), ruleName));
								if (cellValue)
									if (dataToEvaluateRuleValue)
										ruleEvaluation = "VP";
									else 
										ruleEvaluation = "FP";
								else
									if (dataToEvaluateRuleValue)
										ruleEvaluation = "FN";
									else
										ruleEvaluation = "VN";
							}
							else 
							{
								ruleEvaluation = "N/A";
							}
						} catch (IllegalArgumentException e) {
							ruleEvaluation = "N/A";
						}
						resultTableEntry.add(ruleEvaluation); 
					} catch (IllegalArgumentException e) {
						
					}
				}
			}
			else 
			{
				for (int j = 0; j < numberOfRules; j++)
				{
					resultTableEntry.add("N/A");
				}
			}
			comparisonResults[i] = resultTableEntry.toArray(new String[0]);
		}
    	return comparisonResults;
    }
}
