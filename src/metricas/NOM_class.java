package metricas;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedMap;

import com.codahale.metrics.Counter;

public class NOM_class extends Metrica {

	private Counter nomclass = new Counter();
	private SortedMap<String, Counter> cycloSortedMap;

	public NOM_class(Maestro metricas) {
		super(metricas);
		metricName = "NOM_class";
		isClassMetric = true;
	}

	@Override
	protected void extractMetrics() {
		cycloSortedMap = getMetricas().getCYCLO_method().getCounters();
		ArrayList<File> filesInDirectory = getMetricas().getFilesInDirectory();
		for (File file : filesInDirectory) {		
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMetricas().cutAbsolutePath(absolutePath));
			nomclass=this.counter(getPackageClassName());
			for (String s : cycloSortedMap.keySet()) {
				if (s.contains(getPackageClassName())) {
					nomclass.inc();
				}
			}
		}
	}

	@Override
	protected void applyFilter(String s) {
		// TODO Auto-generated method stub
	}
}
