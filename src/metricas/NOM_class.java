package metricas;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedMap;

import com.codahale.metrics.Counter;

/** 
 * NOM_class is a type of {@code Metrica} that counts the number of methods of a class
 * @see Metrica
 * @see Maestro
 * @since 1.0
 */

public class NOM_class extends Metrica {
	
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
	 * Constructs and initializes a {@code NOM_class}, given an object {@code Maestro} and a String. 
	 * This constructor is used only for Unit Testing
	 * @param maestro the maestro that will manipulate all the metrics
	 * @param unitTest the string used for unit testing
	 */
	
	public NOM_class (Maestro maestro, String unitTest) {
		super(maestro, unitTest);
		metricName = "LOC_CLASS";
		isClassMetric = true;
		
	}
	
	/** 
	 * Constructs and initializes a {@code NOM_class}, given an object {@code Maestro}.
	 * @param maestro the maestro that will manipulate all the metrics
	 */
	
	public NOM_class(Maestro maestro) {
		super(maestro);
		metricName = "NOM_CLASS";
		isClassMetric = true;
	}
	
	
	/**
     * Prepares the names of the files that will be evaluated and calculates the numbers of methods of the class
     * by using the cycloSortedMap
     */
	@Override
	public void extractMetrics() {
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
