package metricas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LOC_class extends Metrica {

	public LOC_class() {
		metricName = "LOC_CLASS";
		isClassMetric = true;
	}

	public LOC_class (Maestro maestro, String unitTest) {
		super(maestro, unitTest);
		metricName = "LOC_CLASS";
		isClassMetric = true;
		
	}
	public LOC_class(Maestro maestro) {
		super(maestro);
		metricName = "LOC_CLASS";
		isClassMetric = true;
	}

	@Override
	public void extractMetrics() {
		ArrayList<File> filesInDirectory = getMaestro().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMaestro().cutAbsolutePath(absolutePath));
			counter = this.counter(getPackageClassName());
			filterCode(file);
		}
	}

	@Override
	public void applyMetricFilter(String methodCode) {
		methodCode = methodCode.replaceAll("\t", "");
		if (methodCode != "" && !(methodCode.startsWith("package") || methodCode.startsWith("import")))
			counter.inc();
	}

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
