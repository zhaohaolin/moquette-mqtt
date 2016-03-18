package org.dna.mqtt.moquette.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import java.util.HashMap;
import java.util.Map;
import org.dna.mqtt.moquette.server.Constants;
import org.dna.mqtt.moquette.server.ServerChannel;

/**
 *
 * @author andrea
 */
public class NettyChannel implements ServerChannel {
	
	private ChannelHandlerContext				channel;
	
	private Map<Object, AttributeKey<Object>>	attributesKeys			= new HashMap<Object, AttributeKey<Object>>();
	
	private static final AttributeKey<Object>	ATTR_KEY_KEEPALIVE		= new AttributeKey<Object>(
																				Constants.KEEP_ALIVE);
	private static final AttributeKey<Object>	ATTR_KEY_CLEANSESSION	= new AttributeKey<Object>(
																				Constants.CLEAN_SESSION);
	private static final AttributeKey<Object>	ATTR_KEY_CLIENTID		= new AttributeKey<Object>(
																				Constants.ATTR_CLIENTID);
	
	NettyChannel(ChannelHandlerContext ctx) {
		channel = ctx;
		attributesKeys.put(Constants.KEEP_ALIVE, ATTR_KEY_KEEPALIVE);
		attributesKeys.put(Constants.CLEAN_SESSION, ATTR_KEY_CLEANSESSION);
		attributesKeys.put(Constants.ATTR_CLIENTID, ATTR_KEY_CLIENTID);
	}
	
	public Object getAttribute(Object key) {
		Attribute<Object> attr = channel.attr(mapKey(key));
		return attr.get();
	}
	
	public void setAttribute(Object key, Object value) {
		Attribute<Object> attr = channel.attr(mapKey(key));
		attr.set(value);
	}
	
	private synchronized AttributeKey<Object> mapKey(Object key) {
		if (!attributesKeys.containsKey(key)) {
			throw new IllegalArgumentException(
					"mapKey can't find a matching AttributeKey for " + key);
		}
		return attributesKeys.get(key);
	}
	
	public void setIdleTime(int idleTime) {
		if (channel.pipeline().names().contains("idleStateHandler")) {
			channel.pipeline().remove("idleStateHandler");
		}
		if (channel.pipeline().names().contains("idleEventHandler")) {
			channel.pipeline().remove("idleEventHandler");
		}
		channel.pipeline().addFirst("idleStateHandler",
				new IdleStateHandler(0, 0, idleTime));
		channel.pipeline().addAfter("idleStateHandler", "idleEventHandler",
				new MoquetteIdleTimoutHandler());
	}
	
	public void close(boolean immediately) {
		channel.close();
	}
	
	public void write(Object value) {
		channel.writeAndFlush(value);
	}
	
}
