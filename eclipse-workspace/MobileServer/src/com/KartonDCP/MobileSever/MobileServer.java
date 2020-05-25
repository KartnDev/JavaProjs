package com.KartonDCP.MobileSever;

import com.KartonDCP.MobileSever.DirectoryReader.DirReader;
import com.KartonDCP.MobileSever.Handler.MobileCHandler;
import com.KartonDCP.MobileSever.Handler.Handler;
import com.KartonDCP.MobileSever.Utils.Exceptions.BadConfigException;
import com.KartonDCP.MobileSever.Utils.Exceptions.InvalidRequestException;
import com.KartonDCP.MobileSever.Utils.ServerEndPoint;
import com.KartonDCP.DatabaseWorker.Config.DbConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MobileServer {

    private static final int MAX_T = 3;

    private final ServerEndPoint endPoint;
    private final DbConfig dbConfig;
    private final String token;

    private volatile ServerSocket server;
    private volatile boolean serverRunStatus;

    public MobileServer() throws IOException, BadConfigException {
        //Read Config
        final DirReader dirReader = new DirReader();
        endPoint = dirReader.getEndPoint();
        dbConfig = dirReader.getDbConfig();
        token = dirReader.getAppToken();

        server = new ServerSocket();

        var ipAddr = InetAddress.getByName(endPoint.getIp());
        server.bind(new InetSocketAddress(ipAddr, endPoint.getPort()), endPoint.MAX_CONNECTIONS);
    }
    public boolean startServing(){
        if (serverRunStatus)
        {
            return false;
        }
        serverRunStatus = true;
        // TODO LOGGER Info "Server starts on port " + port + " and ip " + ipAddr +
        //                " and Listen not more than " + numConnections + " connections"

        Thread clientLoop = new Thread(() -> clientListen());
        clientLoop.setPriority(Thread.MAX_PRIORITY);
        clientLoop.start();

        return serverRunStatus;
    }

    private void clientListen() {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
        while (serverRunStatus){
            try {
                var client = server.accept();

                pool.execute(() -> {
                    Handler handler = new MobileCHandler(client, token, dbConfig);
                    try {
                        handler.handleSync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InvalidRequestException e){
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });


            } catch (IOException e) {
                //TODO Cannot accept client
            } catch (Exception e){
                //TODO unhandled
            }

        }
    }



}
