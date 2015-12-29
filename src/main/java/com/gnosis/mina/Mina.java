package com.gnosis.mina;

import com.gnosis.util.util.DandikLogger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Firat on 21.10.2014.
 * TODO : It will be like MinaClient.Builder or MinaServer.Builder
 */
@Deprecated
public class Mina {
    public static final Logger logger = new DandikLogger();
    private ConnectFuture future;

    protected Mina() {

    }

    public interface OnConnectedListener {
        void onConnected(IoSession session);
    }
    public interface OnDisconnectedListener {
        void OnDisconnected(IoSession session);
    }

    public interface OnMessageReceivedListener {
        void onMessageReceived(IoSession session, Object message);
    }

    public static class ServerBuilder {
        public int port;
        private OnMessageReceivedListener onMessageReceivedListener;
        private OnConnectedListener onConnectedListener;
        private OnDisconnectedListener onDisconnectedListener;
        private int pinCode = 9876;
        private boolean isSSLEnabled;
        private SSLContext sslContext;
        private boolean isDeepLoggingEnabled;
        private int idleTime=60;

        private String encodingDelimiter ="\n";
        private String decodingDelimiter ="\n";
        private Class protocolCodecFactory;
        public String getEncodingDelimiter() {
            return encodingDelimiter;
        }

        public int getIdleTime() {
            return idleTime;
        }

        public ServerBuilder setIdleTime(int idleTime) {
            this.idleTime = idleTime;
            return this;
        }

        public ServerBuilder setEncodingDelimiter(String encodingDelimiter) {
            this.encodingDelimiter = encodingDelimiter;
            return this;
        }

        public String getDecodingDelimiter() {
            return decodingDelimiter;
        }

        public ServerBuilder setDecodingDelimiter(String decodingDelimiter) {
            this.decodingDelimiter = decodingDelimiter;
            return this;
        }

        public Class getProtocolCodecFactory() {
            return protocolCodecFactory;
        }

        public ServerBuilder setProtocolCodecFactory(Class protocolCodecFactory) {
            this.protocolCodecFactory = protocolCodecFactory;
            return this;
        }

        public boolean isDeepLoggingEnabled() {
            return isDeepLoggingEnabled;
        }

        public ServerBuilder setDeepLoggingEnabled(boolean isDeepLoggingEnabled) {
            this.isDeepLoggingEnabled = isDeepLoggingEnabled;
            return this;
        }

        public int getPort() {
            return port;
        }

        public ServerBuilder setPort(int port) {
            this.port = port;
            return this;
        }

        public OnMessageReceivedListener getOnMessageReceivedListener() {
            return onMessageReceivedListener;
        }

        public ServerBuilder setOnMessageReceivedListener(OnMessageReceivedListener onMessageReceivedListener) {
            this.onMessageReceivedListener = onMessageReceivedListener;
            return this;
        }

        public OnConnectedListener getOnConnectedListener() {
            return onConnectedListener;
        }

        public ServerBuilder setOnConnectedListener(OnConnectedListener onConnectedListener) {
            this.onConnectedListener = onConnectedListener;
            return this;
        }

        public int getPinCode() {
            return pinCode;
        }

        public ServerBuilder setPinCode(int pinCode) {
            this.pinCode = pinCode;
            return this;
        }

        public boolean isSSLEnabled() {
            return isSSLEnabled;
        }

        public ServerBuilder setSSLEnabled(boolean isSSLEnabled) {
            this.isSSLEnabled = isSSLEnabled;
            return this;
        }

        public SSLContext getSslContext() {
            return sslContext;
        }

        public ServerBuilder setSslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        public MinaServer build() {
            return new MinaServer(this);
        }

        public OnDisconnectedListener getOnDisconnectedListener() {
            return onDisconnectedListener;
        }

        public ServerBuilder setOnDisconnectedListener(OnDisconnectedListener onDisconnectedListener) {
            this.onDisconnectedListener = onDisconnectedListener;
            return this;
        }
    }

    public static class ClientBuilder {
        private List<PeriodicMessage> periodicMessages = new ArrayList<PeriodicMessage>();
        private String ip;
        private String encodingDelimiter ="\n";
        private String decodingDelimiter ="\n";

