package org.dna.mqtt.moquette.messaging.spi.impl.events;

import org.dna.mqtt.moquette.messaging.spi.impl.subscriptions.Subscription;

/**
 * 
 * @author andrea
 */
public class SubscribeEvent extends MessagingEvent {
	
	private Subscription	subscription;
	
	private int				messageID;
	
	public SubscribeEvent(Subscription subscription, int messageID) {
		this.subscription = subscription;
		this.messageID = messageID;
	}
	
	public Subscription getSubscription() {
		return subscription;
	}
	
	public int getMessageID() {
		return messageID;
	}
}
