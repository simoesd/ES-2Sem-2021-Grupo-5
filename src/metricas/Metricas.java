package metricas;

import java.io.File;
import java.util.ArrayList;
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

	private ArrayList<String> filesInDirectory;

	public Metricas() {
		metrics = new ArrayList<Metricas>();
		filesInDirectory = new ArrayList<String>();
	}

	public Metricas(String projectDirectory) {
		metrics = new ArrayList<Metricas>();
		filesInDirectory = new ArrayList<String>();
		this.projectDirectory = projectDirectory;
	}

	public static void main(String[] args) {

		Metricas otario = new Metricas("E:\\ISCTE\\OneDrive - ISCTE-IUL\\eclipse-workspace\\87377_87524");
		otario.openFolder(otario.projectDirectory + otario.sourceCodeLocation);
		otario.startMetricCounters();
		otario.result();

//			Counter counterWVMnum= metricas.counter("WVM_num");
//			counterWVMnum.inc();
//			counterWVMnum.inc();
//			counterWVMnum.inc();
//			counterWVMnum.inc();

//			SortedMap<String, Counter> helloMap = metricas.getCounters();

//			System.out.println(helloMap.get("WVM_num").getCount());

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
		metrics.add(CYCLO_method);
	}

	public void result() {
		// WMC_class.extractMetrics();
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
		t.interrupt();
	}

	public void openFolder(String str) { // str -> diretorio do projeto
		File folder = new File(str);
		int stringLength = str.length();
		stringLength++;
		listFilesForFolder(folder, stringLength);
	}

	private void listFilesForFolder(File folder, int i) {
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry, i);
			} else {
				if (fileEntry.getAbsolutePath().endsWith(".java")) {
					String absoluteFileEntry = fileEntry.getAbsolutePath().substring(i);
					absoluteFileEntry = absoluteFileEntry.replace("\\", ".");
					filesInDirectory.add(absoluteFileEntry);
				}
			}
		}
	}
	public WMC_class getWMC_class() {
		return WMC_class;
	}

	public ArrayList<String> getFilesInDirectory() {
		return filesInDirectory;
	}
}