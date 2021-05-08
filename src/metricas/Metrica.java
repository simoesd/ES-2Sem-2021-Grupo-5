package metricas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

public abstract class Metrica extends MetricRegistry {

	private Maestro maestro;
	private String packageClassName;
	public String metricName;
	private Thread myThread;
	public boolean isClassMetric;
	public Counter counter;
	protected boolean addLine, isNonMethodBlock = false, isMultiLineComment = false;
	protected int incr;
	protected String methodCode, line;
	private Stack<String> betweenMethodsBuffer = new Stack<>();

	public Metrica() {

	}

	public Metrica(Maestro maestro) {
		super();
		this.maestro = maestro;
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

	protected abstract void applyMetricFilter(String methodCode);

	protected void filterCode(File file) {
		try {
			Scanner sc = new Scanner(file);
			incr = -1;
			methodCode = "";
			while (sc.hasNextLine()) {
				addLine = false;
				line = sc.nextLine();
				filterOutJunk();

				char[] charLine = line.toCharArray();
				for (int i = 0; i != charLine.length; i++) {
					if (!isMultiLineComment && charLine[i] == '{') {
						handleOpenBracket();
					} else if (!isMultiLineComment && charLine[i] == '}') {
						handleCloseBracket();
					} else {
						if (incr > 0 && !isNonMethodBlock) // Linha dentro de um método
							addLine = true;
						if (incr == 0) { // Linha fora de método
							betweenMethodsBuffer.push(line);
						}
					}
				}

				if (!isMultiLineComment && addLine) {
					if (methodCode.isEmpty())
						methodCode = methodCode + line;
					else
						methodCode = methodCode + "\n" + line;
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void filterOutJunk() {
		Pattern[] patterns = { Pattern.compile("\'(.*?)\'"), // Pattern para reconhecer e retirar elementos entre ' '
				Pattern.compile("\"(.*?)\""), // Pattern para reconhecer e retirar elementos entre " "
				Pattern.compile("//.*|/\\*((.|\\n)(?!=*/))+\\*/") }; // Pattern para reconhecer e retirar elementos
																		// entre // \n || /* */
		for (Pattern p : patterns) {
			Matcher matcher = p.matcher(line);
			while (matcher.find()) {
				line = line.replace(matcher.group(), "");
			}
		}
		if (line.contains("/*")) { // É o ínicio de um MultiLineComment "/*"
			line = line.split("/*")[0];
			isMultiLineComment = true;
		} else if (line.contains("*/")) { // É o final de um MultiLineComment "*/"
			if (line.length() < 2)
				line = line.split("*/", 1)[1];
			else
				line = "";
			isMultiLineComment = false;
		}
	}

	public void handleOpenBracket() {
		switch (incr) {
		case -1: // Começou a class
			incr++;
			break;
		case 0: // Começou o método
			incr++;
			String methodName;
			if (!(methodName = getMethodName(betweenMethodsBuffer)).equals("")) {
				isNonMethodBlock = false;
				addLine = true;
				counter = counter(getPackageClassName() + methodName);
				betweenMethodsBuffer = new Stack<>();
			} else {
				isNonMethodBlock = true;
			}
			break;
		default: // Adicionar linha ao methodCode
			incr++;
			if (!isNonMethodBlock)
				addLine = true;
			break;
		}
	}

	public void handleCloseBracket() {
		switch (incr) {
		case 0: // Acabou a class
			incr--;
			break;
		case 1: // Acabou o método
			incr--;
			if (isNonMethodBlock) {
				isNonMethodBlock = false;
			} else {
				addLine = false;
				methodCode = methodCode + "\n" + line;
				applyMetricFilter(methodCode);
				methodCode = new String("");
			}
			break;
		default: // Adicionar linha ao methodCode
			incr--;
			if (!isNonMethodBlock)
				addLine = true;
			break;
		}
	}

	public String getMethodName(Stack<String> stack) {
        String methodName = stack.pop();
        while(!methodName.contains("(")) {
            if(!stack.isEmpty()) { 
            	String newLine = stack.pop();
                methodName = newLine + " " + methodName;
                if(newLine.endsWith(";")) {
                   return "";
                }
            }else {
                return "";
            }
        }
        methodName = methodName.trim();
        methodName = methodName.replaceAll("\t", "");
        String[] temp = methodName.split("\\(");
        String[] temp2 = temp[0].split(" ");
        String dataTypes = "";
        if(temp[1].length() > 1) {
        	String[] temp3 = temp[1].split("\\)");
        	String params = temp3[0];
	        String[] paramPairs = params.split(",");
	        List<String> paramWords = new ArrayList<>();
	        Arrays.asList(paramPairs).forEach(x -> paramWords.add(x.trim().split(" ")[0]));	        
	        for(int i = 0; i < paramWords.size(); i++) { // adding every other word from the parameters, to only get the dataTypes in the end
	        	if(paramWords.get(i).contains(".")) {
	        		dataTypes += paramWords.get(i).split("\\.")[paramWords.get(i).split("\\.").length -1] + ",";
	        	} else
	        		dataTypes += paramWords.get(i) + ",";
	        }
	        if(!dataTypes.isEmpty()) {
	        	dataTypes = dataTypes.substring(0, dataTypes.length() - 1); // remove the last ", "
	        }
        }
        methodName = temp2[temp2.length-1] + "(" + dataTypes + ")" ;
        return methodName;
    }

	protected Thread getThread() {
		return myThread;
	}

	protected Maestro getMaestro() {
		return maestro;
	}

	protected String getPackageClassName() {
		return packageClassName + "/";
	}

	protected void setPackageClassName(String packageClassName) {
		this.packageClassName = packageClassName;
	}

	public String getMetricName() {
		return metricName;
	}

	public boolean isClassMetric() {
		return isClassMetric;
	}

	public Counter getCounter() {
		return counter;
	}

	public void setCounter(Counter counter) {
		this.counter = counter;
	}
	
	

}
