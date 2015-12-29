package com.gnosis.mina.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.IoBufferWrapper;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.serialization.ObjectSerializationDecoder;

/**
 * Created by Firat on 24.10.2014.
 */
public class MessageDecoder extends ObjectSerializationDecoder {
    private int pinCode;
    private PinCodeCodecFactory pinCodeCodecFactory;

    MessageDecoder(int pinCode) {
        this.pinCode = pinCode;
        pinCodeCodecFactory=new PinCodeCodecFactory(pinCode);
    }

    @Override
    public void decode(IoSession session, IoBuffer ioBuffer, ProtocolDecoderOutput out) throws Exception {

        if(new String(IoBufferWrapper.wrap(ioBuffer.array()).array()).contains("com.gnosis.mina.Message")){
            super.decode(session, ioBuffer, out);

        }else{
            pinCodeCodecFactory.getDecoder(session).decode(session, ioBuffer, out);

        }
    }
}
