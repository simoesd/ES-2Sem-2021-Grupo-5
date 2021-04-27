package metricas;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedMap;

import com.codahale.metrics.Counter;

public class WMC_class extends Metrica {
	
	private SortedMap<String, Counter> cycloSortedMap;

	public WMC_class(Maestro metricas) {
		super(metricas);
		metricName = "WMC_class";
	}

	@Override
	protected void extractMetrics() {
		
		cycloSortedMap = getMaestro().getCYCLO_method().getCounters();
		ArrayList<File> filesInDirectory = getMaestro().getFilesInDirectory();
		for (File file : filesInDirectory) {		
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMaestro().cutAbsolutePath(absolutePath));
			counter=counter(getPackageClassName());
			for (String s : cycloSortedMap.keySet()) {
				if (s.contains(getPackageClassName())) {
					counter.inc(cycloSortedMap.get(s).getCount());
				}
			}
		}
		
	}

	@Override
	protected void applyMetricFilter(String line) {}

}
