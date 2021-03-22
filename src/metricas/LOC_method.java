package metricas;

import java.io.File;
import java.util.ArrayList;

import com.codahale.metrics.Counter;

public class LOC_method extends Metrica {

	// private final String filter = "class";
	private Counter methodName = new Counter();

	public LOC_method(Maestro metricas) {
		super(metricas);
	}

	@Override
	protected void extractMetrics() {
		ArrayList<File> filesInDirectory = getMetricas().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMetricas().cutAbsolutePath(absolutePath));
			methodName = new Counter();
			this.openReadFile(file);
		}
	}
	
	@Override
	protected void applyFilter(String s) {
		String[] line = s.split(" ");
		// String[] filterToApply = filter.split(",");
		String temp = methodName(s, line);

		if (!temp.isBlank()) {
			methodName = new Counter();
			methodName = this.counter(getPackageClassName() + "." + temp);
		} else {
			if (!s.isBlank() && !s.contains("@Override"))
				methodName.inc();

		}
	}
}
