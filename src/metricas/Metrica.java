package metricas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.codahale.metrics.MetricRegistry;

public abstract class Metrica extends MetricRegistry {

	public abstract void extractMetrics();
	
	public void startExtracting() {
		Thread t = new Thread(new Runnable() {

			public void run() {
				extractMetrics();
			}
		});
		t.start();
		
	}
	
	public abstract void applyFilter(String line);

	public void openReadFile(File file) {
	    try {

	        Scanner sc = new Scanner(file);
	        while (sc.hasNextLine()) {
	            String line = sc.nextLine();
	            applyFilter(line);
	        }
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	}

}
