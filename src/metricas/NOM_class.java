package metricas;

import java.io.File;
import java.util.ArrayList;

public class NOM_class extends Metricas{
	
	private Metricas metricas;
	
	public NOM_class(Metricas metricas) {
		this.metricas=metricas;
		startExtracting();
	}

	@Override
	public void extractMetrics() {
		ArrayList<File> filesInDirectory = metricas.getFilesInDirectory();
		for (File file : filesInDirectory) {
			metricas.openReadFile(file);
		}
	}

}
