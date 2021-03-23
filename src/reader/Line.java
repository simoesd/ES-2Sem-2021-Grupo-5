package reader;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;

public class Line {
	private int id, nom_class, loc_class, wmc_class, loc_method, cyclo_method;
	private String pkg, cls, method;
	private Boolean is_god, is_long;
	
	public Line() {
		
	}

	public void setValues(Iterator<Cell> cellIterator) {
		Cell cell = cellIterator.next();
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				System.out.println("Empty");
				break;
			default:
			id = (int)cell.getNumericCellValue();
			cell = cellIterator.next();
			pkg = cell.getStringCellValue() ;
			cell = cellIterator.next();
			cls = cell.getStringCellValue();
			cell = cellIterator.next();
			method = cell.getStringCellValue();
			cell = cellIterator.next();
		    nom_class =(int)cell.getNumericCellValue();
			cell = cellIterator.next();
			loc_class = (int)cell.getNumericCellValue();
			cell = cellIterator.next();
			wmc_class = (int)cell.getNumericCellValue();
			cell = cellIterator.next();
			is_god = cell.getBooleanCellValue();
			cell = cellIterator.next();
			loc_method = (int)cell.getNumericCellValue();
			cell = cellIterator.next();
			cyclo_method = (int)cell.getNumericCellValue();
			cell = cellIterator.next();
			is_long = cell.getBooleanCellValue();
		}
	}		
	
	public int getNom_class() {
		return nom_class;
	}

	public int getLoc_class() {
		return loc_class;
	}

	public String getPkg() {
		return pkg;
	}

	public String getCls() {
		return cls;
	}

	public String[] toArray() {
	    String[] temp = {String.valueOf(id), pkg, cls, method, String.valueOf(nom_class), String.valueOf(loc_class), String.valueOf(wmc_class), is_god.toString(), String.valueOf(loc_method), String.valueOf(cyclo_method), is_long.toString()};
        return temp;
	}

}
