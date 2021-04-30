package metricas;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedMap;

import com.codahale.metrics.Counter;

public class NOM_class extends Metrica {

	private SortedMap<String, Counter> cycloSortedMap;

	public NOM_class(Maestro metricas) {
		super(metricas);
		metricName = "NOM_CLASS";
		isClassMetric = true;
	}

	@Override
	protected void extractMetrics() {
		cycloSortedMap = getMaestro().getCYCLO_method().getCounters();
		ArrayList<File> filesInDirectory = getMaestro().getFilesInDirectory();
		for (File file : filesInDirectory) {		
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMaestro().cutAbsolutePath(absolutePath));
			counter=this.counter(getPackageClassName());
			for (String counterName : cycloSortedMap.keySet()) {
				if (counterName.contains(getPackageClassName())) {
					counter.inc();
				}
			}
		}
	}

	@Override
	protected void applyMetricFilter(String methodCode) {}
	
}
