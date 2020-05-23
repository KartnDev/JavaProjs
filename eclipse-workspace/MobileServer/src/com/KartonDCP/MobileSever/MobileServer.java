package com.KartonDCP.MobileSever;

import com.KartonDCP.MobileSever.DirectoryReader.DirReader;
import com.KartonDCP.MobileSever.Utils.Exceptions.BadConfigException;
import com.KartonDCP.MobileSever.Utils.ServerEndPoint;
import com.KartonDCP.DatabaseWorker.DbConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MobileServer {

    private final int MAX_T = 3;

    private ServerEndPoint endPoint;
    private DbConfig dbConfig;

    private volatile ServerSocket server;
    private volatile boolean serverRunStatus;

    public MobileServer() throws IOException, BadConfigException {
        final DirReader dirReader = new DirReader();
        endPoint = dirReader.getEndPoint();
        dbConfig = dirReader.getDbConfig();

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

                });
            } catch (IOException e) {
                //TODO Cannot accept client
            } catch (Exception e){
                //TODO unhandled
            }

        }
    }



}
