package com.KartonDCP.MobileSever;

import com.KartonDCP.MobileSever.Handler.MobileCHandler;
import com.KartonDCP.MobileSever.Handler.Handler;
import com.KartonDCP.MobileSever.Utils.Exceptions.BadConfigException;
import com.KartonDCP.MobileSever.Utils.Exceptions.InvalidRequestException;

import javax.net.ssl.SSLServerSocket;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TcpMobileServer extends MobileServer {

    public TcpMobileServer() throws IOException, BadConfigException, SQLException {
        server = new ServerSocket();

        server.bind(new InetSocketAddress(ipAddr, endPoint.getPort()), endPoint.MAX_CONNECTIONS);
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

    @Override
    public boolean waitAndShutdown() {
        return false;
    }


}
