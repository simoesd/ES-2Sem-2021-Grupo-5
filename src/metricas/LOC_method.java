package metricas;

import java.io.File;
import java.util.ArrayList;

import com.codahale.metrics.Counter;

public class LOC_method extends Metrica {

	// private final String filter = "class";
	private Counter methodName = new Counter();

	public LOC_method(Maestro metricas) {
		super(metricas);
	}

	@Override
	protected void extractMetrics() {
		ArrayList<File> filesInDirectory = getMetricas().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMetricas().cutAbsolutePath(absolutePath));
			this.openReadFile(file);
		}
	}	
	
	protected void applyFilter(String s) { // n�o lida totalmente com blocos de coment�rio nem metodos internos a outros
		// m�todos. tamb�m h� interferencias na definic�o de vetores entre {}
		String[] line = s.split(" ");
		String temp = methodName(s, line);
		s = s.trim();
		if (!s.startsWith("//") && !s.startsWith("*") && !s.startsWith("/*") && !s.startsWith("@")) {
			if (!temp.isBlank()) {
				methodName = new Counter();
				methodName = this.counter(getPackageClassName() + "." + temp);
			} else { // estamos dentro do m�todo
				if (!s.equals("{") && !s.equals("}") && !s.isBlank()) {
					methodName.inc();
				}
			}
		}
	}
}
