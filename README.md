# Apache-Mina-Builder
Builder for creating reconnectable Apache Mina applications.

   This is example for create a server and client connection.
   
    MinaServer minaServer = new Mina.ServerBuilder()
    .setDeepLoggingEnabled(false)
    .setSSLEnabled(false)
    .setPort(port)
    .setOnMessageReceivedListener((iOSession, o) -> {
     //do something
    })
    .setOnConnectedListener((iOSession, o) -> {
     //do something
    }).setOnDisconnectedListener((iOSession, o) -> {
     //do something
    }).build();
    
Client

    MinaClient minaClient = new Mina.ClientBuilder()
    .setConnectTimeOutMillis(5000)
    .setReconnectWaitingTime(100)
    .setReconnectEnabled(true)
    .setDecodingDelimiter("\n")
    .setSSLEnabled(false)
    .setDeepLoggingEnabled(false)
    .setEncodingDelimiter("\n")
    .setIp(ip)
    .setPort(port)
    .setOnConnectedListener((iOSession) -> {
     //do something when the connection is established 
    }).setOnDisconnectedListener((iOSession) -> {
     //do something
    })
    .setOnMessageReceivedListener((iOSession, o) -> {
     //do something
    }).build();

    minaClient.addOnConnectedListener((iOSession) -> {
     //do something when the connection is established after creation of instance
    });
    
    minaClient.write("Hi !");
    
    
