package com.company.Core;
import Configurations.ConfigModel;
import com.company.Core.Utils.RequestTypeIdentifier;
import com.company.Handlers.Handler;
import com.company.Handlers.HttpGetHandler;
import com.google.gson.Gson;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class Server {

    public static final int DEFAULT_PORT = 1337;
    public static final String DEFAULT_INET_ADDR = "localhost";


    private int numConnections;
    private int port;
    private String ipAddr;
    private String defualtContentPath;

    private boolean running = false;


    private ServerSocket serverSocket;

    public Server(int port, String ipAddr, int numConnections) throws IOException {

        this.ipAddr = ipAddr;
        this.port = port;
        this.numConnections = numConnections;


        if(port == -1){
            String current = new java.io.File( "." ).getCanonicalPath();
            Gson gson = new Gson();
            File file = new File(current + "/src/Configurations/config.JSON");
            var strJSON = Files.readString(file.toPath());

            var schema = gson.fromJson(strJSON, ConfigModel.class);

            this.port = schema.port;

            if(schema.localPath) {
                this.defualtContentPath = current + schema.path;
            } else{
              this.defualtContentPath = schema.path;
            }
        }



        serverSocket = new ServerSocket();
    }
    public Server(int numConnections) throws IOException {
        this.ipAddr = DEFAULT_INET_ADDR;
        this.port = DEFAULT_PORT;
        this.numConnections = numConnections;

        serverSocket = new ServerSocket();
    }

    public Server(int port, int numConnections) throws IOException {
        this.ipAddr = DEFAULT_INET_ADDR;
        this.port = port;
        this.numConnections = numConnections;

        serverSocket = new ServerSocket();

    }

    public boolean startListen() throws IOException {
        if (running)
        {
            return false;
        }
        running = true;
        System.out.println("Server starts on port " + port + " and ip " + ipAddr);
        serverSocket.bind(new InetSocketAddress(InetAddress.getByName(ipAddr), port));


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
                            e.printStackTrace();
                        }

                        char buff[] = new char[10024];
                        try {
                            bufferedStreamReader.read(buff);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        var typeIdentifier = new RequestTypeIdentifier(new String(buff));
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
                    e.printStackTrace();
                }
            }
        });
        requestListenerT.start();
        return running;
    }


}
