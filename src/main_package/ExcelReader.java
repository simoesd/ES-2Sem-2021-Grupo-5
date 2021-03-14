package main_package;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
	public static void main( String[] args ) throws IOException{
    	try { 
    		File excel = new File("E://Downloads/Code_Smells.xlsx"); 
	    	FileInputStream fis = new FileInputStream(excel); 
	    	XSSFWorkbook book = new XSSFWorkbook(fis); 
	    	XSSFSheet sheet = book.getSheetAt(0); 
	    	Iterator<Row> itr = sheet.iterator();
	    	itr.next();
	    	while (itr.hasNext()) { 
	    		Row row = itr.next();
	    		Iterator<Cell> cellIterator = row.cellIterator();
	    		Line line = new Line();
	    		line.setValues(cellIterator);   		
		    	System.out.println(line.toString());
	    	}
    	} catch (FileNotFoundException fe) { 
    		fe.printStackTrace(); 
    	} catch (IOException ie) { 
    		ie.printStackTrace(); 
    	}
    }
}
