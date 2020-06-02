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

public class SSLClient{

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

    public SSLClient(InetAddress endPoint, int port, File trustKey){
        try {
            final char[] password = "passphrase".toCharArray();

            System.out.println((new File("keystore").exists()));

            final KeyStore keyStore = KeyStore.getInstance(new File("keystore"), password);

            final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("NewSunX509");
            keyManagerFactory.init(keyStore, password);

            final SSLContext context = SSLContext.getInstance("TLS");//"SSL" "TLS"
            context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            final SSLSocketFactory factory = context.getSocketFactory();

            innerSock = (SSLSocket) factory.createSocket(endPoint, port);
        } catch (IOException e) {
            System.err.println(e);
        } catch (Exception e){

        }
        innerSock.setEnabledProtocols(protocols);
        innerSock.setEnabledCipherSuites(cipher_suites);
    }



    public void DoRegisterFromStr(String request){
        try {
            innerSock.startHandshake();
            var out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(innerSock.getOutputStream())));

            // Send request to register the random user
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
    }


    public void RandomRegister(){
        DoRegisterFromStr(RandomWork.requestRandUserReg(appToken));
    }

    public void Register(String name, String surname, String password, Integer phone_num){
        DoRegisterFromStr(ReqFormatter.formatRegister(name, surname, password, phone_num.toString(), appToken));
    }


}
