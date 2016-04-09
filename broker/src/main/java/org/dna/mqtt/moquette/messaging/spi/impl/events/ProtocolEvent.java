package org.dna.mqtt.moquette.messaging.spi.impl.events;

import org.dna.mqtt.moquette.proto.Utils;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage;
import org.dna.mqtt.moquette.server.ServerChannel;

/**
 * Event used to carry ProtocolMessages from front handler to event processor
 */
public class ProtocolEvent extends MessagingEvent {
	private ServerChannel	session;
	private AbstractMessage	message;
	
	public ProtocolEvent(ServerChannel session, AbstractMessage message) {
		this.session = session;
		this.message = message;
	}
	
	public ServerChannel getSession() {
		return session;
	}
	
	public AbstractMessage getMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		return "ProtocolEvent wrapping "
				+ Utils.msgType2String(message.getMessageType());
	}
}
