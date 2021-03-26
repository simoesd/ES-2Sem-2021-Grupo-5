package metricas;

import java.io.File;
import java.util.ArrayList;

import com.codahale.metrics.Counter;

public class LOC_method extends Metrica {

	// private final String filter = "class";
	private Counter methodName = new Counter();
	private int countOpenBrace = 0;
	private int countCloseBrace = 0;
	private boolean nextLineIsBrace = false;

	public LOC_method(Maestro metricas) {
		super(metricas);
	}

	@Override
	protected void extractMetrics() {
		ArrayList<File> filesInDirectory = getMetricas().getFilesInDirectory();
		for (File file : filesInDirectory) {
			String absolutePath = file.getAbsolutePath();
			setPackageClassName(getMetricas().cutAbsolutePath(absolutePath));
			methodName = new Counter();
			this.openReadFile(file);
		}
	}

//	@Override
	/*
	 * protected void applyFilter(String s) { String[] line = s.split(" "); //
	 * String[] filterToApply = filter.split(","); String temp = methodName(s,
	 * line);
	 * 
	 * if (!temp.isBlank()) { methodName = new Counter(); methodName =
	 * this.counter(getPackageClassName() + "." + temp); } else { if (!s.isBlank()
	 * && !s.contains("@Override")) methodName.inc();
	 * 
	 * } }
	 */

	/*
	 * protected void applyFilter(String s) { // não lida totalmente com blocos de
	 * comentário nem metodos internos a outros // métodos. também há interferencias
	 * na definicão de vetores entre {} String[] line = s.split(" "); String temp =
	 * methodName(s, line); s = s.trim(); if (!s.startsWith("//") &&
	 * !s.startsWith("*") && !s.startsWith("/*")) { if (!temp.isBlank()) {
	 * countOpenBrace++; methodName = new Counter(); methodName =
	 * this.counter(getPackageClassName() + "." + temp);
	 * 
	 * } else if (countCloseBrace != countOpenBrace) { // estamos dentro do método
	 * if (s.endsWith(";") || s.endsWith(":")) { //: no caso do switch case
	 * methodName.inc(); //System.out.println(s); } else { nextLineIsBrace = true; }
	 * if (nextLineIsBrace) { countOpenBrace++; nextLineIsBrace = false; } if
	 * (s.contains("{")) { //podia ser com "endswith" mas depois há problema com os
	 * comentarios a seguir as { countOpenBrace++; methodName.inc();
	 * //System.out.println(s); } if (s.contains("}")) { countCloseBrace++; } } } }
	 */
	
	
	
	protected void applyFilter(String s) { // não lida totalmente com blocos de comentário nem metodos internos a outros
		// métodos. também há interferencias na definicão de vetores entre {}
		String[] line = s.split(" ");
		String temp = methodName(s, line);
		s = s.trim();
		if (!s.startsWith("//") && !s.startsWith("*") && !s.startsWith("/*") && !s.startsWith("@")) {
			if (!temp.isBlank()) {
				methodName = new Counter();
				methodName = this.counter(getPackageClassName() + "." + temp);
			} else { // estamos dentro do método
				if (!s.equals("{") && !s.equals("}") && !s.isBlank()) {
					methodName.inc();
				}
			}
		}
	}
}
