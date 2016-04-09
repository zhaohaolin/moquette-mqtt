package org.dna.mqtt.moquette.messaging.spi.impl.events;

import org.dna.mqtt.moquette.proto.messages.AbstractMessage.QOSType;

import java.io.Serializable;
import org.dna.mqtt.moquette.server.ServerChannel;

/**
 * 
 * @author andrea
 */
public class PublishEvent extends MessagingEvent implements Serializable {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;
	private String					topic;
	private QOSType					qos;
	private byte[]					message;
	private boolean					retain;
	private String					clientID;
	// Optional attribute, available only fo QoS 1 and 2
	private int						msgID;
	
	private transient ServerChannel	session;
	
	public PublishEvent(String topic, QOSType qos, byte[] message,
			boolean retain, String clientID, ServerChannel session) {
		this.topic = topic;
		this.qos = qos;
		this.message = message;
		this.retain = retain;
		this.clientID = clientID;
		this.session = session;
	}
	
	public PublishEvent(String topic, QOSType qos, byte[] message,
			boolean retain, String clientID, int msgID, ServerChannel session) {
		this(topic, qos, message, retain, clientID, session);
		this.msgID = msgID;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public QOSType getQos() {
		return qos;
	}
	
	public byte[] getMessage() {
		return message;
	}
	
	public boolean isRetain() {
		return retain;
	}
	
	public String getClientID() {
		return clientID;
	}
	
	public int getMessageID() {
		return msgID;
	}
	
	public ServerChannel getSession() {
		return session;
	}
	
	@Override
	public String toString() {
		return "PublishEvent{" + "m_msgID=" + msgID + ", m_clientID='"
				+ clientID + '\'' + ", m_retain=" + retain + ", m_qos=" + qos
				+ ", m_topic='" + topic + '\'' + '}';
	}
}
