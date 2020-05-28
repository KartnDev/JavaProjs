package com.KartonDCP.SDK;

import com.KartonDCP.Utils.Random.RandomWork;
import com.KartonDCP.Utils.Streams.StreamUtils;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;

public class SSLClient implements Runnable{

    private SSLSocket innerSock;

    @SuppressWarnings("SpellCheckingInspection")
    public static final String appToken = "98F1EJJDa4fjwD2fUIHWUd2dsaAsS289IFFFadde3A8213HFI7";


    public SSLClient(InetAddress endPoint, int port, SSLSocketFactory factory){
        try {
            innerSock = (SSLSocket) factory.createSocket(endPoint, port);
        } catch (IOException e) {
            System.err.println(e);
        }

    }



    @Override
    public void run() {
        try {
            innerSock.startHandshake();
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(innerSock.getOutputStream())));

            // Send request to register the random user
            var request = RandomWork.requestRandUserReg(appToken);
            System.out.println("SEND REQUEST: " + request);
            out.println(request);
            out.flush();

            //Listening the response from server
            var response = StreamUtils.InputStreamToString(innerSock.getInputStream());
            

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
