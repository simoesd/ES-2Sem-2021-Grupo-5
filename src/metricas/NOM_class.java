package metricas;

public class NOM_class extends Metricas{
	
	private Metricas metricas;
	
	public NOM_class(Metricas metricas) {
		this.metricas=metricas;
		startExtracting();
	}

	@Override
	public void extractMetrics() {
		this.counter("NOM");
		this.counter("metodo2");
		System.out.println("2");
	}

}
