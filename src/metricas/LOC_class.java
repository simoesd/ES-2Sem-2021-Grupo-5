package metricas;

import java.io.File;
import java.util.ArrayList;

public class LOC_class extends Metricas{
	
	private Metricas metricas;
	
	public LOC_class(Metricas metricas) {
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
//	"class, {, } "
}
