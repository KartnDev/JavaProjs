package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Server server = null;
        try {
            server = new Server(1337, "localhost", 10);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
