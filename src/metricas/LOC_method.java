package metricas;

import java.io.File;
import java.util.ArrayList;

public class LOC_method extends Metricas{
	
	private Metricas metricas;
	private String packageClassName;
	private final String filter = "class";
	
	public LOC_method(Metricas metricas) {
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
//"public, private, ( , ) , { , }"
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
