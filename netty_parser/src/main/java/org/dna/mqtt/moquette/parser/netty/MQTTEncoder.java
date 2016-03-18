package org.dna.mqtt.moquette.parser.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.HashMap;
import java.util.Map;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage;

/**
 *
 * @author andrea
 */
public class MQTTEncoder extends MessageToByteEncoder<AbstractMessage> {
	
	private Map<Byte, DemuxEncoder>	encoderMap	= new HashMap<Byte, DemuxEncoder>();
	
	public MQTTEncoder() {
		encoderMap.put(AbstractMessage.CONNECT, new ConnectEncoder());
		encoderMap.put(AbstractMessage.CONNACK, new ConnAckEncoder());
		encoderMap.put(AbstractMessage.PUBLISH, new PublishEncoder());
		encoderMap.put(AbstractMessage.PUBACK, new PubAckEncoder());
		encoderMap.put(AbstractMessage.SUBSCRIBE, new SubscribeEncoder());
		encoderMap.put(AbstractMessage.SUBACK, new SubAckEncoder());
		encoderMap.put(AbstractMessage.UNSUBSCRIBE, new UnsubscribeEncoder());
		encoderMap.put(AbstractMessage.DISCONNECT, new DisconnectEncoder());
		encoderMap.put(AbstractMessage.PINGREQ, new PingReqEncoder());
		encoderMap.put(AbstractMessage.PINGRESP, new PingRespEncoder());
		encoderMap.put(AbstractMessage.UNSUBACK, new UnsubAckEncoder());
		encoderMap.put(AbstractMessage.PUBCOMP, new PubCompEncoder());
		encoderMap.put(AbstractMessage.PUBREC, new PubRecEncoder());
		encoderMap.put(AbstractMessage.PUBREL, new PubRelEncoder());
	}
	
	@Override
	protected void encode(ChannelHandlerContext chc, AbstractMessage msg,
			ByteBuf bb) throws Exception {
		DemuxEncoder encoder = encoderMap.get(msg.getMessageType());
		if (encoder == null) {
			throw new CorruptedFrameException(
					"Can't find any suitable decoder for message type: "
							+ msg.getMessageType());
		}
		encoder.encode(chc, msg, bb);
	}
}
