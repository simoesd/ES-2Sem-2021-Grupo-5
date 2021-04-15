package reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
	    	itr.next();
	    	
	    	ArrayList<Line> lineList = new ArrayList();
	    	while (itr.hasNext()) { 
	    		Row row = itr.next();
	    		Iterator<Cell> cellIterator = row.cellIterator();
	    		Line line = new Line();
	    		if (cellIterator.hasNext())line.setValues(cellIterator);   	
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
}
