package main_package;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;

public class Line {
	private int id, nom_class, loc_class, wmc_class, loc_method, cyclo_method;
	private String pkg, cls, method;
	private Boolean is_god, is_long;
	
	public Line(int id, String pkg, String cls, String method, int nom_class, int loc_class, int wmc_class, Boolean is_god, int loc_method, int cyclo_method, Boolean is_long) {
		this.id = id;
		this.nom_class = nom_class;
		this.loc_class = loc_class;
		this.wmc_class = wmc_class;
		this.loc_method = loc_method;
		this.pkg = pkg;
		this.cls = cls;
		this.method = method;
		this.is_god = is_god;
		this.cyclo_method = cyclo_method;
		this.is_long = is_long;
	}
	
	public int getCyclo_method() {
		return cyclo_method;
	}

	public void setCyclo_method(int cyclo_method) {
		this.cyclo_method = cyclo_method;
	}

	public Line() {
		
	}

	public void setValues(Iterator<Cell> cellIterator) {
		Cell cell = cellIterator.next();
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				System.out.println("Empty");
				break;
			default:
			setId((int)cell.getNumericCellValue());
			cell = cellIterator.next();
			setPkg(cell.getStringCellValue());
			cell = cellIterator.next();
			setCls(cell.getStringCellValue());
			cell = cellIterator.next();
			setMethod(cell.getStringCellValue());
			cell = cellIterator.next();
			setNom_class((int)cell.getNumericCellValue());
			cell = cellIterator.next();
			setLoc_class((int)cell.getNumericCellValue());
			cell = cellIterator.next();
			setWmc_class((int)cell.getNumericCellValue());
			cell = cellIterator.next();
			setIs_god(cell.getBooleanCellValue());
			cell = cellIterator.next();
			setLoc_method((int)cell.getNumericCellValue());
			cell = cellIterator.next();
			setCyclo_method((int)cell.getNumericCellValue());
			cell = cellIterator.next();
			setIs_long(cell.getBooleanCellValue());
		}
	}
	

	@Override
	public String toString() {
		return "Line [id=" + id + ", pkg=" + pkg + ", cls=" + cls
				+ ", method=" + method + ", nom_class=" + nom_class + ", loc_class=" + loc_class + ", wmc_class=" + wmc_class + ", is_god=" + is_god
				+ ", loc_method=" + loc_method + ", cyclo_method=" + cyclo_method + ", is_long=" + is_long + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNom_class() {
		return nom_class;
	}

	public void setNom_class(int nom_class) {
		this.nom_class = nom_class;
	}

	public int getLoc_class() {
		return loc_class;
	}

	public void setLoc_class(int loc_class) {
		this.loc_class = loc_class;
	}

	public int getWmc_class() {
		return wmc_class;
	}

	public void setWmc_class(int wmc_class) {
		this.wmc_class = wmc_class;
	}

	public int getLoc_method() {
		return loc_method;
	}

	public void setLoc_method(int loc_method) {
		this.loc_method = loc_method;
	}

	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Boolean getIs_god() {
		return is_god;
	}

	public void setIs_god(Boolean is_god) {
		this.is_god = is_god;
	}

	public Boolean getIs_long() {
		return is_long;
	}

	public void setIs_long(Boolean is_long) {
		this.is_long = is_long;
	}

}
