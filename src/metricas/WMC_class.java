package metricas;

public class WMC_class extends Metrica {

	public WMC_class(Maestro metricas) {
		super(metricas);	
	}

	@Override
	protected void extractMetrics() {
		this.counter("WMC");
		this.counter("metodo2");
//		System.out.println("3");

	}

	@Override
	protected void applyFilter(String line) {
		// TODO Auto-generated method stub
		
	}

}
