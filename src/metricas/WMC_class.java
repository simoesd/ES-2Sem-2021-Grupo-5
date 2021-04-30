package metricas;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedMap;

import com.codahale.metrics.Counter;

public class WMC_class extends Metrica {
	
	private Counter className;
	private SortedMap<String, Counter> cycloSortedMap;

	public WMC_class(Maestro metricas) {
		super(metricas);	
		metricName = "WMC_class";
		isClassMetric = true;
	}

	@Override
	protected void extractMetrics() {
		
		cycloSortedMap = getMetricas().getCYCLO_method().getCounters();
		ArrayList<File> filesInDirectory = getMetricas().getFilesInDirectory();
		for (File file : filesInDirectory) {		
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMetricas().cutAbsolutePath(absolutePath));
			className=this.counter(getPackageClassName());
			for (String s : cycloSortedMap.keySet()) {
				if (s.contains(getPackageClassName())) {
					className.inc(cycloSortedMap.get(s).getCount());
				}
			}
		}
		
	}

	@Override
	protected void applyFilter(String line) {
		// TODO Auto-generated method stub
		
	}

}
