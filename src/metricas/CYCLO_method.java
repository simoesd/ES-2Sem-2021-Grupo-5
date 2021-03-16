package metricas;

public class CYCLO_method extends Metricas{
	
	
	public CYCLO_method() {
		
		fazThread();
	}

	@Override
	public void TodasAsTuasContagens() {
		this.counter("CYCLO");
		this.counter("metodo2");
		System.out.println("5");
	}

}
