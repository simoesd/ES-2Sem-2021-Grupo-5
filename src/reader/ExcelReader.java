package reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
    
	public static ArrayList<Line> readExcelFile(String fileToImport) {
    	try { 
    		File excel = new File(fileToImport); 
	    	FileInputStream fis = new FileInputStream(excel); 
	    	XSSFWorkbook book = new XSSFWorkbook(fis); 
	    	XSSFSheet sheet = book.getSheetAt(0); 
	    	Iterator<Row> itr = sheet.iterator();
	    	
	    	List<Cell> columnNames = getColumnNames(itr);
	    	
	    	ArrayList<Line> lineList = new ArrayList();
	    	while (itr.hasNext()) { 
	    		Row row = itr.next();
	    		Iterator<Cell> cellIterator = row.cellIterator();
	    		Line line = new Line();
	    		if (cellIterator.hasNext())line.setValues(columnNames.listIterator(), cellIterator);   	
	    		else break;
	    		lineList.add(line);
	    	}
	    	book.close();
	    	return lineList;
    	} catch (FileNotFoundException fe) { 
    		fe.printStackTrace(); 
    		return null;
    	} catch (IOException ie) { 
    		ie.printStackTrace(); 
    		return null;
    	}
    }
	
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
}
