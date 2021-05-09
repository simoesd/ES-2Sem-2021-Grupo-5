package metricas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import helpers.HelperMethods;
import reader.Line;
import rules.Rule;

/**
 * {@code Maestro} is an object that will initialize all the processing related to code smelling.
 * It will initialize the different {@code Metrica} extended objects that exist and create the xlsx file that will contain all the stats gathered.
 *
 * @see Metrica
 * @see CYCLO_method
 * @see LOC_class
 * @see LOC_method
 * @see NOM_class
 * @see WMC_class
 * @since 1.0
 */
public class Maestro {

	private LinkedList<Metrica> metrics = new LinkedList<Metrica>();
	private ArrayList<Rule> rules = new ArrayList<Rule>();

	private String projectDirectory;
	private static String SOURCE_CODE_LOCATION = File.separator + "src";

	private ArrayList<File> filesInDirectory = new ArrayList<File>();

	private int incrementer = 0;

	/**
	 * Constructs a {@code Maestro} object that hasn't been initialized.
	 */
	public Maestro() {
//		metrics = new ArrayList<Metrica>();
//		rules = new ArrayList<Rule>();
//		filesInDirectory = new ArrayList<File>();
	}

	/**
	 * Constructs and initializes a {@code Maestro} object with a pointer to the directory of the project to be analyzed.
	 * @param projectDirectory path to the directory of the project to be analyzed
	 */
	public Maestro(String projectDirectory) {
		this.projectDirectory = projectDirectory;
	}

	/**
	 * Calls the {@code openFolder(String dirPath)} method that receives a {@code String} containing the path to the project to analyze and adds {@code File} objects to the filesInDirectory {@code ArrayList} that represents all the files to be analyzed. 
	 * The it calls the {@code result()} method that returns a {@code String} containing the path to the xlsx file created with all the code smell metric analysis.
	 * 
	 * @return a {@code String} containing the path to the xlsx file created
	 * @see Maestro#openFolder(String)
	 * @see Maestro#result()
	 */
	public String startMetricCounters() {
		openFolder(projectDirectory);
		return result();
	}

