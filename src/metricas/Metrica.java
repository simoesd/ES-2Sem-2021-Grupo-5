package metricas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

public abstract class Metrica extends MetricRegistry {

	private Maestro maestro;
	private String packageClassName;
	private Thread myThread;

	public Metrica(Maestro metricas) {
		super();
		this.maestro = metricas;
		this.myThread = startExtracting();
		myThread.start();
	}

	protected abstract void extractMetrics();

	protected Thread startExtracting() {
		Thread t = new Thread(new Runnable() {

			public void run() {
				extractMetrics();
			}
		});
		return t;
	}

	protected abstract void applyMetricFilter(String line, Counter counter);

	protected synchronized void filterCode(File file) {
		try {
			Scanner sc = new Scanner(file);
			int incr = -1;
			String methodCode = "";
			Pattern[] patterns = {Pattern.compile("\'(.*?)\'"), //Pattern para reconhecer e retirar elementos entre ' '
								  Pattern.compile("\"(.*?)\""), //Pattern para reconhecer e retirar elementos entre " "
								  Pattern.compile("//.*|/\\*((.|\\n)(?!=*/))+\\*/") }; 	//Pattern para reconhecer e retirar elementos entre // \n || /* */
			Counter counter = new Counter();
			boolean addLine, isClassOrEnum = false, isMultiLineComment= false;
			while (sc.hasNextLine()) {
				addLine = false;
				String line = sc.nextLine();
				for(Pattern p: patterns) {
					Matcher matcher = p.matcher(line);
					while (matcher.find()) {
					    line = line.replace(matcher.group(), "");
					}
				}
				if(line.contains("/*") ) { // � o �nicio de um MultiLineComment "/*"
	                  isMultiLineComment = true;
	                  line = line.split("/*")[0];
				}else if(line.contains("*/") ) { // � o final de um MultiLineComment "*/"
	                  isMultiLineComment = false;
	                  if(line.length() < 2)
	                	  line = line.split("*/", 1)[1];
	                  else 
	                	  line = "";
				}
				if(!isMultiLineComment && incr == 0 && (line.contains(" class ") || line.contains(" enum "))) { //Dete��o de classes aninhadas e enums
					isClassOrEnum = true;
				}
				char[] charLine = line.toCharArray();
				for(int i = 0; i != charLine.length; i++) {
					if(!isMultiLineComment && charLine[i] == '{') {
						switch(incr) { 
						case -1: // Come�ou a class
							incr++;
							break;
						case 0: //Come�ou o m�todo
							incr++;
							if(!isClassOrEnum) {							
								addLine = true;
								counter = new Counter();
								counter = counter(getPackageClassName() + "." + getMethodName(line, line.split(" ")));
							}
							break;
						default: //Adicionar linha ao methodCode
							incr++;
							if(!isClassOrEnum)
								addLine = true;
							break;
						}
					}else if(!isMultiLineComment && charLine[i] == '}') {
						switch(incr) {
						case 0: // Acabou a class
							incr--;
							break;
						case 1: //Acabou o m�todo
							incr--;
							if(isClassOrEnum) {
								isClassOrEnum = false;
							}else {
								addLine = true;
								applyMetricFilter(methodCode, counter);
								methodCode = new String("");
							}
							break;
						default: //Adicionar linha ao methodCode
							incr--;
							if(!isClassOrEnum)
								addLine = true;
							break;					
						}
					}else{
						if(incr > 0) //Linha dentro de um m�todo
							addLine = true;
					}
				}
				if(!isMultiLineComment && addLine) {
					methodCode = methodCode + "\n" + line;
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	public String getMethodName(String s, String[] line) {
		String methodName = "";
		s = s.trim();
		s = s.replaceAll("\t", "");
		if ((s.startsWith("public") || s.startsWith("protected") || s.startsWith("private")) && s.contains("(")
				&& s.contains(")") && !s.endsWith(";")) {

			int posicaoArray = positionArray(line, "(");
			int posicaoArray2 = positionArray(line, ")");

			methodName = line[posicaoArray];
			for (int u = posicaoArray + 1; u <= posicaoArray2; u++) {
				methodName = methodName + " " + line[u];
			}

			int indexTemp = s.indexOf("(");
			if (s.charAt(indexTemp - 1) == ' ')
				methodName = line[posicaoArray - 1].concat(methodName);

			return methodName;
		} else {
			return methodName;
		}
	}

	public boolean isClass(String s) {
		if ((s.startsWith("public") || s.startsWith("private")) && s.contains("class")) {
			return true;
		}
		return false;
	}

	private int positionArray(String[] line, String s) {
		int posicaoArray = -1;
		for (int i = 0; i < line.length; i++) {
			if (line[i].contains(s)) {
				posicaoArray = i;
				break;
			}
		}
		return posicaoArray;
	}

	protected Thread getThread() {
		return myThread;
	}

	protected Maestro getMaestro() {
		return maestro;
	}

	protected String getPackageClassName() {
		return packageClassName;
	}

	protected void setPackageClassName(String packageClassName) {
		this.packageClassName = packageClassName;
	}

}
