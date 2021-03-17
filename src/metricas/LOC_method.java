package metricas;

import java.io.File;
import java.util.ArrayList;

public class LOC_method extends Metricas{
	
	private Metricas metricas;
	
	public LOC_method(Metricas metricas) {
		this.metricas=metricas;
		startExtracting();
	}

	@Override
	public void extractMetrics() {
		ArrayList<File> filesInDirectory = metricas.getFilesInDirectory();
		for (File file : filesInDirectory) {
			openReadFile(file);
		}
	}
//"public, private, ( , ) , { , }"
}
