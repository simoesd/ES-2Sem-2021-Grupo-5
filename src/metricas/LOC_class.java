package metricas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codahale.metrics.Counter;

public class LOC_class extends Metrica {

	private Counter counter;

	public LOC_class(Maestro metricas) {
		super(metricas);
	}

	@Override
	protected void extractMetrics() {
		ArrayList<File> filesInDirectory = getMaestro().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMaestro().cutAbsolutePath(absolutePath));
			counter = new Counter();
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
            Pattern pattern = Pattern.compile("//.*|/\\*((.|\\n)(?!=*/))+\\*/"); //Pattern para reconhecer e retirar elementos entre // \n || /* */
            boolean isMultiLineComment= false;            
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
				Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					    line = line.replace(matcher.group(), "");
				}
                if(line.contains("/*") ) { // É o ínicio de um MultiLineComment "/*"
                    isMultiLineComment = true;
                    line = line.split("/*")[0];
    			}else if(line.contains("*/") ) { // É o final de um MultiLineComment "*/"
                    isMultiLineComment = false;
                    if(line.length() < 2)
                  	  line = line.split("*/", 1)[1];
                    else 
                  	  line = "";
    			}
                if(!isMultiLineComment)
                	applyMetricFilter(line, counter);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
