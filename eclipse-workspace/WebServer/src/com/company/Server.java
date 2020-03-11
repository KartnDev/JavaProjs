package com.company;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class Server {

    public static final int DEFAULT_PORT = 1337;
    public static final String DEFAULT_INET_ADDR = "localhost";


    private int numConnections;
    private int port;
    private String ipAddr;

    private ServerSocket serverSocket;

    public Server(int port, String ipAddr, int numConnections) throws IOException {
        this.ipAddr = ipAddr;
        this.port = port;
        this.numConnections = numConnections;

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

    public void listen() throws IOException {
        System.out.println("Server starts on port " + port + " and ip " + ipAddr);
        serverSocket.bind(new InetSocketAddress(InetAddress.getByName(ipAddr), port));
        Socket handle = serverSocket.accept();

        var bufferedStreamReader = new BufferedReader(new InputStreamReader(handle.getInputStream()));

        char buff[] = new char[10024];
        bufferedStreamReader.read(buff);
        System.out.println(buff);

    }


}
