package org.dna.mqtt.moquette.parser.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage;

/**
 *
 * @author andrea
 */
public class MQTTDecoder extends ByteToMessageDecoder {
	
	private Map<Byte, DemuxDecoder>	decoderMap	= new HashMap<Byte, DemuxDecoder>();
	
	public MQTTDecoder() {
		decoderMap.put(AbstractMessage.CONNECT, new ConnectDecoder());
		decoderMap.put(AbstractMessage.CONNACK, new ConnAckDecoder());
		decoderMap.put(AbstractMessage.PUBLISH, new PublishDecoder());
		decoderMap.put(AbstractMessage.PUBACK, new PubAckDecoder());
		decoderMap.put(AbstractMessage.SUBSCRIBE, new SubscribeDecoder());
		decoderMap.put(AbstractMessage.SUBACK, new SubAckDecoder());
		decoderMap.put(AbstractMessage.UNSUBSCRIBE, new UnsubscribeDecoder());
		decoderMap.put(AbstractMessage.DISCONNECT, new DisconnectDecoder());
		decoderMap.put(AbstractMessage.PINGREQ, new PingReqDecoder());
		decoderMap.put(AbstractMessage.PINGRESP, new PingRespDecoder());
		decoderMap.put(AbstractMessage.UNSUBACK, new UnsubAckDecoder());
		decoderMap.put(AbstractMessage.PUBCOMP, new PubCompDecoder());
		decoderMap.put(AbstractMessage.PUBREC, new PubRecDecoder());
		decoderMap.put(AbstractMessage.PUBREL, new PubRelDecoder());
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		in.markReaderIndex();
		if (!Utils.checkHeaderAvailability(in)) {
			in.resetReaderIndex();
			return;
		}
		in.resetReaderIndex();
		
		byte messageType = Utils.readMessageType(in);
		
		DemuxDecoder decoder = decoderMap.get(messageType);
		if (decoder == null) {
			throw new CorruptedFrameException(
					"Can't find any suitable decoder for message type: "
							+ messageType);
		}
		decoder.decode(ctx, in, out);
	}
}
