package com.gnosis.mina.codec;

import com.gnosis.mina.Message;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.serialization.ObjectSerializationEncoder;

/**
 * Created by Firat on 24.10.2014.
 */
public class MessageEncoder extends ObjectSerializationEncoder {
    private PinCodeCodecFactory pinCodeCodecFactory;
    MessageEncoder(int pinCode) {
        pinCodeCodecFactory=new PinCodeCodecFactory(pinCode);
    }

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        if (message instanceof Message) {
            super.encode(session, message, protocolEncoderOutput);
        }else if(message instanceof String){
            pinCodeCodecFactory.getEncoder(session).encode(session, message, protocolEncoderOutput);
        }

    }
}
