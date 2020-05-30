package com.KartonDCP.Server.MobileSever;

import com.KartonDCP.Server.MobileSever.Handler.Handler;
import com.KartonDCP.Server.MobileSever.Handler.MobileCHandler;
import com.KartonDCP.Utils.Exceptions.InvalidRequestException;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SSLMobileServer extends MobileServer implements Server {

    private static final String[] protocols = new String[] {"TLSv1.3"};
    private static final String[] cipher_suites = new String[] {"TLS_AES_128_GCM_SHA256"};

    public SSLMobileServer() throws  Exception {
        final char[] password = "passphrase".toCharArray();

        System.out.println((new File("keystore").exists()));

        final KeyStore keyStore = KeyStore.getInstance(new File("keystore"), password);

        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("NewSunX509");
        keyManagerFactory.init(keyStore, password);

        final SSLContext context = SSLContext.getInstance("TLS");//"SSL" "TLS"
        context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        final SSLServerSocketFactory factory = context.getServerSocketFactory();

        server = factory.createServerSocket(endPoint.getPort(), endPoint.MAX_CONNECTIONS, ipAddr); // why Intellij hides protected fields???
        ((SSLServerSocket)server).setEnabledProtocols(protocols);
        ((SSLServerSocket)server).setEnabledCipherSuites(cipher_suites);
    }


    @Override
    public boolean waitAndShutdown() {
        return false;
    }



    @SuppressWarnings("DuplicatedCode")
    protected void clientListen() {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
        while (serverRunStatus){

            Socket client = null;
            try {
                client = server.accept();
            } catch (IOException e) {
                e.printStackTrace();
                logger.info(e, "Exception while accepting");
            }
            Socket cFinalSocket = client;

            pool.execute(() -> {
                Handler handler = new MobileCHandler(cFinalSocket, token, dbConfig);
                var exceptionMsg = "Exception in pool thread while handling";
                try {
                    handler.handleSync();
                    logger.info("Start new handler t client");
                }  catch (InvalidRequestException e) {
                    e.printStackTrace();
                    logger.info(e, exceptionMsg);
                } catch (NoSuchFieldException e) {
                    logger.info(e, exceptionMsg);
                    e.printStackTrace();
                } catch (SQLException e) {
                    logger.info(e, exceptionMsg);
                    e.printStackTrace();
                } catch (IOException e) {
                    logger.info(e, exceptionMsg);
                    e.printStackTrace();
                }
            });
        }
    }
}