	/**
	 * Initializes all the {@code Metrica} extended classes and waits for them to finish.
	 * Then calls the {@code createExcelFile()} method and returns its result (which will be a {@code String} containing the path to the xlsx file created).
	 * 
	 * @return a {@code String} containing the path to the xlsx file created
	 * @see Maestro#createExcelFile()
	 */
	private String result() {
        metrics.add(new LOC_class(this));
        metrics.add(new LOC_method(this));
        metrics.add(new CYCLO_method(this));
        
		metrics.forEach(t -> {
			try {
				t.getThread().join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		metrics.add(new WMC_class(this));
		metrics.add(new NOM_class(this));
		
		try {
            getWMC_class().getThread().join();
            getNOM_class().getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
		return createExcelFile();
	}
	
	/**
	 * Creates all the objects necessary to create a xlsx file and calls the methods {@code createHeaderExcel(XSSFSheet sheet)} and {@code exportResults(XSSFSheet sheet)} that will fill the sheet object with all the metric analysis.
	 * Then proceeds to save the new xlsx file and returns a {@code String} containing the path to the xlsx file created.
	 * 
	 * @return a {@code String} containing the path to the xlsx file created
	 * @see Maestro#createHeaderExcel(XSSFSheet)
	 * @see Maestro#exportResults(XSSFSheet)
	 * @see XSSFWorkbook
	 * @see XSSFSheet
	 */
	public String createExcelFile() {
		try {
			String projectDirectory = getProjectDirectory();
			projectDirectory=projectDirectory.replace(File.separator, "/");
			String splitProjectPath[] =projectDirectory.split("/");					
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet();
			createHeaderExcel(sheet);
			exportResults(sheet);	
			FileOutputStream fos = new FileOutputStream(getProjectDirectory() + File.separator + splitProjectPath[splitProjectPath.length -1] + "_metricas" + ".xlsx");
			workbook.write(fos);		
			fos.close();
			workbook.close();			
			incrementer = 1;
			return getProjectDirectory() + File.separator + splitProjectPath[splitProjectPath.length -1] + "_metricas" + ".xlsx";
		} catch (IOException e) {
		}
		return "";
	}

	/**
	 * This method uses the counters generated in each {@code Metrica} extended class and uses the names of the {@code Counter} objects to know which stat corresponds to which class or method, then calls the method {@code writeExcel(XSSFSheet sheet, String[] line)} for each line to insert in the sheet.
	 * 
	 * @param sheet {@code XSSFSheet} object that will be filled with the metrics stats
	 * @see Maestro#writeExcel(XSSFSheet, String[])
	 * @see Metrica
	 * @see XSSFWorkbook
	 * @see XSSFSheet
	 */
	private void exportResults(XSSFSheet sheet) {
		
		for (String packageClassName : getLOC_class().getCounters().keySet()) {
			String[] splitPackageClassName = packageClassName.replace("/", " ").split(" ");
			String namePck = splitPackageClassName[0];
			String nameClass = splitPackageClassName[1];
			Map<String, String> classMetrics = new LinkedHashMap<>();
			
			for (String packageClassMethodName : getCYCLO_method().getCounters().keySet()) {
				if ((packageClassMethodName + "/").contains(packageClassName)) {
					String[] split2 = packageClassMethodName.split("/");
					String nameMtd = split2[split2.length-1];
					LinkedHashMap<String, String> lineMetrics = new LinkedHashMap<>();
					lineMetrics.putAll(classMetrics);
					for (Metrica metric: metrics)
		            {
		                if (metric.isClassMetric())
		                    lineMetrics.put(metric.getMetricName(), String.valueOf(metric.getCounters().get(packageClassName).getCount()));
		                else
		                    lineMetrics.put(metric.getMetricName(), String.valueOf(metric.getCounters().get(packageClassMethodName).getCount()));
		            }
					
					Line line = new Line(incrementer, namePck, nameClass, nameMtd, lineMetrics);
					
					for (Rule rule: rules)
					    line.calculateRule(rule);
					
					writeExcel(sheet, line.toArray());

				}
			}
		}
	}

	/**
	 * Adds to the sheet object the header of the future xlsx file, containing the cells "MethodID", "Package", "Class", "Method" and all the metric and rule names.
	 * 
	 * @param sheet {@code XSSFSheet} object that will be filled with the metrics stats
	 * @see XSSFWorkbook
	 * @see XSSFSheet 
	 */
	public void createHeaderExcel(XSSFSheet sheet) {
	    Row firstRow = sheet.createRow(incrementer);
	    LinkedList<String> header = new LinkedList<>(Arrays.asList("MethodID", "Package", "Class", "Method"));
	    
	    for(Metrica metric: metrics)
	    {
            header.add(metric.getMetricName());
	    }

	    for(Rule rule: rules)
	        header.add(rule.getRuleName());
	    
        for (int i = 0; i < header.size(); i++) {
            Cell cell = firstRow.createCell(i);
            cell.setCellValue(header.get(i));
        }
        incrementer++;
	}

	/**
	 * Fills the sheet object with all the stats from the line parameter.
	 * 
	 * @param sheet {@code XSSFSheet} object that will be filled with the metrics stats
	 * @param line {@code String[]} array that contains all the stats to add to the sheet object
	 * @see XSSFWorkbook
	 * @see XSSFSheet
	 */
	public void writeExcel(XSSFSheet sheet, String[] line) {
	    Row firstRow = sheet.createRow(incrementer);
	    
        for (int i = 0; i < line.length; i++) {
			
            Cell cell = firstRow.createCell(i);
            try {
                int cellValue = Integer.parseInt(line[i]);
                cell.setCellValue(cellValue);
            } catch (NumberFormatException e) {
                try {
                    boolean readBoolean = HelperMethods.customParseBoolean(line[i]);
                    cell.setCellValue(readBoolean);
                } catch (IllegalArgumentException e1){
                    cell.setCellValue(line[i]);
                }
            }
        }
        incrementer++;

	}

	/**
	 * Creates a {@code File} object with the project directory path and calls the method {@code listFilesForFolder(File folder)}.
	 * This is done so the method {@code listFilesForFolder(File folder)} can be recursive.
	 * 
	 * @param dirPath {@code String} with the path to the directory containing the project to analyze
	 * @see Maestro#listFilesForFolder(File)
	 */
	public void openFolder(String dirPath) { 
		File folder = new File(dirPath);
		listFilesForFolder(folder);
	}

	/**
	 * This method searches for all the .java files inside the folder that the folder parameter represents and adds them to the filesInDirectory {@code ArrayList}.
	 * 
	 * If it finds a directory, it recurses itself with this new directory in the parameter.
	 * 
	 * @param folder {@code File} object representing a folder inside the project directory
	 */
	private void listFilesForFolder(File folder) {
		if(folder.listFiles().length != 0) {
			for (File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					listFilesForFolder(fileEntry);
				} else {
					if (fileEntry.getAbsolutePath().endsWith(".java")) {
						filesInDirectory.add(fileEntry);
					}
				}
			}
		}
	}
	
	/**
	 * Does {@code String} manipulation to turn the full path of a file to just the package and class separated by a forward slash (/). e.g. pkg/cls
	 * 
	 * @param absolutePath {@code String} containing the path of a file inside the project directory
	 * @return the package and class of the specified file separated by a forward slash (/)
	 */
	public String cutAbsolutePath(String absolutePath) { // retorna package/class
		String shortPath = getProjectDirectory() + SOURCE_CODE_LOCATION;
        int stringLength = shortPath.length() + 1;
        shortPath = absolutePath.substring(stringLength);
        shortPath = shortPath.replace(File.separator, ".");
        shortPath = shortPath.replace(".java", "");
        if(shortPath.split("\\.").length < 2)
            shortPath = "defaultPackage." +  shortPath;
        shortPath = shortPath.substring(0, shortPath.lastIndexOf(".")) + "/" + shortPath.substring(shortPath.lastIndexOf(".") + 1);
        return shortPath;
		
    }

	/**
	 * Adds a new {@code Rule} object to the rules {@code ArrayList}
	 * 
	 * @param rule new {@code Rule} object to the rules {@code ArrayList}
	 */
	public void addRule(Rule rule)
	{
	    rules.add(rule);
	}
	
	/**
	 * Adds multiple {@code Rule} objects to the rules {@code ArrayList}
	 * 
	 * @param rules {@code List} of new {@code Rule} objects to add to the rules {@code ArrayList}
	 */
	public void addRules(List<Rule> rules)
	{
	    this.rules.addAll(rules);
	}

	/**
	 * Returns the global variable {@code ArrayList<File>} filesInDirecory
	 * 
	 * @return the global variable {@code ArrayList<File>} filesInDirecory
	 */
	public ArrayList<File> getFilesInDirectory() {
		return filesInDirectory;
	}

	/**
	 * Returns the {@code LOC_class} object inside the global variable {@code LinkedList<Metrica>} metrics
	 * 
	 * @return the {@code LOC_class} object inside the global variable {@code LinkedList<Metrica>} metrics
	 */
	public LOC_class getLOC_class() {
		return (LOC_class) metrics.get(0);
	}

	/**
	 * Returns the {@code LOC_method} object inside the global variable {@code LinkedList<Metrica>} metrics
	 * 
	 * @return the {@code LOC_method} object inside the global variable {@code LinkedList<Metrica>} metrics
	 */
	public LOC_method getLOC_method() {
		return (LOC_method) metrics.get(1);
	}

	/**
	 * Returns the {@code CYCLO_method} object inside the global variable {@code LinkedList<Metrica>} metrics
	 * 
	 * @return the {@code CYCLO_method} object inside the global variable {@code LinkedList<Metrica>} metrics
	 */
	public CYCLO_method getCYCLO_method() {
		return (CYCLO_method) metrics.get(2);
	}

	/**
	 * Returns the {@code WMC_class} object inside the global variable {@code LinkedList<Metrica>} metrics
	 * 
	 * @return the {@code WMC_class} object inside the global variable {@code LinkedList<Metrica>} metrics
	 */
	public WMC_class getWMC_class() {
		return (WMC_class) metrics.get(3);
	}

	/**
	 * Returns the {@code NOM_class} object inside the global variable {@code LinkedList<Metrica>} metrics
	 * 
	 * @return the {@code NOM_class} object inside the global variable {@code LinkedList<Metrica>} metrics
	 */
	public NOM_class getNOM_class() {
		return (NOM_class) metrics.get(4);
	}

	/**
	 * Returns the global variable {@code String} projectDirectory
	 * 
	 * @return the global variable {@code String} projectDirectory
	 */
	public String getProjectDirectory() {
		return projectDirectory;
	}

	/**
	 * Adds a new {@code Metrica} object to the global variable {@code LinkedList<Metrica>} metrics
	 * 
	 * @param m new {@code Metrica} object to add to the global variable {@code LinkedList<Metrica>} metrics
	 */
	public void addMetric(Metrica m) {
		metrics.add(m);
	}
	
	/**
	 * Replace the value of the global variable {@code String} projectDirectory with the value of dir
	 * 
	 * @param dir new project directory
	 */
	public void setProjectDirectory(String dir) {
		projectDirectory = dir;
	}

}