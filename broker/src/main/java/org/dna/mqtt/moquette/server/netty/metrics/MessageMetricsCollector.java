package org.dna.mqtt.moquette.server.netty.metrics;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Collects all the metrics from the various pipeline.
 */
public class MessageMetricsCollector {
	private Queue<MessageMetrics>	allMetrics	= new ConcurrentLinkedQueue<MessageMetrics>();
	
	void addMetrics(MessageMetrics metrics) {
		this.allMetrics.add(metrics);
	}
	
	public MessageMetrics computeMetrics() {
		MessageMetrics aMetrics = new MessageMetrics();
		for (MessageMetrics m : allMetrics) {
			aMetrics.incrementRead(m.messagesRead());
			aMetrics.incrementWrote(m.messagesWrote());
		}
		return aMetrics;
	}
}
