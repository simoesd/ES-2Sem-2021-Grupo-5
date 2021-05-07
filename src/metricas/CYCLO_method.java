package metricas;


import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class CYCLO_method extends Metrica {

	private final String filter = "for,if,while,case";

	public CYCLO_method() {
		metricName = "CYCLO_METHOD";
		isClassMetric = false;
	}
	
	public CYCLO_method(Maestro maestro) {
		super(maestro);
		metricName = "CYCLO_METHOD";
		isClassMetric = false;
	}

	@Override
	protected void extractMetrics() {		
		ArrayList<File> filesInDirectory = getMaestro().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMaestro().cutAbsolutePath(absolutePath));
			filterCode(file);
		}
	}
	
	
	@Override
	protected void applyMetricFilter(String methodCode) {  
		Scanner scanner = new Scanner(methodCode);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] splitLine = line.split(" ");
			String[] filterToApply = filter.split(",");
			for (String word : splitLine) {
				word = word.replaceAll("\t", "");
				for (String f : filterToApply) {
					if (word.equals(f) || word.startsWith(f + "(")) {
						counter.inc();
					}
				}
			}		   
		}
		scanner.close();
	}	
}
