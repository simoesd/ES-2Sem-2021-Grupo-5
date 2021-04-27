package metricas;

import java.io.File;
import java.util.ArrayList;

import com.codahale.metrics.Counter;

public class CYCLO_method extends Metrica {

	private final String filter = "for,if,while,case";
	private Counter methodName = new Counter();

	public CYCLO_method(Maestro metricas) {
		super(metricas);
		metricName = "CYCLO_method";
		isClassMetric = false;
	}

	@Override
	protected void extractMetrics() {
		
		ArrayList<File> filesInDirectory = getMetricas().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMetricas().cutAbsolutePath(absolutePath));
			this.openReadFile(file);
		}

	}

	@Override
	protected void applyFilter(String s) {  //TODO Lidar com a situação dos comentários
		String[] line = s.split(" ");
		String[] filterToApply = filter.split(",");
		
		String temp = methodName(s, line);
		if (!temp.isBlank()) {
			methodName = new Counter();
			methodName = this.counter(getPackageClassName() + "." + temp);
		}
		for (String l : line) {
			l = l.replace("\t", "");
			for (String f : filterToApply) {
				if (l.equals(f)) {
					methodName.inc();
				}
			}
		}
	}
}
