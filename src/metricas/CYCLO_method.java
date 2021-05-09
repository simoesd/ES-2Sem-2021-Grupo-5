package metricas;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


/** 
 * CYCLO_method is a type of {@code Metrica} that increments its counter everytime
 * it finds certain words in the code inside of a method, evaluating the cyclomatic complexity of the method
 * @see Metrica
 * @see Maestro
 * @since 1.0
 */

public class CYCLO_method extends Metrica {

	/** 
	 * String representing the words (separated by commas) that increment the counter of the {@code CYCLO_method}  
	 */
	private final String filter = "for,if,while,case";
	
	/** 
	 * Constructs and initializes a {@code CYCLO_method}
	 */

	public CYCLO_method() {
		metricName = "CYCLO_METHOD";
		isClassMetric = false;
	}
	
	/** 
	 * Constructs and initializes a {@code CYCLO_method}, given an object {@code Maestro} and a String. 
	 * This constructor is used only for Unit Testing
	 * @param maestro the maestro that will manipulate all the metrics
	 * @param unitTest the string used for unit testing
	 */
	
	public CYCLO_method(Maestro maestro, String unitTest) { //Unit test
		super(maestro, unitTest);
		metricName = "CYCLO_METHOD";
		isClassMetric = false;
	}
	
	/** 
	 * Constructs and initializes a {@code CYCLO_method}, given an object {@code Maestro}.
	 * @param maestro the maestro that will manipulate all the metrics
	 */
	
	public CYCLO_method(Maestro maestro) {
		super(maestro);
		metricName = "CYCLO_METHOD";
		isClassMetric = false;
	}

	/**
     * Prepares the names of the files that will be evaluated and calls filterCode() for each of the files
     */
	@Override
	public void extractMetrics() {		
		ArrayList<File> filesInDirectory = getMaestro().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMaestro().cutAbsolutePath(absolutePath));
			filterCode(file);
		}
	}
	
	/**
     * Analyzes the given methodCode, incrementing the counter if it encounters any of the 
     * words present in the filter 
     * @param methodCode string representing the line of the method that will be analyzed 
     */
	
	@Override
	public void applyMetricFilter(String methodCode) {  
		Scanner scanner = new Scanner(methodCode);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] splitLine = line.split(" ");
			String[] filterToApply = filter.split(",");
			for (String word : splitLine) {
				word = word.replaceAll("\t", "");
				for (String f : filterToApply) {
					if (word.equals(f) || word.startsWith(f + "(")) {
						counter.inc();
					}
				}
			}		   
		}
		scanner.close();
	}	
}
