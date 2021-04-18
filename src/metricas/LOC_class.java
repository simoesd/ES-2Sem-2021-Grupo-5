package metricas;

import java.io.File;
import java.util.ArrayList;

import com.codahale.metrics.Counter;

public class LOC_class extends Metrica {

	private boolean enteredClass = false;
	private Counter className;

	public LOC_class(Maestro metricas) {
		super(metricas);
	}

	@Override
	protected void extractMetrics() {
		ArrayList<File> filesInDirectory = getMaestro().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMaestro().cutAbsolutePath(absolutePath));
			className = new Counter();
			className = this.counter(getPackageClassName());
			this.openReadFile(file);
			enteredClass = false;
		}
	}

	@Override
	protected void applyFilter(String s) { //este programa ainda conta como 2 linhas 1 linha que foi separada em duas 
		s = s.trim();
		if (enteredClass ==false) {
			if (isClass(s)) {
				enteredClass = true;
			}
		} else {
				if (!s.startsWith("//") && !s.startsWith("*") && !s.startsWith("@") && !s.startsWith("/*")) { //não lida totalmente com os blocos de comentario
					if (!s.equals("{") && !s.equals("}") && !s.isBlank()) {
						className.inc();
					}
				}
		}
	}
}
