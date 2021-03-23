package metricas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Maestro {

	private ArrayList<Metrica> metrics;

	private String projectDirectory;
	private static String SOURCE_CODE_LOCATION = "\\src";

	private ArrayList<File> filesInDirectory;

	private int incrementer = 1;

	public Maestro() {
		metrics = new ArrayList<Metrica>();
		filesInDirectory = new ArrayList<File>();
	}

	public Maestro(String projectDirectory) {
		metrics = new ArrayList<Metrica>();
		filesInDirectory = new ArrayList<File>();
		this.projectDirectory = projectDirectory;
	}

	public void startMetricCounters() {
		openFolder(projectDirectory + SOURCE_CODE_LOCATION);
		metrics.add(new LOC_class(this));
		metrics.add(new LOC_method(this));
		metrics.add(new CYCLO_method(this));
		result();
	}

	public void result() {
		metrics.forEach(t -> {
			try {
				t.getThread().join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		metrics.add(new WMC_class(this));
		metrics.add(new NOM_class(this));
		try {
			getWMC_class().getThread().join();
			getNOM_class().getThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileWriter csvWriter;
		try {
			String projetctDirectory = getProjectDirectory();
			projetctDirectory=projetctDirectory.replace("\\", "/");
			String b[]=projetctDirectory.split("/");
			csvWriter = new FileWriter(getProjectDirectory() + "\\" + b[b.length -1] + "_metricas" + ".csv");
			createHeaderExcel(csvWriter);
			exportResults(csvWriter);
			csvWriter.flush();
			csvWriter.close();
			incrementer = 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void exportResults(FileWriter csvWriter) {
		for (String u : getLOC_class().getCounters().keySet()) {
			String temp = u;
			temp = u.replace(".", " ");
			String[] splitted = temp.split(" ");
			String namePck = splitted[0];
			String nameClass = splitted[1];
			String LOC_class = String.valueOf(getLOC_class().getCounters().get(u).getCount());
			String WMC_class = String.valueOf(getWMC_class().getCounters().get(u).getCount());
			String NOM_class = String.valueOf(getNOM_class().getCounters().get(u).getCount());
			for (String s : getCYCLO_method().getCounters().keySet()) {
				if (s.contains(u)) {
					String temp2 = s;
					temp2 = s.replace(".", "/");
					String[] splitted2 = temp2.split("/");
					String nameMtd = splitted2[2];
					String CYCLO_method = String.valueOf(getCYCLO_method().getCounters().get(s).getCount());
					String LOC_method = String.valueOf(getLOC_method().getCounters().get(s).getCount());
					String line = namePck + ";" + nameClass + ";" + nameMtd + ";" + NOM_class + ";" + LOC_class + ";"
							+ WMC_class + ";" + " " + ";" + LOC_method + ";" + CYCLO_method + ";" + " ";
					try {
						writeExcel(csvWriter, line);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void createHeaderExcel(FileWriter csvWriter) throws IOException {

		csvWriter.append(
				"MethodID;Package;Class;Method; NOM_class;LOC_class;WMC_class;is_God_Class;LOC_method;CYCLO_method;is_Long_Method");
		csvWriter.append("\n");
	}

	private void writeExcel(FileWriter csvWriter, String line) throws IOException {

		csvWriter.append(String.valueOf(incrementer));
		csvWriter.append(";");
		csvWriter.append(line);
		csvWriter.append("\n");
		incrementer++;

	}

	public void openFolder(String str) { // str -> diretorio do projeto
		File folder = new File(str);
		listFilesForFolder(folder);
	}

	private void listFilesForFolder(File folder) {
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				if (fileEntry.getAbsolutePath().endsWith(".java")) {
					filesInDirectory.add(fileEntry);
				}
			}
		}
	}

	public String cutAbsolutePath(String absolutePath) { // retorna package.class
		String shortPath = getProjectDirectory() + getSourceCodeLocation();
		int stringLength = shortPath.length() + 1;
		shortPath = absolutePath.substring(stringLength);
		shortPath = shortPath.replace("\\", ".");
		shortPath = shortPath.replace(".java", "");
		return shortPath;
	}

	public ArrayList<File> getFilesInDirectory() {
		return filesInDirectory;
	}

	public LOC_class getLOC_class() {
		return (LOC_class) metrics.get(0);
	}

	public LOC_method getLOC_method() {
		return (LOC_method) metrics.get(1);
	}

	public CYCLO_method getCYCLO_method() {
		return (CYCLO_method) metrics.get(2);
	}

	public WMC_class getWMC_class() {
		return (WMC_class) metrics.get(3);
	}

	public NOM_class getNOM_class() {
		return (NOM_class) metrics.get(4);
	}

	public String getProjectDirectory() {
		return projectDirectory;
	}

	public String getSourceCodeLocation() {
		return SOURCE_CODE_LOCATION;
	}

}