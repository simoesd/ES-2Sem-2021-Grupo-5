package metricas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.codahale.metrics.Counter;

public class LOC_method extends Metrica {

	// private final String filter = "class";
		private Counter methodName = new Counter();

	public LOC_method(Maestro metricas) {
		super(metricas);
	}

	@Override
	protected void extractMetrics() {
		ArrayList<File> filesInDirectory = getMaestro().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMaestro().cutAbsolutePath(absolutePath));
			filterCode(file);
		}
	}	
	
	protected void applyMetricFilter(String s, Counter counter) { // n�o lida totalmente com blocos de coment�rio nem metodos internos a outros
		// m�todos. tamb�m h� interferencias na definic�o de vetores entre
		s = s.trim();
		String[] line = s.split(" ");
		String temp = getMethodName(s, line);
		
		if (!s.startsWith("//") && !s.startsWith("*") && !s.startsWith("@") && !s.startsWith("/*") && !s.endsWith("*/")) {
			} else { // estamos dentro do m�todo
				if (!s.equals("{") && !s.equals("}") && !s.isBlank()) {
					methodName.inc();
				}
			}
	}
	
}