        private long reconnectWaitingTime = 0;
        private long connectTimeOutMillis=1000;
        private int writeTimeOut = 1000;
        public int port;
        private OnMessageReceivedListener onMessageReceivedListener;
        private OnConnectedListener onConnectedListener;
        private OnDisconnectedListener onDisconnectedListener;
        private int pinCode ;
        private boolean isSSLEnabled;
        private SSLContext sslContext;
        private boolean reconnectEnabled=true;
        private Class protocolCodecFactory;
        private int idleTime=60;
        private Charset charset=Charset.defaultCharset();
        public ClientBuilder setReconnectEnabled(boolean reconnectEnabled) {
            this.reconnectEnabled = reconnectEnabled;
            return this;
        }

        public Charset getCharset() {
            return charset;
        }

        public ClientBuilder setCharset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public int getIdleTime() {
            return idleTime;
        }

        public ClientBuilder setIdleTime(int idleTime) {
            this.idleTime = idleTime;
            return this;
        }

        public boolean isDeepLoggingEnabled() {
            return isDeepLoggingEnabled;
        }

        public ClientBuilder setDeepLoggingEnabled(boolean isDeepLoggingEnabled) {
            this.isDeepLoggingEnabled = isDeepLoggingEnabled;
            return this;
        }

        private boolean isDeepLoggingEnabled;
        public int getPort() {
            return port;
        }

        public ClientBuilder setPort(int port) {
            this.port = port;
            return this;
        }

        public OnMessageReceivedListener getOnMessageReceivedListener() {
            return onMessageReceivedListener;
        }

        public ClientBuilder setOnMessageReceivedListener(OnMessageReceivedListener onMessageReceivedListener) {
            this.onMessageReceivedListener = onMessageReceivedListener;
            return this;
        }

        public OnConnectedListener getOnConnectedListener() {
            return onConnectedListener;
        }

        public ClientBuilder setOnConnectedListener(OnConnectedListener onConnectedListener) {
            this.onConnectedListener = onConnectedListener;
            return this;
        }

        public int getPinCode() {
            return pinCode;
        }

        public ClientBuilder setPinCode(int pinCode) {
            this.pinCode = pinCode;
            return this;
        }

        public boolean isSSLEnabled() {
            return isSSLEnabled;
        }

        public ClientBuilder setSSLEnabled(boolean isSSLEnabled) {
            this.isSSLEnabled = isSSLEnabled;
            return this;
        }

        public SSLContext getSslContext() {
            return sslContext;
        }

        public ClientBuilder setSslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        public MinaClient build() {
            return new MinaClient(this);
        }

        public List<PeriodicMessage> getPeriodicMessages() {
            return periodicMessages;
        }

        public ClientBuilder setPeriodicMessages(List<PeriodicMessage> periodicMessages) {
            this.periodicMessages = periodicMessages;
            return this;
        }

        public String getIp() {
            return ip;
        }

        public ClientBuilder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public long getReconnectWaitingTime() {
            return reconnectWaitingTime;
        }

        public ClientBuilder setReconnectWaitingTime(long reconnectWaitingTime) {
            this.reconnectWaitingTime = reconnectWaitingTime;
            return this;
        }

        public long getConnectTimeOutMillis() {
            return connectTimeOutMillis;
        }

        public ClientBuilder setConnectTimeOutMillis(long connectTimeOutMillis) {
            this.connectTimeOutMillis = connectTimeOutMillis;
            return this;
        }

        public int getWriteTimeOut() {
            return writeTimeOut;
        }

        public ClientBuilder setWriteTimeOut(int writeTimeOut) {
            this.writeTimeOut = writeTimeOut;
            return this;
        }


        public ClientBuilder addPeriodicMessage(PeriodicMessage periodicMessage) {
            periodicMessages.add(periodicMessage);
            return this;
        }

        public boolean isReconnectEnabled() {
            return reconnectEnabled;
        }

        public String getEncodingDelimiter() {
            return encodingDelimiter;
        }

        public ClientBuilder setEncodingDelimiter(String encodingDelimiter) {
            this.encodingDelimiter = encodingDelimiter;
            return this;
        }

        public String getDecodingDelimiter() {
            return decodingDelimiter;
        }

        public ClientBuilder setDecodingDelimiter(String decodingDelimiter) {
            this.decodingDelimiter = decodingDelimiter;
            return this;
        }

        public OnDisconnectedListener getOnDisconnectedListener() {
            return onDisconnectedListener;
        }

        public ClientBuilder setOnDisconnectedListener(OnDisconnectedListener onDisconnectedListener) {
            this.onDisconnectedListener = onDisconnectedListener;
            return this;
        }

        public Class getProtocolCodecFactory() {
            return protocolCodecFactory;
        }

        public ClientBuilder setProtocolCodecFactory(Class protocolCodecFactory) {
            this.protocolCodecFactory = protocolCodecFactory;
            return this;
        }
    }


}