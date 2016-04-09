package org.dna.mqtt.moquette.messaging.spi.impl.events;

/**
 * Used to re-fire stored QoS1 QoS2 events once a client reconnects.
 */
public class RepublishEvent extends MessagingEvent {
	
	private String	clientID;
	
	public RepublishEvent(String clientID) {
		this.clientID = clientID;
	}
	
	public String getClientID() {
		return clientID;
	}
}
