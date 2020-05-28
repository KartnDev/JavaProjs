package com.KartonDCP.SDK;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientStartup {

    public static void main(String[] args) throws UnknownHostException {

        var factory = SocketFactory.getDefault();


        ExecutorService pool = Executors.newFixedThreadPool(3);


        pool.submit(new SSLClient(InetAddress.getByName("127.0.0.1"), 3305, factory));


        pool.shutdown();

    }
}
