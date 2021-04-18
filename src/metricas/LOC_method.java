package metricas;

import java.io.File;
import java.util.ArrayList;

import com.codahale.metrics.Counter;

public class LOC_method extends Metrica {

	// private final String filter = "class";
//	private Counter methodName = new Counter();

	public LOC_method(Maestro metricas) {
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
	
	protected void applyFilter(String s, Counter counter) { // não lida totalmente com blocos de comentário nem metodos internos a outros
		// métodos. também há interferencias na definicão de vetores entre {}
		s = s.trim();
		String[] line = s.split(" ");
		String temp = methodName(s, line);
		
		if (!s.startsWith("//") && !s.startsWith("*") && !s.startsWith("@") && !s.startsWith("/*") && !s.endsWith("*/")) {
			} else { // estamos dentro do método
				if (!s.equals("{") && !s.equals("}") && !s.isBlank()) {
					counter.inc();
				}
			}
	}
}
