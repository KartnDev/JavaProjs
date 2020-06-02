package com.KartonDCP.SDK;

import com.KartonDCP.Utils.Random.RandomWork;
import com.KartonDCP.Utils.Streams.StreamUtils;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.util.concurrent.Callable;

public class SSLClient implements Callable {

    private SSLSocket innerSock;

    @SuppressWarnings("SpellCheckingInspection")
    public static final String appToken = "98F1EJJDa4fjwD2fUIHWUd2dsaAsS289IFFFadde3A8213HFI7";

    private static final String[] protocols = new String[] {"TLSv1.3"};
    private static final String[] cipher_suites = new String[] {"TLS_AES_128_GCM_SHA256"};



    public SSLClient(InetAddress endPoint, int port, SocketFactory factory){
        try {

            innerSock = (SSLSocket) factory.createSocket(endPoint, port);
        } catch (IOException e) {
            System.err.println(e);
        } catch (Exception e){

        }
        innerSock.setEnabledProtocols(protocols);
        innerSock.setEnabledCipherSuites(cipher_suites);
    }



    @Override
    public Object call() throws Exception {
        try {
            innerSock.startHandshake();
            var out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(innerSock.getOutputStream())));

            // Send request to register the random user
            var request = RandomWork.requestRandUserReg(appToken);
            System.out.println("SEND REQUEST: " + request);
            out.println(request);
            out.flush();

            //Listening the response from server
            var response = StreamUtils.InputStreamToString(innerSock.getInputStream());
            System.out.println("RECEIVED: " + response);

            if (!response.contains("error=100"))
            {
                String uuid = response.split("UUID=")[1].split("\\s+")[0];
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(innerSock.getOutputStream())));
                System.out.println("Sent: " + uuid);
                out.println(uuid);
                out.flush();

                // Read the first batch of the TcpServer response bytes.

                response = StreamUtils.InputStreamToString(innerSock.getInputStream());

                System.out.println("Received status: " + response);
            }
            else
            {
                System.out.println("User exists!");
            }
            out.close();

        } catch (IOException e) {
            System.err.println(e);
        } finally {
            try {
                innerSock.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        return 0;
    }

}
