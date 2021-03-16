package metricas;

import java.util.ArrayList;
import java.util.SortedMap;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

public class Metricas extends MetricRegistry {

	private LOC_class LOC_class;
	private NOM_class NOM_class;
	private WMC_class WMC_class;
	private LOC_method LOC_method;
	private CYCLO_method CYCLO_method;
	private ArrayList<Metricas> metricas;

	public Metricas() {
		metricas = new ArrayList<Metricas>();

	}

	public static void main(String[] args) {

		Metricas otario = new Metricas();
		otario.FazMetricas();
		otario.daValores();

//			Counter counterWVMnum= metricas.counter("WVM_num");
//			counterWVMnum.inc();
//			counterWVMnum.inc();
//			counterWVMnum.inc();
//			counterWVMnum.inc();

//			SortedMap<String, Counter> helloMap = metricas.getCounters();

//			System.out.println(helloMap.get("WVM_num").getCount());

	}

	public void FazMetricas() {
		LOC_class = new LOC_class();
		NOM_class = new NOM_class();
		WMC_class = new WMC_class();
		LOC_method = new LOC_method();
		CYCLO_method = new CYCLO_method();
		metricas.add(LOC_class);
		metricas.add(NOM_class);
		metricas.add(WMC_class);
		metricas.add(LOC_method);
		metricas.add(CYCLO_method);

	}

	public void daValores() {
		for (Metricas m : metricas) {
			SortedMap<String, Counter> helloMap = m.getCounters();
			Object[] counters = helloMap.values().toArray();
			System.out.println(((Counter) counters[0]).getCount());
			System.out.println(helloMap.values());
			System.out.println(helloMap.keySet());
			System.out.println(m.getCounters());
		}

	}

//		public abstract int contagem();
//		public abstract String nomeString();
	public void TodasAsTuasContagens() {
	}

	public void fazThread() {
		Thread t = new Thread(new Runnable() {

			public void run() {
				TodasAsTuasContagens();
			}
		});
		t.start();
		t.interrupt();		
	}

//		public String getLOC_class() {
//			return LOC_class;
//		}
//
//
//		public String getNOM_class() {
//			return NOM_class;
//		}
//
//
//		public String getWMC_class() {
//			return WMC_class;
//		}
//
//
//		public String getLOC_method() {
//			return LOC_method;
//		}
//
//
//		public String getCYCLO_method() {
//			return CYCLO_method;
//		}

}
