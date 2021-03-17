package metricas;

public class LOC_class extends Metricas{
	
	private Metricas metricas;
	
	public LOC_class(Metricas metricas) {
		this.metricas=metricas;
		startExtracting();
	}

	@Override
	public void extractMetrics() {
		this.counter("LOC CLASS");
		this.counter("metodo2");
		System.out.println("1");
	}

}
