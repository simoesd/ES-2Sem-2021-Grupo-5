package metricas;

import java.io.File;
import java.util.ArrayList;

import com.codahale.metrics.Counter;

public class LOC_class extends Metrica{
	
	private boolean enteredClass = false;
	private Counter className;

	public LOC_class(Maestro metricas) {
		super(metricas);
	}

	@Override
	protected void extractMetrics() {
		ArrayList<File> filesInDirectory = getMetricas().getFilesInDirectory();
		for (File file : filesInDirectory) {		
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMetricas().cutAbsolutePath(absolutePath));
			className=this.counter(getPackageClassName());
			this.openReadFile(file);
		}
	}
	
	@Override
	protected void applyFilter(String s) {
		if (isClass(s)) {
			enteredClass=true;
		}else{
			if (enteredClass==true) {
				if (!s.isBlank()) {
					className.inc();
				}
			}
		}
		
	}
}
