package com.KartonDCP.Core;
import Configurations.ConfigModel;
import com.KartonDCP.Core.Utils.RequestTypeIdentifier;
import com.KartonDCP.Handlers.Handler;
import com.KartonDCP.Handlers.HttpGetHandler;
import com.KartonDCP.Logger.EventLogger;
import com.KartonDCP.Logger.ILogger;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class Server {


    private int numConnections;
    private int port;
    private String ipAddr;
    private String defualtContentPath;
    private String secretKey;


    private boolean running = false;


    private ServerSocket serverSocket;

    private ILogger logger = new EventLogger();


    public Server() {
        String current = null;
        try {
            current = new File( "." ).getCanonicalPath();
        } catch (IOException e) {
            logger.logException(e);
        }
        Gson gson = new Gson();
        File file = new File(current + "/src/Configurations/config.JSON");
        String strJSON = null;
        try {
            strJSON = Files.readString(file.toPath());
        } catch (IOException e) {
            logger.logException(e);
        }

        var schema = gson.fromJson(strJSON, ConfigModel.class);

        this.port = schema.Port;

        if(schema.LocalPath) {
            this.defualtContentPath = current + schema.Path;
        } else{
            this.defualtContentPath = schema.Path;
        }

        ipAddr = schema.IP;
        secretKey = schema.SecretKey;
        numConnections = schema.NumConnections;

        try {
            serverSocket = new ServerSocket();
        } catch (IOException e) {
            logger.logException(e);
        }
    }



    public boolean startListen() {
        if (running)
        {
            return false;
        }
        running = true;
        System.out.println("Server starts on port " + port + " and ip " + ipAddr +
                " and Listen not more than " + numConnections + " connections");
        System.out.println("Defualt content path: " + defualtContentPath);
        try {
            serverSocket.bind(new InetSocketAddress(InetAddress.getByName(ipAddr), port));
        } catch (IOException e) {
            logger.logException(e);
        }


        Thread requestListenerT = new Thread(() -> {
            while (running) {
                Socket handle = null;
                try {

                    handle = serverSocket.accept();

                    Socket finalHandle = handle;
                    Thread requestHandler = new Thread(() -> {
                        BufferedReader bufferedStreamReader = null;
                        try {
                            bufferedStreamReader = new BufferedReader(new InputStreamReader(finalHandle.getInputStream()));
                        } catch (IOException e) {
                            logger.logException(e);
                        }

                        char buff[] = new char[10024];
                        try {
                            bufferedStreamReader.read(buff);
                        } catch (IOException e) {
                            logger.logException(e);
                        }
                        var requestMsg = new String(buff);

                        if(requestMsg.contains(secretKey) && requestMsg.contains("STOP")){
                            running = false;
                            System.out.println("EXITING...");
                            return;
                        }

                        var typeIdentifier = new RequestTypeIdentifier(requestMsg);
                        String httpMethod = typeIdentifier.getMethodType();
                        String urlPath = typeIdentifier.getRequestPath();

                        Handler httpHandler = null;

                        if(httpMethod.contains("GET")){
                            httpHandler = new HttpGetHandler(urlPath, finalHandle, this.defualtContentPath);
                        } else if(httpMethod == "POST"){
                            //Handle POST method
                        }

                        httpHandler.handleResponse();

                    });
                    requestHandler.start();
                } catch (IOException e) {
                    logger.logException(e);
                }
            }
        });
        requestListenerT.start();
        return running;
    }


}
