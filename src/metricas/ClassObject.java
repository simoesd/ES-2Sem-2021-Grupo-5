package metricas;

public class ClassObject {
	private String nameOfClass;
	private Integer numberofLines;
	
	public ClassObject (String nameOfClass) {
		this.nameOfClass = nameOfClass;
		this.numberofLines = this.countNOL();
	}
	
	private Integer countNOL() {
	 //conta-se o nr de linhas da classe tendo em conta o seu nome e retorna-se esse numero
	return 2;
	}
	
	
	
	
}
