package metricas;

import java.io.File;
import java.util.ArrayList;

public class CYCLO_method extends Metricas{
	
	private Metricas metricas;
	private final String filter = "for,if,while,case";
	private String packageClassName;
	
	public CYCLO_method(Metricas metricas) {
		this.metricas=metricas;
//		startExtracting();
	}

	@Override
	public void extractMetrics() {
		ArrayList<File> filesInDirectory = metricas.getFilesInDirectory();
		System.out.println(filesInDirectory);
		for (File file : filesInDirectory) {
			File file2 = new File(file.getAbsolutePath());
			this.openReadFile(file2);
		}
//			String absolutePath = file.getAbsolutePath();
//			packageClassName = cutAbsolutePath(absolutePath);
//			System.out.println(file);
//			System.out.println(absolutePath);
			
			
		
		metricas.getWMC_class().startExtracting();
	}
	@Override
	public void applyFilter(String s){
		System.out.println("li");
		String[] line = s.split(" ");
		String[] filterToApply = filter.split(",");
		for (String l : line) {
			for(String f : filterToApply){
				if(l.equals(f)){
					//faz isto
				}
			}
//			if (o.equals("for")||o.equals("if")||o.equals("while")||o.equals("case")){
//				//fazIsto
//			}
		}
		
	}
	

}
