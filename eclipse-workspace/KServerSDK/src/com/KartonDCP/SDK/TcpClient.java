package com.KartonDCP.SDK;

import com.KartonDCP.SDK.Status.RegStat;
import com.KartonDCP.SDK.Status.RegStatusCode;
import com.KartonDCP.Utils.Random.RandomWork;
import com.KartonDCP.Utils.Streams.StreamUtils;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class TcpClient {

    @SuppressWarnings("SpellCheckingInspection")
    public static final String appToken = "98F1EJJDa4fjwD2fUIHWUd2dsaAsS289IFFFadde3A8213HFI7";

    private static final Logger logger = Logger.getGlobal();
    private Socket innerSock;

    public TcpClient(InetAddress endPoint, int port, SocketFactory factory) {
        try {
            innerSock = factory.createSocket(endPoint, port);
        } catch (IOException e) {
            System.err.println(e);
        } catch (Exception e) {

        }
    }


    @Deprecated
    public TcpClient(InetAddress endPoint, int port, File trustKey) {
        try {
            final char[] password = "passphrase".toCharArray();

            System.out.println(trustKey.exists() ? "TrustKey found !" : "No Trust key!");

            final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore keyStore = null;
            trustManagerFactory.init(keyStore);

            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("NewSunX509");
            keyManagerFactory.init(keyStore, password);

            final SSLContext context = SSLContext.getInstance("TLS");//"SSL" "TLS"
            context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            final SSLSocketFactory factory = context.getSocketFactory();

            innerSock = (SSLSocket) factory.createSocket(endPoint, port);
        } catch (IOException e) {
            System.err.println(e);
        } catch (Exception e) {
            System.err.println(e);
        }
        //innerSock.setEnabledProtocols(protocols);
        //innerSock.setEnabledCipherSuites(cipher_suites);
    }


    public RegStat registerFromStr(String request) {
        try {

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(innerSock.getOutputStream())));

            // Send request to register the random user
            logger.info("SEND REQUEST: " + request);
            out.println(request);
            out.flush();

            //Listening the response from server
            String response = StreamUtils.InputStreamToString(innerSock.getInputStream()); // async get
            logger.info("RECEIVED: " + response);

            UUID token = UUID.fromString(response.split("user_token=")[1].split("&")[0]);

            if (!response.contains("error=100")) {
                String uuid = response.split("UUID=")[1].split("\\s+")[0];
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(innerSock.getOutputStream())));
                System.out.println("Sent: " + uuid);
                out.println(uuid);
                out.flush();

                // Read the first batch of the TcpServer response bytes.

                response = StreamUtils.InputStreamToString(innerSock.getInputStream());  // async get

                logger.info("Received status: " + response);

                out.close();

                return new RegStat(RegStatusCode.OK, token);
            } else {
                return new RegStat(RegStatusCode.USER_EXISTS, null);
            }


        } catch (IOException e) {
            System.err.println(e);
            return new RegStat(RegStatusCode.IO_ERROR, null);
        } finally {
            try {
                innerSock.close();
            } catch (IOException e) {
                System.err.println(e);
                return new RegStat(RegStatusCode.SERVER_ERROR, null);
            }
        }

    }


    public CompletableFuture<RegStat> randomRegisterAsync() {
        CompletableFuture<RegStat> supplier;
        supplier = CompletableFuture.supplyAsync(() -> registerFromStr(RandomWork.requestRandUserReg(appToken)));
        return supplier;
    }


    public CompletableFuture<RegStat> registerAsync(String name, String surname, String password, Integer phone_num) {
        CompletableFuture<RegStat> supplier;
        supplier = CompletableFuture.supplyAsync(() -> registerFromStr(ReqFormatter.formatRegister(name, surname, password, phone_num.toString(), appToken)));
        return supplier;
    }


    public void getSession() {
        try {

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(innerSock.getOutputStream())));

            // Send request to register the random user
            String request = ReqFormatter.formatTheRequest("reg_session", null, appToken);
            System.out.println("SEND REQUEST: " + request);
            out.println(request);
            out.flush();

            //Listening the response from server
            String response = StreamUtils.InputStreamToString(innerSock.getInputStream());
            System.out.println("RECEIVED: " + response);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
