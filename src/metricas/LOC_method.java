package metricas;

public class LOC_method extends Metricas{
	
	
	public LOC_method() {
		
		fazThread();
	}

	@Override
	public void TodasAsTuasContagens() {
		this.counter("LOC");
		this.counter("metodo2");
		System.out.println("4");
	}

}
