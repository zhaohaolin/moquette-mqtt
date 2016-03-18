package org.dna.mqtt.moquette.server.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.HashMap;
import java.util.Map;
import org.dna.mqtt.moquette.messaging.spi.IMessaging;
import org.dna.mqtt.moquette.proto.Utils;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage;
import static org.dna.mqtt.moquette.proto.messages.AbstractMessage.*;
import org.dna.mqtt.moquette.proto.messages.PingRespMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrea
 */
@Sharable
public class NettyMQTTHandler extends ChannelInboundHandlerAdapter {
	
	private static final Logger							LOG				= LoggerFactory
																				.getLogger(NettyMQTTHandler.class);
	private IMessaging									messaging;
	private Map<ChannelHandlerContext, NettyChannel>	channelMapper	= new HashMap<ChannelHandlerContext, NettyChannel>();
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) {
		AbstractMessage msg = (AbstractMessage) message;
		LOG.info(String.format("Received a message of type %s",
				Utils.msgType2String(msg.getMessageType())));
		try {
			switch (msg.getMessageType()) {
				case CONNECT:
				case SUBSCRIBE:
				case UNSUBSCRIBE:
				case PUBLISH:
				case PUBREC:
				case PUBCOMP:
				case PUBREL:
				case DISCONNECT:
					NettyChannel channel;
					synchronized (channelMapper) {
						if (!channelMapper.containsKey(ctx)) {
							channelMapper.put(ctx, new NettyChannel(ctx));
						}
						channel = channelMapper.get(ctx);
					}
					
					messaging.handleProtocolMessage(channel, msg);
					break;
				case PINGREQ:
					PingRespMessage pingResp = new PingRespMessage();
					ctx.write(pingResp);
					break;
			}
		} catch (Exception ex) {
			LOG.error("Bad error in processing the message", ex);
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.close(/* false */);
		synchronized (channelMapper) {
			channelMapper.remove(ctx);
		}
	}
	
	public void setMessaging(IMessaging messaging) {
		this.messaging = messaging;
	}
}
