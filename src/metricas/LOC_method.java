package metricas;

public class LOC_method extends Metricas{
	
	private Metricas metricas;
	
	public LOC_method(Metricas metricas) {
		this.metricas=metricas;
		startExtracting();
	}

	@Override
	public void extractMetrics() {
		this.counter("LOC");
		this.counter("metodo2");
		System.out.println("4");

	}

}
