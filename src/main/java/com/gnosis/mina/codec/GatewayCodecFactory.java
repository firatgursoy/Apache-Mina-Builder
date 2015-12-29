package com.gnosis.mina.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.TextLineDecoder;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by pcc on 29.01.2015.
 */
public class GatewayCodecFactory implements ProtocolCodecFactory {
    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return new ProtocolEncoder() {
            @Override
            public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
                int maxLineLength = Integer.MAX_VALUE;
                Charset charset=Charset.defaultCharset();
                final AttributeKey ENCODER = new AttributeKey(getClass(), "encoder");

                CharsetEncoder encoder = (CharsetEncoder) session.getAttribute(ENCODER);

                if (encoder == null) {
                    encoder = charset.newEncoder();
                    session.setAttribute(ENCODER, encoder);
                }

                String value = (message == null ? "" : message.toString());
                IoBuffer buf = IoBuffer.allocate(value.length()).setAutoExpand(true);
                buf.putString(value, encoder);

                if (buf.position() > maxLineLength) {
                    throw new IllegalArgumentException("Line length: " + buf.position());
                }
                buf.flip();
                out.write(buf);
            }

            @Override
            public void dispose(IoSession session) throws Exception {

            }
        };
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return new TextLineDecoder(Charset.defaultCharset(),"\n");
    }
}
