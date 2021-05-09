package metricas;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/** 
 * LOC_method is a type of {@code Metrica} that counts the number of lines of a method
 * @see Metrica
 * @see Maestro
 * @since 1.0
 */
public class LOC_method extends Metrica {
	
	/** 
	 * Constructs and initializes a {@code LOC_method}
	 */
	public LOC_method() {
		metricName = "LOC_METHOD";
		isClassMetric = false;
	}
	
	/** 
	 * Constructs and initializes a {@code LOC_method}, given an object {@code Maestro} and a String. 
	 * This constructor is used only for Unit Testing
	 * @param maestro the maestro that will manipulate all the metrics
	 * @param unitTest the string used for unit testing
	 */ 
	
	public LOC_method (Maestro maestro, String unitTest) {
		super(maestro, unitTest);
		metricName = "LOC_CLASS";
		isClassMetric = true;
		
	}
	
	/** 
	 * Constructs and initializes a {@code LOC_method}, given an object {@code Maestro}.
	 * @param maestro the maestro that will manipulate all the metrics
	 */
	
	public LOC_method(Maestro maestro) {
		super(maestro);
		metricName = "LOC_METHOD";
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
     * Analyzes the given methodCode, incrementing the counter for each line it encounters, counting the lines of the method
     * @param methodCode string representing the method that will be analyzed
     */
	
	@Override
	public void applyMetricFilter(String methodCode) {
		Scanner scanner = new Scanner(methodCode);
		while (scanner.hasNextLine()) {
			scanner.nextLine();
			counter.inc();	   
		}
		scanner.close();
	}
	
}
