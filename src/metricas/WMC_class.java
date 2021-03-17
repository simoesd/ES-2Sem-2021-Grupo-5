package metricas;

public class WMC_class extends Metricas{
	
	private Metricas metricas;
	
	public WMC_class(Metricas metricas) {
		this.metricas=metricas;
		startExtracting();
	}

	@Override
	public void extractMetrics() {
		this.counter("WMC");
		this.counter("metodo2");
		System.out.println("3");
	}

}
