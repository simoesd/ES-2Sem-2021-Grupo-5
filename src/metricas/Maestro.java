package metricas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import reader.Line;
import rules.Rule;

public class Maestro {

	private ArrayList<Metrica> metrics = new ArrayList<Metrica>();
	private ArrayList<Rule> rules = new ArrayList<Rule>();

	private String projectDirectory;
	private static String SOURCE_CODE_LOCATION = "\\src";

	private ArrayList<File> filesInDirectory = new ArrayList<File>();

	private int incrementer = 0;

	public Maestro() {
//		metrics = new ArrayList<Metrica>();
//		rules = new ArrayList<Rule>();
//		filesInDirectory = new ArrayList<File>();
	}

	public Maestro(String projectDirectory) {
		metrics = new ArrayList<Metrica>();
		filesInDirectory = new ArrayList<File>();
		this.projectDirectory = projectDirectory;
	}

	public String startMetricCounters() {
		openFolder(projectDirectory);
		metrics.add(new LOC_class(this));
		metrics.add(new LOC_method(this));
		metrics.add(new CYCLO_method(this));
		return result();
	}

	private String result() {
		metrics.forEach(t -> {
			try {
				t.getThread().join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		metrics.add(new WMC_class(this));
		metrics.add(new NOM_class(this));
		try {
			getWMC_class().getThread().join();
			getNOM_class().getThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String projetctDirectory = getProjectDirectory();
			projetctDirectory=projetctDirectory.replace("\\", "/");
			String b[]=projetctDirectory.split("/");
					
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet();
			createHeaderExcel(sheet);
			exportResults(sheet);
			
			FileOutputStream fos = new FileOutputStream(getProjectDirectory() + "\\" + b[b.length -1] + "_metricas" + ".xlsx");
			workbook.write(fos);
			
			fos.close();
			workbook.close();
			
			incrementer = 1;
			return getProjectDirectory() + "\\" + b[b.length -1] + "_metricas" + ".xlsx";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ""; //TODO proper exception return
		}
	}

	private void exportResults(XSSFSheet sheet) {
		for (String u : getLOC_class().getCounters().keySet()) {
		    
			String temp = u;
			temp = u.replace(".", " ");
			String[] splitted = temp.split(" ");
			String namePck = splitted[0];
			String nameClass = splitted[1];
			Map<String, String> classMetrics = new HashMap<>();
			for (Metrica metric: metrics)
			{
			    if (metric.isClassMetric())
			        classMetrics.put(metric.getMetricName(), String.valueOf(metric.getCounters().get(u).getCount()));
			}
			
//			String LOC_class = String.valueOf(getLOC_class().getCounters().get(u).getCount());
//			String WMC_class = String.valueOf(getWMC_class().getCounters().get(u).getCount());
//			String NOM_class = String.valueOf(getNOM_class().getCounters().get(u).getCount());
			
			for (String s : getCYCLO_method().getCounters().keySet()) {
				if (s.contains(u)) {
					String temp2 = s;
					temp2 = s.replace(".", "/");
					String[] split2 = temp2.split("/");
					String nameMtd = split2[2];
					LinkedHashMap<String, String> lineMetrics = new LinkedHashMap<>();
					lineMetrics.putAll(classMetrics);
					for (Metrica metric: metrics)
		            {
		                if (!metric.isClassMetric())
		                    lineMetrics.put(metric.getMetricName(), String.valueOf(metric.getCounters().get(s).getCount()));
		            }
					
//					String CYCLO_method = String.valueOf(getCYCLO_method().getCounters().get(s).getCount());
//					String LOC_method = String.valueOf(getLOC_method().getCounters().get(s).getCount());
//					String[] lineString = {NOM_class, LOC_class, WMC_class, LOC_method, CYCLO_method};
					
					Line line = new Line(incrementer, namePck, nameClass, nameMtd, lineMetrics);
					for (Rule rule: rules)
					    line.calculateRule(rule);
					try {
						writeExcel(sheet, line.toArray());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void createHeaderExcel(XSSFSheet sheet) throws IOException {

	    Row firstRow = sheet.createRow(incrementer);
	    List<String> header = new ArrayList<>(Arrays.asList("MethodID", "Package", "Class", "Method"));
	    
	    for(Metrica metric: metrics)
            header.add(metric.getMetricName());
	    
	    for(Rule rule: rules)
	        header.add(rule.ruleName);
	    
        for (int i = 0; i < header.size(); i++) {
            Cell cell = firstRow.createCell(i);
            cell.setCellValue(header.get(i));
        }
        incrementer++;
	}

	private void writeExcel(XSSFSheet sheet, String[] line) throws IOException {

	    Row firstRow = sheet.createRow(incrementer);
	    
        for (int i = 0; i < line.length; i++) {
            Cell cell = firstRow.createCell(i);
            try {
                int cellValue = Integer.parseInt(line[i]);
                cell.setCellValue(cellValue);
            } catch (NumberFormatException e) {
                if (Boolean.parseBoolean(line[i])) {
                    switch (line[i].toLowerCase()) {
                        case "true":
                            cell.setCellValue(true);
                            break;
                        case "false":
                            cell.setCellValue(false);
                            break;
                    }
                } else {
                    cell.setCellValue(line[i]);
                }
            }
        }
        incrementer++;

	}

	public void openFolder(String str) { // str -> diretorio do projeto
		File folder = new File(str);
		listFilesForFolder(folder);
	}

	private void listFilesForFolder(File folder) {
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

	public String cutAbsolutePath(String absolutePath) { // retorna package.class
		String shortPath = getProjectDirectory() + getSourceCodeLocation();
		int stringLength = shortPath.length() + 1;
		shortPath = absolutePath.substring(stringLength);
		shortPath = shortPath.replace("\\", ".");
		shortPath = shortPath.replace(".java", "");
		return shortPath;
	}
	
	public void addRule(Rule rule)
	{
	    rules.add(rule);
	}
	
	public void addRules(List<Rule> rules)
	{
	    this.rules.addAll(rules);
	}

	public ArrayList<File> getFilesInDirectory() {
		return filesInDirectory;
	}

	public LOC_class getLOC_class() {
		return (LOC_class) metrics.get(0);
	}

	public LOC_method getLOC_method() {
		return (LOC_method) metrics.get(1);
	}

	public CYCLO_method getCYCLO_method() {
		return (CYCLO_method) metrics.get(2);
	}

	public WMC_class getWMC_class() {
		return (WMC_class) metrics.get(3);
	}

	public NOM_class getNOM_class() {
		return (NOM_class) metrics.get(4);
	}

	public String getProjectDirectory() {
		return projectDirectory;
	}

	public String getSourceCodeLocation() {
		return SOURCE_CODE_LOCATION;
	}

}