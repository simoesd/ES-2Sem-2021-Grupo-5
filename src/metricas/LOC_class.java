package metricas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codahale.metrics.Counter;

public class LOC_class extends Metrica {

	public LOC_class(Maestro metricas) {
		super(metricas);
	}

	@Override
	protected void extractMetrics() {
		ArrayList<File> filesInDirectory = getMaestro().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMaestro().cutAbsolutePath(absolutePath));
			counter = this.counter(getPackageClassName());
			filterCode(file);
		}
	}
	
	@Override
	protected void applyMetricFilter(String s, Counter counter) { 
		s = s.replaceAll("\t", "");
		if(s != "" && !(s.startsWith("package") || s.startsWith("import")))
			counter.inc();
	}
	
	@Override
	protected void filterCode(File file) {
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                line = sc.nextLine();
				filterOutJunk();
                if(!isMultiLineComment)
                	applyMetricFilter(line, counter);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
