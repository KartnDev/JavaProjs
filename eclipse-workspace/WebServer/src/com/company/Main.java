package com.company;

import com.company.Core.Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class Main {

    public static void main(String[] args) {
        Server server = null;

        try {
            server = new Server(1337, "localhost", 10);
            server.startListen();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
