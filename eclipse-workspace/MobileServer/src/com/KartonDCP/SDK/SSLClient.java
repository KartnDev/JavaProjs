package com.KartonDCP.SDK;

import com.KartonDCP.Utils.Random.RandomWork;
import com.KartonDCP.Utils.Streams.StreamUtils;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

public class SSLClient implements Callable {

    private Socket innerSock;

    @SuppressWarnings("SpellCheckingInspection")
    public static final String appToken = "98F1EJJDa4fjwD2fUIHWUd2dsaAsS289IFFFadde3A8213HFI7";


    public SSLClient(InetAddress endPoint, int port, SocketFactory factory){
        try {
            innerSock = factory.createSocket(endPoint, port);
        } catch (IOException e) {
            System.err.println(e);
        }

    }



    @Override
    public Object call() throws Exception {
        try {

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(innerSock.getOutputStream())));

            // Send request to register the random user
            var request = RandomWork.requestRandUserReg(appToken);
            System.out.println("SEND REQUEST: " + request);
            out.println(request);
            out.flush();

            //Listening the response from server
            var response = StreamUtils.InputStreamToString(innerSock.getInputStream());
            System.out.println("RECEIVED: " + response);

            if (!response.contains("error=100") && request.contains("UUID"))
            {
                String uuid = response.split("UUID=")[1].split("\\s+")[0];

                System.out.println("Sent: " + uuid);
                out.println(uuid);


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
