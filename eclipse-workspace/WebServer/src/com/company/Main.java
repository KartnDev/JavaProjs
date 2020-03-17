package com.company;

import com.company.Core.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        int port = -1;

        if(args.length == 1){
            port = Integer.parseInt(args[0]);
        }


        Server server = null;

        try {
            server = new Server(-1, "localhost", 10);
            server.startListen();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
