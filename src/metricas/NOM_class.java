package metricas;

import java.io.File;
import java.util.ArrayList;

public class NOM_class extends Metrica{
	
	private final String filter = "class";
	
	public NOM_class(Maestro metricas) {
		super(metricas);
	}

	@Override
	protected void extractMetrics() {
		ArrayList<File> filesInDirectory = getMetricas().getFilesInDirectory();
		for (File file : filesInDirectory) {
			this.openReadFile(file);
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMetricas().cutAbsolutePath(absolutePath));
		}
	}
	
	@Override
	protected void applyFilter(String s) {
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
