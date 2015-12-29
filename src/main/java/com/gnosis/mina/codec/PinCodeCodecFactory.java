package com.gnosis.mina.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.*;
import org.apache.mina.filter.codec.textline.TextLineDecoder;
import org.apache.mina.filter.codec.textline.TextLineEncoder;

/**
 * Created by Firat on 23.10.2014.
 */
class PinCodeCodecFactory implements ProtocolCodecFactory {
    private int pinCode;
    public PinCodeCodecFactory(int pinCode) {
        this.pinCode=pinCode;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return new PinCodeEncoder();
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return new PinCodeDecoder();
    }

    private class PinCodeEncoder implements ProtocolEncoder {
        public String encodeIt(String word, int pinCode) {
            char[] encArr = new char[word.length()];
            for (int i = 0; i < word.length(); i++) {
                encArr[i]= (char)((((int) word.charAt(i) + 113 + pinCode)%222)+33);
            }
            return new String(encArr);
        }

        @Override
        public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {

           /* IoBuffer buffer = IoBuffer.allocate(50, false);
            buffer.put((encodeIt(o.toString(), pinCode) ).getBytes());
            buffer.flip();
            protocolEncoderOutput.write(buffer);*/
            new TextLineEncoder().encode(ioSession, o.toString(), protocolEncoderOutput);
        }

        @Override
        public void dispose(IoSession ioSession) throws Exception {

        }
    }

    private class PinCodeDecoder implements ProtocolDecoder {
        @Override
        public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
            new TextLineDecoder().decode(ioSession, ioBuffer, protocolDecoderOutput);
           /* byte[] bytes={};
            ioBuffer.get(bytes);
            protocolDecoderOutput.write(new String(bytes));*/
        }

        @Override
        public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

        }

        @Override
        public void dispose(IoSession ioSession) throws Exception {

        }
    }
}
