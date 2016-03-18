package org.dna.mqtt.moquette.server;

/**
 * Maintains the information of single connection, like ClientID, IoSession, and
 * other connection related flags.
 * 
 * 
 * @author andrea
 */
public class ConnectionDescriptor {
	
	private String			clientID;
	private ServerChannel	session;
	private boolean			cleanSession;
	
	public ConnectionDescriptor(String clientID, ServerChannel session,
			boolean cleanSession) {
		this.clientID = clientID;
		this.session = session;
		this.cleanSession = cleanSession;
	}
	
	public boolean isCleanSession() {
		return cleanSession;
	}
	
	public String getClientID() {
		return clientID;
	}
	
	public ServerChannel getSession() {
		return session;
	}
}
