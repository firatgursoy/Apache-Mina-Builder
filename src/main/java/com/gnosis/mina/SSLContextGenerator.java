package com.gnosis.mina;

import org.apache.mina.filter.ssl.KeyStoreFactory;
import org.apache.mina.filter.ssl.SslContextFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.security.KeyStore;

/**
 * Created by Firat on 23.10.2014.
 */
public class SSLContextGenerator {
    public static SSLContext getSslContext(File keyStoreFile,File trustStoreFile,String keyStorePassword,String trushStorePassword)
    {
        SSLContext sslContext = null;
        try
        {
            if (keyStoreFile.exists() && trustStoreFile.exists())
            {
                final KeyStoreFactory keyStoreFactory = new KeyStoreFactory();
                System.out.println("Url is: " + keyStoreFile.getAbsolutePath());
                keyStoreFactory.setDataFile(keyStoreFile);
                keyStoreFactory.setPassword(keyStorePassword);

                final KeyStoreFactory trustStoreFactory = new KeyStoreFactory();
                trustStoreFactory.setDataFile(trustStoreFile);
                trustStoreFactory.setPassword(trushStorePassword);

                final SslContextFactory sslContextFactory = new SslContextFactory();
                final KeyStore keyStore = keyStoreFactory.newInstance();
                sslContextFactory.setKeyManagerFactoryKeyStore(keyStore);

                final KeyStore trustStore = trustStoreFactory.newInstance();
                sslContextFactory.setTrustManagerFactoryKeyStore(trustStore);
                sslContextFactory.setKeyManagerFactoryKeyStorePassword(keyStorePassword);
                sslContext = sslContextFactory.newInstance();
                System.out.println("SSL provider is: " + sslContext.getProvider());
            }
            else
            {
                System.out.println("Keystore or Truststore file does not exist");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return sslContext;
    }
}
