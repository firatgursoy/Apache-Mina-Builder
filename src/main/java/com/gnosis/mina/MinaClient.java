package com.gnosis.mina;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Firat Gursoy on 23.10.2014.
 *
 */
public class MinaClient extends Mina {
    private ClientBuilder clientBuilder;
    private ConnectFuture future;
    private List<OnConnectedListener> onConnectedListeners = new ArrayList<OnConnectedListener>();

    public void addOnConnectedListener(Mina.OnConnectedListener onConnectedListener) {
        if (isReady()) {
            onConnectedListener.onConnected(getSession());//if already connected fun(run) it !
            return;
        }
        onConnectedListeners.add(onConnectedListener);
    }

    public void write(Object object) {
        if (isReady()) {
            future.getSession().write(object);
            logger.info("Sent Message" + object);
        }
    }

    public void close() {
        try {
            future.getSession().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isReady() {
        try {
            return (future.getSession().isConnected());//isClosing ?
        } catch (Exception e) {
            logger.info("Mina:Trying reconnect.");
            return false;
        }
    }

    public IoSession getSession() {
        try {
            return future.getSession();
        } catch (Exception e) {
            return null;
        }

    }

    private IoConnector connector;

    public MinaClient(final ClientBuilder minaBuilder) {
        new Thread(new Runnable() {
            @java.lang.Override
            public void run() {
                clientBuilder = (ClientBuilder) minaBuilder;
                try {
                    connector = new NioSocketConnector();
                    connector.getSessionConfig().setReadBufferSize(2048);
                    connector.setConnectTimeoutMillis(clientBuilder.getConnectTimeOutMillis());
                    connector.getSessionConfig().setWriteTimeout(clientBuilder.getWriteTimeOut());
                    connector.getSessionConfig().setUseReadOperation(true);
                    connector.getSessionConfig().setBothIdleTime(clientBuilder.getIdleTime());
                    if (clientBuilder.isSSLEnabled()) {
                        SslFilter sslFilter = new SslFilter(clientBuilder.getSslContext());
                        sslFilter.setUseClientMode(true);
                        connector.getFilterChain().addFirst("sslFilter", sslFilter);
                    }
                    if (clientBuilder.isDeepLoggingEnabled())
                        connector.getFilterChain().addLast("logger", new LoggingFilter());
                    if (clientBuilder.getProtocolCodecFactory() == null) {
                        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(clientBuilder.getCharset(), clientBuilder.getEncodingDelimiter(), clientBuilder.getDecodingDelimiter())));
                    } else {
                        ProtocolCodecFactory protocolCodecFactory = (ProtocolCodecFactory) clientBuilder.getProtocolCodecFactory().newInstance();
                        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(protocolCodecFactory));
                    }
                    ClientHandler clientHandler = new ClientHandler();
                    connector.setHandler(clientHandler);
                    try {
                        future = connector.connect(new InetSocketAddress(clientBuilder.getIp(), clientBuilder.getPort()));
                    } catch (Exception e) {
                        logger.info("Cant Connect");
                    }
                    future = future.awaitUninterruptibly();
                    reconnect();//starts reconnect thread
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("Connect Error");
                }
            }
        }).run();


    }

    // TODO : maybe i can use thread pool at next time.
    // TODO : create an external interrupt method.
    public void reconnect() { //
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (clientBuilder.isReconnectEnabled()) {

                    while (true) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {
                            if (!isReady()) {

                                if (clientBuilder.getOnDisconnectedListener() != null)
                                    clientBuilder.getOnDisconnectedListener().OnDisconnected(getSession());
                                Thread.sleep(clientBuilder.getReconnectWaitingTime());
                                logger.info("Trying Reconnect :" + clientBuilder.getIp() + ":" + clientBuilder.getPort());
                                future = connector.connect(new InetSocketAddress(clientBuilder.getIp(), clientBuilder.getPort()));
                                future = future.awaitUninterruptibly();
                                if (isReady()) {
                                    logger.info("Connected.");

                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                future = connector.connect(new InetSocketAddress(clientBuilder.getIp(), clientBuilder.getPort()));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    private class ClientHandler extends IoHandlerAdapter {
        @Override
        public void sessionClosed(IoSession session) throws Exception {
            logger.info("Session Closed. " + session.getRemoteAddress());
        }

        @Override
        public void sessionOpened(IoSession session) {
            session.getConfig().setUseReadOperation(true);

            if (clientBuilder.getOnConnectedListener() != null)
                clientBuilder.getOnConnectedListener().onConnected(session);
            for (int i = 0; i < onConnectedListeners.size(); i++) {
                onConnectedListeners.get(i).onConnected(session);
                onConnectedListeners.remove(onConnectedListeners.get(i));//connection a��ld���nda g�nderilmesi istenenleri g�nderiyoruz.
            }
        }

        @Override
        public void messageReceived(IoSession session, Object message) {
            if (clientBuilder.getOnMessageReceivedListener() != null)
                clientBuilder.getOnMessageReceivedListener().onMessageReceived(session, message);
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) {

            session.close();
            cause.printStackTrace();
        }
    }

    public void startPeriodicMesages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isReady()) {
                    for (final PeriodicMessage periodicMessage : clientBuilder.getPeriodicMessages()) {
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                write(periodicMessage.getMessage());
                            }
                        };
                        Timer timer = new Timer(true);
                        timer.scheduleAtFixedRate(timerTask, 0, periodicMessage.getPeriod());
                    }
                }
            }
        }).run();
    }
}
