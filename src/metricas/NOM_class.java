package metricas;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedMap;

import com.codahale.metrics.Counter;

/** 
 * NOM_class is a type of {@code Metric} that counts the number of methods of a class
 * @see Metric
 * @see MetricHandler
 * @since 1.0
 */

public class NOM_class extends Metric {
	
	/** 
	 * SortedMap representing the CYCLO_Method counters
	 */

	private SortedMap<String, Counter> cycloSortedMap;

	/** 
	 * Constructs and initializes a {@code NOM_class}
	 */
	public NOM_class() {
		metricName = "NOM_CLASS";
		isClassMetric = true;
	}
	
	/** 
	 * Constructs and initializes a {@code NOM_class}, given an object {@code MetricHandler} and a String. 
	 * This constructor is used only for Unit Testing
	 * @param metricHandler the metricHandler that will manipulate all the metrics
	 * @param unitTest the string used for unit testing
	 */
	
	public NOM_class (MetricHandler metricHandler, String unitTest) {
		super(metricHandler, unitTest);
		metricName = "LOC_CLASS";
		isClassMetric = true;
		
	}
	
	/** 
	 * Constructs and initializes a {@code NOM_class}, given an object {@code MetricHandler}.
	 * @param metricHandler the metricHandler that will manipulate all the metrics
	 */
	
	public NOM_class(MetricHandler metricHandler) {
		super(metricHandler);
		metricName = "NOM_CLASS";
		isClassMetric = true;
	}
	
	
	/**
     * Prepares the names of the files that will be evaluated and calculates the numbers of methods of the class
     * by using the cycloSortedMap
     */
	@Override
	public void extractMetrics() {
		cycloSortedMap = getMetricHandler().getCYCLO_method().getCounters();
		ArrayList<File> filesInDirectory = getMetricHandler().getFilesInDirectory();
		for (File file : filesInDirectory) {		
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMetricHandler().cutAbsolutePath(absolutePath));
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
