package metricas;

public class LOC_class extends Metricas{
	
	
	public LOC_class() {
		fazThread();
	}

	@Override
	public void TodasAsTuasContagens() {
		this.counter("LOC CLASS");
		this.counter("metodo2");
		System.out.println("1");
	}

}
