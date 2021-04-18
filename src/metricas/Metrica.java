package metricas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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

	protected abstract void applyFilter(String line);

	protected void openReadFile(File file) {
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				applyFilter(line);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	public String methodName(String s, String[] line) {
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
