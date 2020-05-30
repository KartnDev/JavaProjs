package com.KartonDCP.Server.MobileSever;

import com.KartonDCP.Server.MobileSever.Handler.Handler;
import com.KartonDCP.Server.MobileSever.Handler.MobileCHandler;
import com.KartonDCP.Utils.Exceptions.BadConfigException;
import com.KartonDCP.Utils.Exceptions.InvalidRequestException;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SSLMobileServer extends MobileServer implements Server {

    private static final String[] PROTOCOLS = new String[] {"TLSv1.2"};


    public SSLMobileServer() throws IOException, BadConfigException, SQLException {
        var factory =  SSLServerSocketFactory.getDefault();
        server = factory.createServerSocket(endPoint.getPort(), endPoint.MAX_CONNECTIONS, ipAddr); // why Intellij hides protected fields???
    }


    @Override
    public boolean waitAndShutdown() {
        return false;
    }



    @SuppressWarnings("DuplicatedCode")
    protected void clientListen() {
        ((SSLServerSocket)server).setEnabledProtocols(PROTOCOLS);
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
