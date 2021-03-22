package metricas;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedMap;

import com.codahale.metrics.Counter;

public class Maestro {

	private ArrayList<Metrica> metrics;

	private String projectDirectory;
	private static String SOURCE_CODE_LOCATION = "\\src";

	private ArrayList<File> filesInDirectory;

	public Maestro() {
		metrics = new ArrayList<Metrica>();
		filesInDirectory = new ArrayList<File>();
	}

	public Maestro(String projectDirectory) {
		metrics = new ArrayList<Metrica>();
		filesInDirectory = new ArrayList<File>();
		this.projectDirectory = projectDirectory;
	}

	public void startMetricCounters() {
		openFolder(projectDirectory + SOURCE_CODE_LOCATION);
		metrics.add(new LOC_class(this));
		metrics.add(new LOC_method(this));
		metrics.add(new CYCLO_method(this));
	}

	public void result() {
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
		for (Metrica m : metrics) {  //TODO Resultados
			SortedMap<String, Counter> counters = m.getCounters();
//			Object[] counters = helloMap.values().toArray();
//			System.out.println(((Counter) counters[0]).getCount());
//			System.out.println(helloMap.values());
//			System.out.println(helloMap.keySet());
//			System.out.println(m.getCounters());
			System.out.println(m.toString());
			for (String s : counters.keySet()) {
			    System.out.println(s);  
			    System.out.println(counters.get(s).getCount());
			}
		}
	}

//		public abstract int contagem();
//		public abstract String nomeString();

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