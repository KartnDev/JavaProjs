package com.KartonDCP.MobileSever;

import com.KartonDCP.MobileSever.DirectoryReader.DirReader;
import com.KartonDCP.MobileSever.Utils.Exceptions.BadConfigException;
import com.KartonDCP.MobileSever.Utils.ServerEndPoint;
import com.KartonDCP.DatabaseWorker.DbConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MobileServer {
    private ServerEndPoint endPoint;
    private DbConfig dbConfig;

    private ServerSocket server;
    private volatile boolean serverRunStatus;

    public MobileServer() throws IOException, BadConfigException {
        final DirReader dirReader = new DirReader();
        endPoint = dirReader.GetEndPoint();
        dbConfig = dirReader.GetDbConfig();

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





        return serverRunStatus;
    }





}
