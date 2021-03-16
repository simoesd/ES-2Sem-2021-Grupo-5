package metricas;

public class NOM_class extends Metricas{
	
	
	public NOM_class() {
		
		fazThread();
	}

	@Override
	public void TodasAsTuasContagens() {
		this.counter("NOM");
		this.counter("metodo2");
		System.out.println("2");
	}

}
