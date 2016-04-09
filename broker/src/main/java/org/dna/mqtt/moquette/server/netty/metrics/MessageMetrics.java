package org.dna.mqtt.moquette.server.netty.metrics;

public class MessageMetrics {
	
	private long	messagesRead	= 0;
	private long	messageWrote	= 0;
	
	void incrementRead(long numMessages) {
		this.messagesRead += numMessages;
	}
	
	void incrementWrote(long numMessages) {
		this.messageWrote += numMessages;
	}
	
	public long messagesRead() {
		return messagesRead;
	}
	
	public long messagesWrote() {
		return messageWrote;
	}
}
