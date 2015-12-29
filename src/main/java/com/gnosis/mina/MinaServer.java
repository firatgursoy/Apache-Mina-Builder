package com.gnosis.mina;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Firat on 23.10.2014.
 */
public class MinaServer extends Mina {
    ServerBuilder serverBuilder;

    private class ServerHandler extends IoHandlerAdapter {
        @Override
        public void sessionClosed(IoSession session) throws Exception {
            serverBuilder.getOnDisconnectedListener().OnDisconnected(session);
            logger.info("Session Closed." + session.getRemoteAddress());
        }

        @Override
        public void sessionOpened(IoSession session) {
            if (serverBuilder.getOnConnectedListener() != null)
                serverBuilder.getOnConnectedListener().onConnected(session);
        }


        @Override
        public void messageReceived(IoSession session, Object message) {
            if (serverBuilder.getOnMessageReceivedListener() != null)
                serverBuilder.getOnMessageReceivedListener().onMessageReceived(session, message);
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) {
            cause.printStackTrace();
            session.close();
        }

        @Override
        public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
            session.close(true);
        }

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            super.messageSent(session, message);
        }
    }

    public IoAcceptor getIOAcceptor() {
        return acceptor;
    }

    private IoAcceptor acceptor;

    public MinaServer(ServerBuilder serverBuilder) {
        try {
            this.serverBuilder = serverBuilder;
            SimpleIoProcessorPool<NioSession> pool =
                    new SimpleIoProcessorPool<NioSession>(NioProcessor.class, 16);
            acceptor = new NioSocketAcceptor(pool);
            if (serverBuilder.isSSLEnabled()) {
                SslFilter sslFilter = new SslFilter(serverBuilder.getSslContext());
                acceptor.getFilterChain().addFirst("sslFilter", sslFilter);
            }

            if (serverBuilder.isDeepLoggingEnabled())
                acceptor.getFilterChain().addLast("logger", new LoggingFilter());
            Executor executor = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
            acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(executor));
            if(serverBuilder.getProtocolCodecFactory()==null) {
                acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.defaultCharset(), serverBuilder.getEncodingDelimiter(), serverBuilder.getDecodingDelimiter())));
            }else {
                ProtocolCodecFactory protocolCodecFactory= (ProtocolCodecFactory) serverBuilder.getProtocolCodecFactory().newInstance();
                acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(protocolCodecFactory));
            }
            acceptor.getSessionConfig().setReadBufferSize(2048);
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, serverBuilder.getIdleTime());
            ServerHandler serverHandler = new ServerHandler();
            acceptor.setHandler(serverHandler);

            acceptor.bind(new InetSocketAddress(serverBuilder.getPort()));

        }catch ( java.net.BindException bex){
            logger.info("Address already using.");
            System.exit(-1);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }


}
