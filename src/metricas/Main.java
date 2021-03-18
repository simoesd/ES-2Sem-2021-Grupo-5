package metricas;

public class Main {
	public static void main(String[] args) {

		Maestro metricas = new Maestro("E:\\ISCTE\\OneDrive - ISCTE-IUL\\eclipse-workspace\\87377_87524");
//		metricas.openFolder(metricas.getProjectDirectory() + metricas.getSourceCodeLocation());
//		otario.cutAbsolutePath("E:\\ISCTE\\OneDrive - ISCTE-IUL\\eclipse-workspace\\87377_87524\\src\\Workers\\DealWithTimeWorker.java");
		metricas.startMetricCounters();
		System.out.println(metricas.getFilesInDirectory());
		
//		otario.CYCLO_method.extractMetrics();   //funciona
//		otario.result();
//		otario.openReadFile(otario.filesInDirectory.get(1));
//		for (File file : otario.filesInDirectory) {
//			otario.openReadFile(file);
//		}
//			Counter counterWVMnum= metricas.counter("WVM_num");
//			counterWVMnum.inc();
//			counterWVMnum.inc();
//			counterWVMnum.inc();
//			counterWVMnum.inc();

//			SortedMap<String, Counter> helloMap = metricas.getCounters();

//			System.out.println(helloMap.get("WVM_num").getCount());

	}
}
