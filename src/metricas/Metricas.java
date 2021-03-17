package metricas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.SortedMap;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

public class Metricas extends MetricRegistry {

	private LOC_class LOC_class;
	private NOM_class NOM_class;
	private WMC_class WMC_class;
	private LOC_method LOC_method;
	private CYCLO_method CYCLO_method;
	private ArrayList<Metricas> metrics;

	private String projectDirectory;
	private String sourceCodeLocation = "\\src";

	private ArrayList<File> filesInDirectory;

	public Metricas() {
		metrics = new ArrayList<Metricas>();
		filesInDirectory = new ArrayList<File>();
	}

	public Metricas(String projectDirectory) {
		metrics = new ArrayList<Metricas>();
		filesInDirectory = new ArrayList<File>();
		this.projectDirectory = projectDirectory;
	}

	

	public void startMetricCounters() {
		LOC_class = new LOC_class(this);
		NOM_class = new NOM_class(this);
		WMC_class = new WMC_class(this);
		LOC_method = new LOC_method(this);
		CYCLO_method = new CYCLO_method(this);
		metrics.add(LOC_class);
		metrics.add(NOM_class);
		metrics.add(WMC_class);
		metrics.add(LOC_method);
		metrics.add(getCYCLO_method());
	}

	public void result() {
		for (Metricas m : metrics) {
			SortedMap<String, Counter> helloMap = m.getCounters();
			Object[] counters = helloMap.values().toArray();
			System.out.println(((Counter) counters[0]).getCount());
			System.out.println(helloMap.values());
			System.out.println(helloMap.keySet());
			System.out.println(m.getCounters());
		}
	}

//		public abstract int contagem();
//		public abstract String nomeString();
	public void extractMetrics() {
	}

	public void startExtracting() {
		Thread t = new Thread(new Runnable() {

			public void run() {
				extractMetrics();
			}
		});
		t.start();
		
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
	
	public String cutAbsolutePath(String absolutePath){ //retorna package.class
		String shortPath = getProjectDirectory() + getSourceCodeLocation();
		int stringLength = shortPath.length() + 1;
		shortPath = absolutePath.substring(stringLength);
		shortPath = shortPath.replace("\\", ".");
		shortPath = shortPath.replace(".java", "");
		return shortPath;
	}
	
	public void applyFilter(String line){
	}

	public void openReadFile(File file) {
	    try {

	        Scanner sc = new Scanner(file);
	        while (sc.hasNextLine()) {
	            String line = sc.nextLine();
	            applyFilter(line);
	        }
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	}

	public WMC_class getWMC_class() {
		return WMC_class;
	}

	public ArrayList<File> getFilesInDirectory() {
		return filesInDirectory;
	}

	public CYCLO_method getCYCLO_method() {
		return CYCLO_method;
	}

	public LOC_class getLOC_class() {
		return LOC_class;
	}

	public NOM_class getNOM_class() {
		return NOM_class;
	}

	public LOC_method getLOC_method() {
		return LOC_method;
	}

	public String getProjectDirectory() {
		return projectDirectory;
	}

	public String getSourceCodeLocation() {
		return sourceCodeLocation;
	}

	
	
}