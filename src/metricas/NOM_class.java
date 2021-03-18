package metricas;

import java.io.File;
import java.util.ArrayList;

public class NOM_class extends Metrica{
	
	private Maestro metricas;
	private String packageClassName;
	private final String filter = "class";
	
	public NOM_class(Maestro metricas) {
		this.metricas=metricas;
		startExtracting();
	}

	@Override
	public void extractMetrics() {
		ArrayList<File> filesInDirectory = metricas.getFilesInDirectory();
		for (File file : filesInDirectory) {
			this.openReadFile(file);
			String absolutePath = file.getAbsolutePath();
			packageClassName = metricas.cutAbsolutePath(absolutePath);
		}
	}

	@Override
	public void applyFilter(String s) {
		String[] line = s.split(" ");
		String[] filterToApply = filter.split(",");
		for (String l : line) {
			for (String f : filterToApply) {
				if (l.equals(f)) {
					// faz isto
				}
			}
		}

	}
}
