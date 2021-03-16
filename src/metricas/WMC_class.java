package metricas;

public class WMC_class extends Metricas{
	
	
	public WMC_class() {
		
		fazThread();
	}

	@Override
	public void TodasAsTuasContagens() {
		this.counter("WMC");
		this.counter("metodo2");
		System.out.println("3");
	}

}
