package org.dna.mqtt.moquette.parser.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.dna.mqtt.moquette.parser.netty.TestUtils.*;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage.QOSType;
import org.dna.mqtt.moquette.proto.messages.PublishMessage;

/**
 *
 * @author andrea
 */
public class PublishEncoderTest {
    PublishEncoder m_encoder = new PublishEncoder();
    ChannelHandlerContext m_mockedContext;
    ByteBuf m_out;
         
    @Before
    public void setUp() {
        //mock the ChannelHandlerContext to return an UnpooledAllocator
        m_mockedContext = mockChannelHandler();
        m_out = Unpooled.buffer();
    }
    
    
    @Test
    public void testEncodeWithQos_0_noMessageID() throws Exception {
        String topic = "/photos";
        PublishMessage msg = new PublishMessage();
        msg.setQos(QOSType.MOST_ONE);
        msg.setTopicName(topic);

        //variable part
        byte[] payload = new byte[]{0x0A, 0x0B, 0x0C};
        msg.setPayload(payload);

        //Exercise
        m_encoder.encode(m_mockedContext, msg, m_out);

        //Verify
        assertEquals(0x30, m_out.readByte()); //1 byte
        //(2+7) topic + 3 payload
        assertEquals(12, m_out.readByte()); //remaining length

        //Variable part
        verifyString(topic, m_out);
        verifyBuff(payload.length, payload, m_out);
    }
    
    
    @Test
    public void testEncodeWithQos_1_MessageID() throws Exception {
        String topic = "/photos";
        PublishMessage msg = new PublishMessage();
        msg.setQos(QOSType.LEAST_ONE);
        msg.setMessageID(1);
        msg.setTopicName(topic);

        //variable part
        byte[] payload = new byte[]{0x0A, 0x0B, 0x0C};
        msg.setPayload(payload);

        //Exercise
        m_encoder.encode(m_mockedContext, msg, m_out);

        //Verify
        assertEquals(0x32, m_out.readByte()); //1 byte
        //(2+7) topic + 2 messageID + 3 payload
        assertEquals(14, m_out.readByte()); //remaining length

        //Variable part
        verifyString(topic, m_out);
        assertEquals(0, m_out.readByte()); //MessageID MSB
        assertEquals(1, m_out.readByte()); //MessageID LSB
        verifyBuff(payload.length, payload, m_out);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodeWithQos_1_noMessageID_fail() throws Exception {
        String topic = "pictures/photos";
        PublishMessage msg = new PublishMessage();
        msg.setQos(QOSType.LEAST_ONE);
        msg.setTopicName(topic);

        //Exercise
        m_encoder.encode(m_mockedContext, msg, m_out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncode_empty_topic() throws Exception {
        PublishMessage msg = new PublishMessage();
        msg.setQos(QOSType.LEAST_ONE);
        msg.setMessageID(1);
        ByteBuf out = Unpooled.buffer();

        //Exercise
        m_encoder.encode(m_mockedContext, msg, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncode_null_topic() throws Exception {
        PublishMessage msg = new PublishMessage();
        msg.setQos(QOSType.LEAST_ONE);
        msg.setMessageID(1);
        msg.setTopicName(null);

        //Exercise
        m_encoder.encode(m_mockedContext, msg, m_out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncode_qos_reserved() throws Exception {
        PublishMessage msg = new PublishMessage();
        msg.setQos(QOSType.RESERVED);
        msg.setMessageID(1);
        msg.setTopicName(null);

        //variable part
        byte[] payload = new byte[]{0x0A, 0x0B, 0x0C};
        msg.setPayload(payload);
        ByteBuf out = Unpooled.buffer();

        //Exercise
        m_encoder.encode(m_mockedContext, msg, out);
    }
    
    @Test
    public void testBugMissedMessageIDEncoding() throws Exception{
        String topic = "/topic";
        PublishMessage msg = new PublishMessage();
        msg.setQos(QOSType.MOST_ONE);
        msg.setTopicName(topic);

        //variable part
        byte[] payload = "Test my payload".getBytes();
        msg.setPayload(payload);

        //Exercise
        m_encoder.encode(m_mockedContext, msg, m_out);

        //Verify
        //2 byte header + (2+6) topic name + [2 message ID if QoQ <> 0]+ payload
        int size = m_out.readableBytes();
        assertEquals(10+payload.length, size);
        assertEquals(0x30, m_out.readByte()); //1 byte
        assertEquals(23, m_out.readByte()); //1 byte the length
    }
    
    @Test
    public void testEncodeBigMessage() throws Exception {
        byte[] payload = new byte[256];
        
        TestUtils.generateRandomPayload(256).readBytes(payload);
        
        String topic = "/topic";
        PublishMessage msg = new PublishMessage();
        msg.setQos(QOSType.MOST_ONE);
        msg.setTopicName(topic);

        //variable part
        msg.setPayload(payload);

        //Exercise
        m_encoder.encode(m_mockedContext, msg, m_out);
        
        int size = m_out.readableBytes();
        assertEquals(11+payload.length, size);
        assertEquals(0x30, m_out.readByte()); //1 byte
    }
    
}
