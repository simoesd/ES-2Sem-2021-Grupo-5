package metricas;

public class CYCLO_method extends Metricas{
	
	private Metricas metricas;
	
	public CYCLO_method(Metricas metricas) {
		this.metricas=metricas;
		startExtracting();
	}

	@Override
	public void extractMetrics() {
		this.counter("CYCLO");
		this.counter("metodo2");
		System.out.println("5");
		System.out.println(metricas.getFilesInDirectory());
	}

	

}
