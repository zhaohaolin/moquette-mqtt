package org.dna.mqtt.moquette.server.netty.metrics;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Collects all the metrics from the various pipeline.
 */
public class MessageMetricsCollector {
	private Queue<MessageMetrics>	_allMetrics	= new ConcurrentLinkedQueue<MessageMetrics>();
	
	void addMetrics(MessageMetrics metrics) {
		_allMetrics.add(metrics);
	}
	
	public MessageMetrics computeMetrics() {
		MessageMetrics allMetrics = new MessageMetrics();
		for (MessageMetrics m : _allMetrics) {
			allMetrics.incrementRead(m.messagesRead());
			allMetrics.incrementWrote(m.messagesWrote());
		}
		return allMetrics;
	}
}
