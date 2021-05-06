package metricas;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;

public class MetricRegistryToTest extends MetricRegistry {//TODO nomes

	private ConcurrentMap<String, Counter> hello = new ConcurrentHashMap<String, Counter>();

	@Override
	public SortedMap<String, Counter> getCounters() {
		// TODO Auto-generated method stub
		return getCounters(MetricFilter.ALL);
	}

	@Override
	public SortedMap<String, Counter> getCounters(MetricFilter filter) {
		return getMetrics(Counter.class, filter);
	}

	@SuppressWarnings("unchecked")
	private <T extends Metric> SortedMap<String, T> getMetrics(Class<T> klass, MetricFilter filter) {
		final TreeMap<String, T> timers = new TreeMap<>();
		for (Map.Entry<String, Counter> entry : hello.entrySet()) {
			if (klass.isInstance(entry.getValue()) && filter.matches(entry.getKey(), entry.getValue())) {
				timers.put(entry.getKey(), (T) entry.getValue());
			}
		}
		return timers;
	}

	public ConcurrentMap<String, Counter> buildMap(SortedMap<String, Counter> sm) {
		ConcurrentMap<String, Counter> teste = new ConcurrentHashMap<String, Counter>(sm);
		hello = teste;
		return teste;
	}

	private MetricRegistry setCounters(SortedMap<String, Counter> sm) {
		this.buildMap(sm);
		return this;
	}

	public MetricRegistry addCounter(String key, Counter value) {
		SortedMap<String, Counter> newMr = this.getCounters();

		newMr.put(key, value);
		setCounters(newMr);

		return this;

	}
	
	public MetricRegistry addCounterList(String[] key, int[] value) {
		SortedMap<String, Counter> newMr = this.getCounters();

		for (int i = 0; i < key.length; i++) {
			Counter c = new Counter();
			c.inc(value[i]);
			newMr.put(key[i], c);
		}
		
		setCounters(newMr);

		return this;

	}
}
