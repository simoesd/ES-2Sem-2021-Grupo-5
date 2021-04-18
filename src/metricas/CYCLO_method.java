package metricas;

import java.io.File;
import java.util.ArrayList;

import com.codahale.metrics.Counter;

public class CYCLO_method extends Metrica {

	private final String filter = "for,if,while,case";
	private Counter methodCounter = new Counter();

	public CYCLO_method(Maestro metricas) {
		super(metricas);
	}

	@Override
	protected void extractMetrics() {
		
		ArrayList<File> filesInDirectory = getMaestro().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMaestro().cutAbsolutePath(absolutePath));
			openReadFile(file);
		}

	}

	@Override
	protected void applyFilter(String s) {  //TODO Lidar com a situa��o dos coment�rios
		String[] line = s.split(" ");
		String[] filterToApply = filter.split(",");
		String temp = methodName(s, line);
		if (!temp.isBlank()) {
			methodCounter = new Counter();
			methodCounter = counter(getPackageClassName() + "." + temp);
		}
		for (String l : line) {
			l = l.replace("\t", "");
			for (String f : filterToApply) {
				if (l.equals(f) || l.contains(f + "(")) {
					methodCounter.inc();
				}
			}
		}
	}
}
