package metricas;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class LOC_method extends Metrica {

	public LOC_method() {
		metricName = "LOC_METHOD";
		isClassMetric = false;
	}
	
	public LOC_method(Maestro maestro) {
		super(maestro);
		metricName = "LOC_METHOD";
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
	public void applyMetricFilter(String methodCode) {
		Scanner scanner = new Scanner(methodCode);
		while (scanner.hasNextLine()) {
			scanner.nextLine();
			counter.inc();	   
		}
		scanner.close();
	}
	
}
