package metricas;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedMap;

import com.codahale.metrics.Counter;

/** 
 * WMC_class is a type of {@code Metrica} that sums the cyclomatic complexity of each method it contains,
 * evaluating the cyclomatic complexity of the class
 * @see Metrica
 * @see Maestro
 * @since 1.0
 */

public class WMC_class extends Metrica {
	
	/** 
	 * SortedMap representing the CYCLO_Method counters
	 */
	private SortedMap<String, Counter> cycloSortedMap;

	/** 
	 * Constructs and initializes a {@code WMC_class}
	 */
	public WMC_class() {
		metricName = "WMC_CLASS";
		isClassMetric = true;
	}
	
	/** 
	 * Constructs and initializes a {@code WMC_class}, given an object {@code Maestro} and a String. 
	 * This constructor is used only for Unit Testing
	 * @param maestro the maestro that will manipulate all the metrics
	 * @param unitTest the string used for unit testing
	 */
	
	public WMC_class (Maestro maestro, String unitTest) {
		super(maestro, unitTest);
		metricName = "LOC_CLASS";
		isClassMetric = true;
		
	}

	/** 
	 * Constructs and initializes a {@code WMC_class}, given an object {@code Maestro}.
	 * @param maestro the maestro that will manipulate all the metrics
	 */
	
	public WMC_class(Maestro maestro) {
		super(maestro);	
		metricName = "WMC_CLASS";
		isClassMetric = true;
	}
	
	/**
     * Prepares the names of the files that will be evaluated and calculates the cyclomatic complexity of the class
     * by using the cycloSortedMap
     */

	@Override
	public void extractMetrics() {
		
		cycloSortedMap = getMaestro().getCYCLO_method().getCounters();
		ArrayList<File> filesInDirectory = getMaestro().getFilesInDirectory();
		for (File file : filesInDirectory) {		
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMaestro().cutAbsolutePath(absolutePath));
			counter=counter(getPackageClassName());
			for (String counterName : cycloSortedMap.keySet()) {
				if (counterName.contains(getPackageClassName())) {
					counter.inc(cycloSortedMap.get(counterName).getCount());
				}
			}
		}
	}

	@Override
	protected void applyMetricFilter(String methodCode) {}

}
