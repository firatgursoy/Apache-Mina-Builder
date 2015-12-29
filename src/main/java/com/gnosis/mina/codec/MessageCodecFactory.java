package com.gnosis.mina.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;

/**
 * Created by Firat on 24.10.2014.
 */
public class MessageCodecFactory extends ObjectSerializationCodecFactory {
    private int pinCode;

    public MessageCodecFactory(int pinCode) {
        this.pinCode = pinCode;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) {
        return new MessageEncoder(pinCode);
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) {
        return new MessageDecoder(pinCode);
    }
}
