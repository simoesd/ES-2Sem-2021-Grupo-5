package metricas;


import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import com.codahale.metrics.Counter;

public class CYCLO_method extends Metrica {

	private final String filter = "for,if,while,case";

	public CYCLO_method(Maestro metricas) {
		super(metricas);
	}

	@Override
	protected void extractMetrics() {		
		ArrayList<File> filesInDirectory = getMaestro().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMaestro().cutAbsolutePath(absolutePath));
			filterCode(file);
		}
	}

	@Override
	protected void applyMetricFilter(String s, Counter counter) {  
		Scanner scanner = new Scanner(s);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] splitLine = line.split(" ");
			String[] filterToApply = filter.split(",");
			for (String l : splitLine) {
				l = l.replaceAll("\t", "");
				for (String f : filterToApply) {
					if (l.equals(f) || l.startsWith(f + "(")) {
						counter.inc();
					}
				}
			}		   
		}
		scanner.close();
	}	
}
