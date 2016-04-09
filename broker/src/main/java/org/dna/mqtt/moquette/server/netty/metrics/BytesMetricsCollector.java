package org.dna.mqtt.moquette.server.netty.metrics;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Collects all the metrics from the various pipeline.
 */
public class BytesMetricsCollector {
	
	private Queue<BytesMetrics>	allMetrics	= new ConcurrentLinkedQueue<BytesMetrics>();
	
	void addMetrics(BytesMetrics metrics) {
		this.allMetrics.add(metrics);
	}
	
	public BytesMetrics computeMetrics() {
		BytesMetrics aMetrics = new BytesMetrics();
		for (BytesMetrics m : allMetrics) {
			aMetrics.incrementRead(m.readBytes());
			aMetrics.incrementWrote(m.wroteBytes());
		}
		return aMetrics;
	}
}
