package metricas;

public class WMC_class extends Metrica {

	private Maestro metricas;

	public WMC_class(Maestro metricas) {
		this.metricas = metricas;
		
	}

	@Override
	public void extractMetrics() {
		this.counter("WMC");
		this.counter("metodo2");
//		System.out.println("3");

	}

	@Override
	public void applyFilter(String line) {
		// TODO Auto-generated method stub
		
	}

}
