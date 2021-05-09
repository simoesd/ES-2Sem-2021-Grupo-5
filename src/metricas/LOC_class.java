package metricas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/** 
 * LOC_class is a type of {@code Metric} that counts the number of lines of a class
 * @see Metric
 * @see MetricHandler
 * @since 1.0
 */

public class LOC_class extends Metric {
	
	/** 
	 * Constructs and initializes a {@code LOC_class}
	 */

	public LOC_class() {
		metricName = "LOC_CLASS";
		isClassMetric = true;
	}
	
	/** 
	 * Constructs and initializes a {@code LOC_class}, given an object {@code MetricHandler} and a String. 
	 * This constructor is used only for Unit Testing
	 * @param metricHandler the metricHandler that will manipulate all the metrics
	 * @param unitTest the string used for unit testing
	 */

	public LOC_class (MetricHandler metricHandler, String unitTest) {
		super(metricHandler, unitTest);
		metricName = "LOC_CLASS";
		isClassMetric = true;		
	}
	

	/** 
	 * Constructs and initializes a {@code LOC_class}, given an object {@code MetricHandler}.
	 * @param metricHandler the metricHandler that will manipulate all the metrics
	 */
	
	public LOC_class(MetricHandler metricHandler) {
		super(metricHandler);
		metricName = "LOC_CLASS";
		isClassMetric = true;
	}

	
	/**
     * Prepares the names of the files that will be evaluated and calls filterCode() for each of the files
     */
	@Override
	public void extractMetrics() {
		ArrayList<File> filesInDirectory = getMetricHandler().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMetricHandler().cutAbsolutePath(absolutePath));
			counter = this.counter(getPackageClassName());
			filterCode(file);
		}
	}

	/**
     * Analyzes the given methodCode, incrementing the counter if the line is valid
     * @param methodCode string representing the line of the class that will be analyzed 
     */
	
	@Override
	public void applyMetricFilter(String methodCode) {
		methodCode = methodCode.replaceAll("\t", "");
		if (methodCode != "" && !(methodCode.startsWith("package") || methodCode.startsWith("import")))
			counter.inc();
	}
	
	/**
     * Analyzes the given file and calls applyMetricFilter for every line that is worth analyzing 
     * (ex: does not analyze commented lines)
     * @param file file/class that will be analyzed
     */

	@Override
	public void filterCode(File file) {
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				filterOutJunk();
				if (!isMultiLineComment)
					applyMetricFilter(line);
			}
			sc.close();
		} catch (FileNotFoundException e) {
		}
	}

}
