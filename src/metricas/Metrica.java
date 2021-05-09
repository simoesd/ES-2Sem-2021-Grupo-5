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

/**
 * {@code Metrica} is an object that represents and processes a certain code smell metric.
 * It extends the class {@code MetricRegistry} to make use of {@code Counter}.
 * 
 * @see MetricRegistry
 * @see Maestro
 * @see CYCLO_method
 * @see LOC_class
 * @see LOC_method
 * @see NOM_class
 * @see WMC_class
 */
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
	
	
	/**
	 * Constructs a {@code Metrica} object without initializing anything.
	 */
	public Metrica() {}
	
	/**
	 * Constructs and initializes a {@code Metrica} object. 
	 * Upon calling this constructor, the method {@code startExtracting()} will be called and a thread will start processing the code smell this{@code Metrica} represents.
	 * 
	 * @param {@code Maestro} object that contains this metric and that calls this constructor.
	 */
	public Metrica(Maestro maestro) {
		super();
		this.maestro = maestro;
		this.myThread = startExtracting();
		myThread.start();
	}

	/**
	 * Prepares the names of the files that will be evaluated and calls {@code filterCode()} for each of the files
	 */
	protected abstract void extractMetrics();

	/**
	 * Starts a new {@code Thread} that will run the method {@code extractMetrics()}
	 * 
	 * @return the created thread
	 * @see Metrica#extractMetrics()
	 */
	protected Thread startExtracting() {
		Thread t = new Thread(new Runnable() {

			public void run() {
				extractMetrics();
			}
		});
		return t;
	}
	
	/**
	 * 
	 * 
	 * @param methodCode
	 */
	protected abstract void applyMetricFilter(String methodCode);

	/**
	 * Receives a {@code File} representing a .java file and starts by calling {@code filterOutJunk()} to remove all comments, strings (text between double quotes (") and chars (text between ticks (')).
	 * Then it checks the file line by line and then character by character where methods start and end.
	 * When it finds brackets, it calls {@code handleOpenBracket()} and {@code handleCloseBracket()}.
	 * 
	 * @param file file to evaluate the code smell this {@code Metrica} represents
	 * @see Metrica#handleOpenBracket()
	 * @see Metrica#handleCloseBracket()
	 * @see Metrica#filterOutJunk()
	 */
	protected void filterCode(File file) {
		try {
			Scanner sc = new Scanner(file);
			incr = -1;
			methodCode = "";
			while (sc.hasNextLine()) {
				addLine = false;
				line = sc.nextLine();
				filterOutJunk();
				if (incr == 0) { // Linha fora de método
					betweenMethodsBuffer.push(line);
				}
				char[] charLine = line.toCharArray();
				for (int i = 0; i != charLine.length; i++) {
					if (!isMultiLineComment && charLine[i] == '{') {
						handleOpenBracket();
					} else if (!isMultiLineComment && charLine[i] == '}') {
						handleCloseBracket();
					} else {
						if (incr > 0 && !isNonMethodBlock) // Linha dentro de um método
							addLine = true;
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
	
	/**
	 * Removes all comments, strings (text between double quotes (") and chars (text between ticks (')) on the global variable {@code line}.
	 */
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

	/**
	 * Handles what happens when the method {@code filterCode()} finds an opening bracket.
	 * Makes use of the global variable {@code incr} that counts how many closing brackets are left to close a method or class.
	 * 
	 * 
	 * If this method is called when incr is -1, the code is entering this file's class;
	 * 
	 * If incr = 0 the code is entering a method or some other non method block (e.g. a declaration of an array).
	 * It will check which by calling the method {@code getMethodName()}, which will return the name of the method if it's a method, or an empty {@code String} if it is not a method.
	 * If it is not a method, the {@code boolean} isNonMethodBlock will be set to {@code true};
	 * If it is a method, a new {@code Counter} will be initialized calling the {@code counter(String name)} from the {@code MetricRegistry} class with the name of the package, class and method being checked.
	 * 
	 * If incr > 0, the code is inside a method and found an unimportant bracket to handle.
	 * 
	 * @see Metrica#handleCloseBracket()
	 * @see Metrica#getMethodName(Stack)
	 * @see MetricRegistry#counter(String)
	 */
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
	
	/**
	 * Handles what happens when the method {@code filterCode()} finds a closing bracket.
	 * Makes use of the global variable {@code incr} that counts how many closing brackets are left to close a method or class.
	 * 
	 * 
	 * If this method is called when incr is 0, the code is exiting this file's class;
	 * 
	 * If incr = 1 the code is exiting a method or some other non method block (e.g. a declaration of an array).
	 * It will check which by checking the value of the {@code boolean} isNonMethodBlock;
	 * If it is not a method, the {@code boolean} isNonMethodBlock is set to false;
	 * If it is a method, the method {@code applyMetricFilter(String methodCode)} is called with a {@code String} containing all the code from the method that has been exited so that the code smell can be evaluated, and the methodCode variable is reset;
	 * 
	 * If incr > 1, the code is inside a method and found an unimportant bracket to handle.
	 * 
	 * @see Metrica#handleOpenBracket()
	 * @see Metrica#applyMetricFilter(String)
	 */
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

	/**
	 * Receives a {@code Stack<String>} that contains the lines before the method start and checks them one by one until it finds a opening parethesis.
	 * If and when does, it processes the {@code String} so that it returns the name of the method with the parameters' data types.
	 * If it doesn't find an opening parenthesis, the method returns an empty {@code String}, indicating that there is no method.
	 * 
	 * @param stack a stack containing the lines before the method start and after the last method end
	 * @return the name of the method with the parameters' data types (e.g. someMethod(int,String)) or empty if there was no method
	 */
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
	
	/**
	 * Returns the global variable {@code Thread} myThread
	 * 
	 * @return the global variable {@code Thread} myThread
	 */
	protected Thread getThread() {
		return myThread;
	}

	/**
	 * Returns the global variable {@code Maestro} maestro that launched this {@code Metrica}
	 * 
	 * @return the global variable {@code Maestro} maestro
	 */
	protected Maestro getMaestro() {
		return maestro;
	}

	/**
	 * Returns the global variable {@code String} packageClassName concatenated with a forward slash (/) in the end
	 * 
	 * @return the global variable {@code String} packageClassName with a forward slash (/) in the end
	 */
	protected String getPackageClassName() {
		return packageClassName + "/";
	}

	/**
	 * Replaces the value of the global variable packageClassName to the value on the parameter packageClassName
	 * 
	 * @param packageClassName {@code String} containing the name of the package and class
	 */
	protected void setPackageClassName(String packageClassName) {
		this.packageClassName = packageClassName;
	}

	/**
	 * Returns the global variable {@code String} metricName
	 * 
	 * @return the global variable {@code String} metricName
	 */
	public String getMetricName() {
		return metricName;
	}

	/**
	 * Returns the global variable {@code boolean} isClassMetric
	 * 
	 * @return the global variable {@code boolean} isClassMetric
	 */
	public boolean isClassMetric() {
		return isClassMetric;
	}

	/**
	 * Returns the global variable {@code Counter} counter
	 * 
	 * @return the global variable {@code Counter} counter
	 */
	public Counter getCounter() {
		return counter;
	}

	/**
	 * Replaces the global variable {@code Counter} counter with the parameter {@code Counter} counter
	 * 
	 * @param counter a {@code Counter}
	 */
	public void setCounter(Counter counter) {
		this.counter = counter;
	}
	
	

}
