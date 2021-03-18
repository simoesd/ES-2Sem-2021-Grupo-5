package metricas;

import java.io.File;
import java.util.ArrayList;

import com.codahale.metrics.Counter;

public class CYCLO_method extends Metrica {

	private Maestro metricas;
	private final String filter = "for,if,while,case";
	private String packageClassName;

	public CYCLO_method(Maestro metricas) {
		this.metricas = metricas;
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
		Counter c =this.counter("ola");
		c.inc();
		
		metricas.getWMC_class().startExtracting();
	}

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
//			if (o.equals("for")||o.equals("if")||o.equals("while")||o.equals("case")){
//				//fazIsto
//			}

}
