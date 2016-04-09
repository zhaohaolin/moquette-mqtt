package org.dna.mqtt.moquette.messaging.spi.impl.events;

/**
 * Used to send the ack message back to the client after a publish
 */
public class PubAckEvent extends MessagingEvent {
	
	private int		messageId;
	private String	clientID;
	
	public PubAckEvent(int messageID, String clientID) {
		this.messageId = messageID;
		this.clientID = clientID;
	}
	
	public int getMessageId() {
		return messageId;
	}
	
	public String getClientID() {
		return clientID;
	}
}
