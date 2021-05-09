package metricas;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedMap;

import com.codahale.metrics.Counter;

/** 
 * WMC_class is a type of {@code Metric} that sums the cyclomatic complexity of each method it contains,
 * evaluating the cyclomatic complexity of the class
 * @see Metric
 * @see MetricHandler
 * @since 1.0
 */

public class WMC_class extends Metric {
	
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
	 * Constructs and initializes a {@code WMC_class}, given an object {@code MetricHandler} and a String. 
	 * This constructor is used only for Unit Testing
	 * @param metricHandler the metricHandler that will manipulate all the metrics
	 * @param unitTest the string used for unit testing
	 */
	
	public WMC_class (MetricHandler metricHandler, String unitTest) {
		super(metricHandler, unitTest);
		metricName = "LOC_CLASS";
		isClassMetric = true;
		
	}

	/** 
	 * Constructs and initializes a {@code WMC_class}, given an object {@code MetricHandler}.
	 * @param metricHandler the metricHandler that will manipulate all the metrics
	 */
	
	public WMC_class(MetricHandler metricHandler) {
		super(metricHandler);	
		metricName = "WMC_CLASS";
		isClassMetric = true;
	}
	
	/**
     * Prepares the names of the files that will be evaluated and calculates the cyclomatic complexity of the class
     * by using the cycloSortedMap
     */

	@Override
	public void extractMetrics() {
		
		cycloSortedMap = getMetricHandler().getCYCLO_method().getCounters();
		ArrayList<File> filesInDirectory = getMetricHandler().getFilesInDirectory();
		for (File file : filesInDirectory) {		
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMetricHandler().cutAbsolutePath(absolutePath));
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
