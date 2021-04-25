package metricas;

import java.io.File;
import java.util.ArrayList;

import com.codahale.metrics.Counter;

public class LOC_method extends Metrica {

	// private final String filter = "class";
	private Counter methodName = new Counter();

	public LOC_method(Maestro metricas) {
		super(metricas);
		metricName = "LOC_method";
	}

	@Override
	protected void extractMetrics() {
		ArrayList<File> filesInDirectory = getMetricas().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMetricas().cutAbsolutePath(absolutePath));
			this.openReadFile(file);
			methodName = new Counter();
		}
	}	
	
	protected void applyFilter(String s) { // não lida totalmente com blocos de comentário nem metodos internos a outros
		// métodos. também há interferencias na definicão de vetores entre {}
		s = s.trim();
		String[] line = s.split(" ");
		String temp = methodName(s, line);
		
		if (!s.startsWith("//") && !s.startsWith("*") && !s.startsWith("/*") && !s.startsWith("@")) {
			if (!temp.isBlank()) {
				methodName = new Counter();
				methodName = this.counter(getPackageClassName() + "." + temp);
			} else { // estamos dentro do método
				if (!s.equals("{") && !s.equals("}") && !s.isBlank()) {
					methodName.inc();
				}
			}
		}
	}
}
